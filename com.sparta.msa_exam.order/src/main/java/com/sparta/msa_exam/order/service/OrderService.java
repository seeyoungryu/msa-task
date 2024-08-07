package com.sparta.msa_exam.order;

import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponseDto addOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setName(orderRequestDto.getName());
        order.setProductIds(orderRequestDto.getProductIds());
        Order savedOrder = orderRepository.save(order);

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(savedOrder.getOrderId());
        orderResponseDto.setName(savedOrder.getName());
        orderResponseDto.setProductIds(savedOrder.getProductIds());
        return orderResponseDto;
    }

    public Optional<OrderResponseDto> getOrder(Long orderId) {
        return orderRepository.findById(orderId).map(order -> {
            OrderResponseDto orderResponseDto = new OrderResponseDto();
            orderResponseDto.setOrderId(order.getOrderId());
            orderResponseDto.setName(order.getName());
            orderResponseDto.setProductIds(order.getProductIds());
            return orderResponseDto;
        });
    }

    public OrderResponseDto updateOrder(Long orderId, Long productId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
        order.getProductIds().add(productId);
        Order updatedOrder = orderRepository.save(order);

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(updatedOrder.getOrderId());
        orderResponseDto.setName(updatedOrder.getName());
        orderResponseDto.setProductIds(updatedOrder.getProductIds());
        return orderResponseDto;
    }
}
