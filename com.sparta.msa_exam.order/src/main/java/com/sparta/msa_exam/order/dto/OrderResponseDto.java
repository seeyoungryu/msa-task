package com.sparta.msa_exam.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponseDto {
    private Long orderId;
    private String name;
    private List<Long> productIds;
}
