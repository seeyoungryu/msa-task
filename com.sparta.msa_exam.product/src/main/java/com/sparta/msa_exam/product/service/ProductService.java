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


/*

스트림 사용 과정
findAll(): ProductRepository의 findAll() 메서드를 호출하여 모든 Product 객체의 리스트를 가져옵니다.
stream(): 리스트를 스트림으로 변환합니다. 스트림을 사용하면 각 요소를 순차적으로 처리할 수 있습니다.
map(): 스트림의 각 Product 객체를 ProductResponseDTO 객체로 변환합니다. map() 메서드는 스트림의 각 요소에 대해 주어진 변환 함수를 적용하여 새로운 스트림을 반환합니다.
collect(Collectors.toList()): 변환된 스트림을 다시 리스트로 수집합니다. 최종적으로 List<ProductResponseDTO> 타입의 결과를 얻습니다.

 */