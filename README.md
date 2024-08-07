# MSA 구성, Redis 캐싱, Docker 기반 CI/CD 프로젝트 구현 레파지토리

## 전체 흐름 

![Untitled](https://github.com/user-attachments/assets/8d008528-893a-40d8-aa29-843487d92d7d)


## **기본 API 구성**

<details><summary> API 명세서 확인하기</summary>
  - API 목록
        1. `POST /products`  상품 추가 API 
        **상품 Entity**
            
            
            | Key | Value |
            | --- | --- |
            | product_id | Long (Primary, Auto Increment) |
            | name | String |
            | supply_price | Integer |
        2. `GET /products` 상품 목록 조회 API
            
            **응답 형태: List<응답 객체>**
            
            **응답 객체**
            
            | Key | Value |
            | --- | --- |
            | product_id | Long |
            | name | String |
            | supply_price | Integer |
        3. `POST /order` 주문 추가 API
            
            **주문 Entity**
            
            | Key | Value |
            | --- | --- |
            | order_id | Long (Primary, Auto Increment) |
            | name | String |
            | product_ids | List<https://www.notion.so/0c299a44a1db4301bd26ad2f5004564b?pvs=21> |
            
            **주문 매핑 상품 Entity**
            
            | Key | Value |
            | --- | --- |
            | id | Long (Primary, Auto Increment) |
            | product_id | Long |
        4. `PUT /order/{orderId}`  주문에 상품을 추가하는 API
            
            **요청 Body**
            
            | Key | Value |
            | --- | --- |
            | product_id | Long |
        5. `GET /order/{orderId}`  주문 단건 조회 API
            
            **응답 객체**
            
            | Key | Value |
            | --- | --- |
            | order_id | Long |
            | product_ids | List<Integer> |
        6. `GET /auth/signIn?user_id={string}`  로그인 API 
            

</details>
            
  
@Todo :  (Gateway 서비스의 Filter 만 통과할 수 있도록 구성 / 추가구현 가능시 DB 연결 예정)

---
## 구현 상세 내용

- [ ]  **상품 서비스는 라운드로빈 형식으로 로드밸런싱 구성**
    1. 라운드로빈 형식으로 로드밸런싱을 구현해서 상품 서비스가 호출될 때마다 두 서비스를 반복하며 호출되게 구성
    2. 상품 목록을 조회할 때마다 API 의 응답 헤더의 `Server-Port` 값이 `19093` , `19094` 로 변경됨
- [ ]  **분산추적 구현**
    - `주문 서비스` 와 `상품 서비스` 에 Zipkin 을 연동하고, 호출시 Zipkin 대시보드에 `Duration` 이 측정됨 (대시보드로 확인 가능 -> @Todo : 대시보드 이미지 추가 예정)

     
- [ ]  **캐싱 기능 구현**
    - 주문 조회 API 의 결과를 캐싱 처리하여 **60초 동안**은 DB 에서 불러오는 데이터가 아닌 **메모리에 캐싱된 데이터**가 보여지도록 설정
- [ ]  **외부 요청 보호**
    - Oauth2,JWT 기반으로 인증/인가를 구성하여 인가 없이 `상품 서비스`, `주문 서비스`를 호출할 때 **401** HTTP ****Status Code를 응답하도록 설정

      
- [ ]  **Advanced - 캐시 활용**

@ Todo : 내용정리 필요함 
    1. [상품 추가 API](https://www.notion.so/0c299a44a1db4301bd26ad2f5004564b?pvs=21)  를 호출 할 경우 [상품 조회 API](https://www.notion.so/0c299a44a1db4301bd26ad2f5004564b?pvs=21) 의 응답 데이터 캐시가 갱신되도록 구현할 예정 
    (~~MSA~~ **인메모리 저장소 및 캐싱 강의** 중 **Spring Boot 프로젝트에 캐싱 적용하기** 를 참고해서 구현예정)
    2. 상품 추가 후 [상품 조회 API](https://www.notion.so/0c299a44a1db4301bd26ad2f5004564b?pvs=21) 호출 시 데이터가 변경 되는지 확인할 수 있음
    
- [ ]  **로컬과 서버의 환경을 분리**
    - 로컬에서는 [localhost:3306](http://localhost:3306) 으로 DB에 접근하고, 서버에 배포시엔 RDS 주소로 접근하게 구성하도록 환경을 분리 (환경은 `dev`, `prod` 프로필로 나뉨)


---


#### GET /auth/signIn?user_id={string} API가 DB 연결 없이 Gateway 서비스의 Filter를 통과하도록 구성하는 것이 목적이라면, Auth 서비스에서 간단히 JWT 토큰을 생성하여 반환하고, Gateway에서 이 토큰을 검증하는 방식으로 충분합니다. 이 경우 사용자 엔티티나 DB는 필요하지 않습니다.
