package com.sparta.msa_exam.product.dto;

import lombok.Getter;
import lombok.Setter;

// @Builder
@Getter
@Setter
public class ProductRequestDTO {
    private String name;
    private Integer supplyPrice;
}
