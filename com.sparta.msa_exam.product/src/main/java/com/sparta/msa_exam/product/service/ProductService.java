package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.ProductRequestDTO;
import com.sparta.msa_exam.product.dto.ProductResponseDTO;
import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;


    // 빌더 패턴을 사용한 createProduct 메소드
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDto) {
        Product product = Product.builder()
                .name(productRequestDto.getName())
                .supplyPrice(productRequestDto.getSupplyPrice())
                .build();

        Product savedProduct = productRepository.save(product);

        return ProductResponseDTO.builder()
                .productId(savedProduct.getProductId())
                .name(savedProduct.getName())
                .supplyPrice(savedProduct.getSupplyPrice())
                .build();
    }


    // 빌더 패턴을 사용한 getProducts 메소드
    public List<ProductResponseDTO> getProducts() {
        return productRepository.findAll().stream()
                .map(product -> ProductResponseDTO.builder()
                        .productId(product.getProductId())
                        .name(product.getName())
                        .supplyPrice(product.getSupplyPrice())
                        .build())
                .collect(Collectors.toList());
    }
}


//    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
//        Product product = new Product();
//        product.setName(productRequestDTO.getName());
//        product.setSupplyPrice(productRequestDTO.getSupplyPrice());
//        Product savedProduct = productRepository.save(product);
//
//        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
//        productResponseDTO.setProductId(savedProduct.getProductId());
//        productResponseDTO.setName(savedProduct.getName());
//        productResponseDTO.setSupplyPrice(savedProduct.getSupplyPrice());
//        return productResponseDTO;
//    }


