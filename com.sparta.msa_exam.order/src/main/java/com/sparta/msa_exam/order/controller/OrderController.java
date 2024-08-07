package com.sparta.msa_exam.order.controller;


import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto createdOrder = orderService.createOrder(orderRequestDto);
        return ResponseEntity.ok(createdOrder);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable Long orderId, @RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto updatedOrder = orderService.updateOrder(orderId, orderRequestDto.getProductIds().get(0));
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        Optional<OrderResponseDto> order = orderService.getOrder(orderId);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
