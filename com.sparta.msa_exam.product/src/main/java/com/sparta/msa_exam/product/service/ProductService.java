package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.ProductRequestDTO;
import com.sparta.msa_exam.product.dto.ProductResponseDTO;
import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setSupplyPrice(productRequestDTO.getSupplyPrice());
        Product savedProduct = productRepository.save(product);

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setProductId(savedProduct.getProductId());
        productResponseDTO.setName(savedProduct.getName());
        productResponseDTO.setSupplyPrice(savedProduct.getSupplyPrice());
        return productResponseDTO;
    }

    public List<ProductResponseDTO> getProducts() {
        return productRepository.findAll().stream().map(product -> {
            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
            productResponseDTO.setProductId(product.getProductId());
            productResponseDTO.setName(product.getName());
            productResponseDTO.setSupplyPrice(product.getSupplyPrice());
            return productResponseDTO;
        }).collect(Collectors.toList());
    }
}
