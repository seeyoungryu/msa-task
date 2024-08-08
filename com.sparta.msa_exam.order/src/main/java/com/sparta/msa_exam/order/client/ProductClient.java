package com.sparta.msa_exam.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping("/products/{id}")
    ProductResponseDTO getProduct(@PathVariable("id") Long id);
}


/*
     - FeignClient 설정은 OrderService에서 다른 서비스(ProductService)의 API를 호출하기 위해 사용됨.
       -> OrderService는 ProductService의 API를 호출하여 상품이 유효한지 확인하고, 유효한 경우에만 주문을 업데이트함
       -> 따라서 OrderService에서 ProductService의 상품 정보를 조회할 수 있도록 FeignClient를 설정
*/
