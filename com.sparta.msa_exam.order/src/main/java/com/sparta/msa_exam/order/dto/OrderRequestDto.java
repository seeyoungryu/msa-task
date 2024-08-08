package com.sparta.msa_exam.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// @Builder
@Getter
@Setter
public class OrderRequestDto {
    private String name;
    private List<Long> productIds;
}
