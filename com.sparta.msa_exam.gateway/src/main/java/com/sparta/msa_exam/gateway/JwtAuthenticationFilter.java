package com.sparta.msa_exam.gateway;//package com.sparta.msa_exam.gateway;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import javax.crypto.SecretKey;
//
//@Slf4j
//@Component
//public class JwtAuthenticationFilter implements GlobalFilter {
//
//    @Value("${service.jwt.secret-key}")
//    private String secretKey;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        log.info("JwtAuthenticationFilter invoked for path: {}", exchange.getRequest().getURI().getPath());
//
//        String path = exchange.getRequest().getURI().getPath();
//
//        if (path.equals("/auth/signIn")) {
//            return chain.filter(exchange);  // /signIn 경로는 필터를 적용하지 않음
//        }
//
//        String token = extractToken(exchange);
//
//        if (token == null || !validateToken(token, exchange)) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        return chain.filter(exchange);
//    }
//
//    private String extractToken(ServerWebExchange exchange) {
//        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            return authHeader.substring(7);
//        }
//        return null;
//    }
//
//
//    private boolean validateToken(String token, ServerWebExchange exchange) {
//        try {
//            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
//            Jws<Claims> claimsJws = Jwts.parser()
//                    .verifyWith(key)
//                    .build().parseSignedClaims(token);
//            log.info("#####payload :: " + claimsJws.getPayload().toString());
//            Claims claims = claimsJws.getBody();
//            exchange.getRequest().mutate()
//                    .header("X-User-Id", claims.get("user_id").toString())
//                    .header("X-Role", claims.get("role").toString())
//                    .build();
//            // 추가적인 검증 로직 (예: 토큰 만료 여부 확인 등)을 여기에 추가할 수 있습니다.
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//}
//
//
//
//
///*
//
//// 변경 전 코드
//validateToken 메서드 코드 수정 사유
//-
//SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
//Jws<Claims> claimsJws = Jwts.parser()
//        .verifyWith(key)
//        .build().parseSignedClaims(token);
//log.info("#####payload :: " + claimsJws.getPayload().toString());
//
//// 변경 후 코드
//SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
//Jws<Claims> claimsJws = Jwts.parserBuilder()
//        .setSigningKey(key)
//        .build()
//        .parseClaimsJws(token);
//Claims claims = claimsJws.getBody();
//log.info("#####payload :: " + claims.toString());
//
//<코드 변경의 이유>
//-----------------------------------------------------------------
//Jwts.parser() 대신 Jwts.parserBuilder()를 사용하는 이유는
//JWT 라이브러리의 최신 버전에서 권장되는 방식이기 때문입니다.
//parserBuilder()는 다양한 설정을 적용할 수 있으며, 더 유연하게 사용 가능합니다.
//------------------------------------------------------------------
//SecretKey 생성: JWT 서명/검증에 사용하는 SecretKey를 생성할 때
//Decoders.BASE64.decode(secretKey)를 사용하는 이유는
//secretKey가 Base64로 인코딩된 문자열이기 때문입니다.
//이를 디코딩하여 SecretKey 객체를 생성합니다.
// */

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter {

    @Value("${service.jwt.secret-key}")
    private String secretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // /auth/signIn 경로와 /products 경로는 필터를 적용하지 않음
        if (path.equals("/auth/signIn") || path.startsWith("/products")) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange);

        if (token == null || !validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(key)
                    .build().parseSignedClaims(token);
            log.info("#####payload :: " + claimsJws.getPayload().toString());

            return true;
        } catch (Exception e) {
            return false;
        }
    }


}

//Auth 빌드 되는 코드