package com.sparta.msa_exam.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderResponseDto {
    private Long orderId;
    private String name;
    private List<Long> productIds;
}
