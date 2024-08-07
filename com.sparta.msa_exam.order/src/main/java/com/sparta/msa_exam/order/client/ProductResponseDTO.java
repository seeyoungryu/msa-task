package com.sparta.msa_exam.order.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDTO {
    private Long productId;
    private String name;
    private Integer supplyPrice;
}
