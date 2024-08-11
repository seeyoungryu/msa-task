# 📘 MSA 구성, Redis 캐싱, Docker 기반 CI/CD 프로젝트 구현 레파지토리

![Untitled](https://github.com/user-attachments/assets/8d008528-893a-40d8-aa29-843487d92d7d)

## 실행 순서와 확인 방법

전체 서비스 흐름의 실행 순서와 확인 방법입니다.

<details>
<summary>1. Eureka Server 실행</summary>
1. `server` 어플리케이션을 실행합니다.
2. 브라우저에서 `http://localhost:19090`로 접속하여 Eureka 서버 대시보드를 확인합니다.
</details>

<details>
<summary>2. Gateway 서비스 실행</summary>
1. `gateway` 어플리케이션을 실행합니다.
2. Eureka 대시보드에서 `gateway` 서비스가 등록되었는지 확인합니다.
</details>

<details>
<summary>3. Auth 서비스 실행</summary>
1. `auth` 어플리케이션을 실행합니다.
2. Eureka 대시보드에서 `auth` 서비스가 등록되었는지 확인합니다.
</details>

<details>
<summary>4. Product 서비스 실행 (두 인스턴스)</summary>
1. `product` 어플리케이션을 두 번 실행합니다. 각 인스턴스의 포트는 `19093`과 `19094`로 설정합니다.
2. Eureka 대시보드에서 두 개의 `product` 서비스 인스턴스가 등록되었는지 확인합니다.
</details>

<details>
<summary>5. Order 서비스 실행</summary>
1. `order` 어플리케이션을 실행합니다.
2. Eureka 대시보드에서 `order` 서비스가 등록되었는지 확인합니다.
</details>



--- 

## 기능 테스트

<details>
<summary>1. 로그인 API 테스트</summary>
1. 브라우저나 API 클라이언트를 사용하여 `http://localhost:19091/auth/signIn?user_id=test`에 GET 요청을 보냅니다.
2. JWT 토큰이 반환되는지 확인합니다.
</details>

<details>
<summary>2. 상품 목록 조회 API 테스트 (로드밸런싱 확인)</summary>
1. 브라우저나 API 클라이언트를 사용하여 `http://localhost:19091/products`에 GET 요청을 보냅니다.
2. 응답 헤더에 `Server-Port` 값이 `19093`과 `19094`로 번갈아 가며 설정되는지 확인합니다.
</details>

<details>
<summary>3. 주문에 상품 추가 API 테스트</summary>
1. 브라우저나 API 클라이언트를 사용하여 `http://localhost:19091/order`에 POST 요청을 보냅니다.
   - 요청 바디 예시:
     ```json
     {
       "name": "Test Order",
       "product_ids": [1, 2]
     }
     ```
2. 주문에 상품이 정상적으로 추가되는지 확인합니다.
3. 상품이 존재하지 않을 경우 주문에 저장되지 않는지 확인합니다.
</details>

---

### 기본 API 구성

   <details>
<summary>API 명세서 확인하기</summary>

### API 목록

1. **상품 추가 API**
   - **URL**: `POST /products`
   - **상품 Entity**
     | Key         | Value                          |
     |-------------|--------------------------------|
     | product_id  | Long (Primary, Auto Increment) |
     | name        | String                         |
     | supply_price| Integer                        |

2. **상품 목록 조회 API**
   - **URL**: `GET /products`
   - **응답 형태**: List<응답 객체>
   - **응답 객체**
     | Key         | Value  |
     |-------------|--------|
     | product_id  | Long   |
     | name        | String |
     | supply_price| Integer|

3. **주문 추가 API**
   - **URL**: `POST /order`
   - **주문 Entity**
     | Key        | Value                          |
     |------------|--------------------------------|
     | order_id   | Long (Primary, Auto Increment) |
     | name       | String                         |
     | product_ids| List<Long>                     |
   - **주문 매핑 상품 Entity**
     | Key        | Value                          |
     |------------|--------------------------------|
     | id         | Long (Primary, Auto Increment) |
     | product_id | Long                           |

4. **주문에 상품을 추가하는 API**
   - **URL**: `PUT /order/{orderId}`
   - **요청 Body**
     | Key        | Value |
     |------------|-------|
     | product_id | Long  |

5. **주문 단건 조회 API**
   - **URL**: `GET /order/{orderId}`
   - **응답 객체**
     | Key        | Value          |
     |------------|----------------|
     | order_id   | Long           |
     | product_ids| List<Integer>  |

6. **로그인 API**
   - **URL**: `GET /auth/signIn?user_id={string}`
</details>



@Todo :  (Gateway 서비스의 Filter 만 통과할 수 있도록 구성 / 추가구현 가능시 DB 연결 예정)

## 구현 상세 내용

<details>
<summary>상품 서비스는 라운드로빈 형식으로 로드밸런싱 구성</summary>
1. 라운드로빈 형식으로 로드밸런싱을 구현해서 상품 서비스가 호출될 때마다 두 서비스를 반복하며 호출되게 구성합니다.
2. 상품 목록을 조회할 때마다 API 의 응답 헤더의 `Server-Port` 값이 `19093` , `19094` 로 변경됩니다.
</details>

<details>
<summary>분산추적 구현</summary>
- `주문 서비스` 와 `상품 서비스` 에 Zipkin 을 연동하고, 호출시 Zipkin 대시보드에 `Duration` 이 측정됩니다. (대시보드로 확인 가능 -> @Todo : 대시보드 이미지 추가 예정)
</details>

<details>
<summary>캐싱 기능 구현</summary>
- 주문 조회 API 의 결과를 캐싱 처리하여 **60초 동안**은 DB 에서 불러오는 데이터가 아닌 **메모리에 캐싱된 데이터**가 보여지도록 설정합니다.
</details>

<details>
<summary>외부 요청 보호</summary>
- Oauth2,JWT 기반으로 인증/인가를 구성하여 인가 없이 `상품 서비스`, `주문 서비스`를 호출할 때 **401** HTTP **Status Code를 응답하도록 설정합니다**
</details>

<details>
<summary>Advanced - 캐시 활용</summary>
@ Todo : 내용정리 필요함 
1. [상품 추가 API](https://www.notion.so/0c299a44a1db4301bd26ad2f5004564b?pvs=21)  를 호출 할 경우 [상품 조회 API](https://www.notion.so/0c299a44a1db4301bd26ad2f5004564b?pvs=21) 의 응답 데이터 캐시가 갱신되도록 구현할 예정입니다. 
    (~~MSA~~ **인메모리 저장소 및 캐싱 강의** 중 **Spring Boot 프로젝트에 캐싱 적용하기** 를 참고해서 구현예정입니다)
2. 상품 추가 후 [상품 조회 API](https://www.notion.so/0c299a44a1db4301bd26ad2f5004564b?pvs=21) 호출 시 데이터가 변경 되는지 확인할 수 있습니다.
</details>

<details>
<summary>로컬과 서버의 환경을 분리</summary>
- 로컬에서는 [localhost:3306](http://localhost:3306) 으로 DB에 접근하고, 서버에 배포시엔 RDS 주소로 접근하게 구성하도록 환경을 분리합니다 (환경은 `dev`, `prod` 프로필로 나뉩니다).
</details>



---

### signin 처리 관련 

GET /auth/signIn?user_id={string} API가 DB 연결 없이 Gateway 서비스의 Filter를 통과하도록 구성하는 것이 목적이라면, Auth 서비스에서 간단히 JWT 토큰을 생성하여 반환하고, Gateway에서 이 토큰을 검증하는 방식으로 충분합니다. 이 경우 사용자 엔티티나 DB는 필요하지 않습니다.


---

# 진행 중 
