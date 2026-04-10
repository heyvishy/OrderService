package com.cricketwireless.order_service.service.impl;

import com.cricketwireless.order_service.domain.OrderEntity;
import com.cricketwireless.order_service.dtos.CreateOrderRequest;
import com.cricketwireless.order_service.dtos.OrderResponse;
import com.cricketwireless.order_service.dtos.UpdateOrderRequest;
import com.cricketwireless.order_service.exception.ResourceNotFoundException;
import com.cricketwireless.order_service.mapper.OrderMapper;
import com.cricketwireless.order_service.repository.OrderRepository;
import com.cricketwireless.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse createOrder(final CreateOrderRequest orderRequest) {
        final OrderEntity orderEntity = orderMapper.toOrderEntity(orderRequest);
        return orderMapper.toOrderResponse(orderRepository.save(orderEntity));
    }

    @Override
    public OrderResponse updateOrder(long id, final UpdateOrderRequest updateOrderRequest) {
        OrderEntity updatedEntity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for id: " + id));
        updatedEntity.setQuantity(updateOrderRequest.quantity());
        return orderMapper.toOrderResponse(orderRepository.save(updatedEntity));
    }

    @Override
    public OrderResponse patchOrder(Long id, UpdateOrderRequest request) {
        OrderEntity updatedEntity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));

        if (request.quantity() != null) {
            updatedEntity.setQuantity(request.quantity());
        }
        return orderMapper.toOrderResponse(orderRepository.save(updatedEntity));
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderResponse> getOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponse getOrderById(long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toOrderResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for id: " + id));
    }

    @Override
    public void deleteOrderById(long id) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for id: " + id));
        orderRepository.delete(entity);
    }

}
