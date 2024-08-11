package com.sparta.msa_exam.product.controller;

import com.sparta.msa_exam.product.dto.ProductRequestDTO;
import com.sparta.msa_exam.product.dto.ProductResponseDTO;
import com.sparta.msa_exam.product.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Value("${server.port}") // 애플리케이션이 실행 중인 포트를 주입받음
    private String serverPort;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO createdProduct = productService.createProduct(productRequestDTO);
        return ResponseEntity.ok(createdProduct);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getProducts(HttpServletResponse response) {
        // 응답 헤더에 서버 포트 번호를 설정
        response.setHeader("Server-Port", serverPort);
        List<ProductResponseDTO> products = productService.getProducts();
        return ResponseEntity.ok(products);
    }
}
