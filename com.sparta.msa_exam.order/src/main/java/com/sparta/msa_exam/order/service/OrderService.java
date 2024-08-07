package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.client.ProductClient;
import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductClient productClient;
    private final OrderRepository orderRepository;

    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = Order.builder()
                .name(orderRequestDto.getName())
                .productIds(orderRequestDto.getProductIds())
                .build();
        Order savedOrder = orderRepository.save(order);
        return OrderResponseDto.builder()
                .orderId(savedOrder.getOrderId())
                .name(savedOrder.getName())
                .productIds(savedOrder.getProductIds())
                .build();
    }

    public Optional<OrderResponseDto> getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> OrderResponseDto.builder()
                        .orderId(order.getOrderId())
                        .name(order.getName())
                        .productIds(order.getProductIds())
                        .build());
    }

    public OrderResponseDto updateOrder(Long orderId, Long productId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
        order.getProductIds().add(productId);
        Order updatedOrder = orderRepository.save(order);
        return OrderResponseDto.builder()
                .orderId(updatedOrder.getOrderId())
                .name(updatedOrder.getName())
                .productIds(updatedOrder.getProductIds())
                .build();
    }
}
