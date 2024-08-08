package com.sparta.msa_exam.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductResponseDTO {
    private Long productId;
    private String name;
    private Integer supplyPrice;
}
