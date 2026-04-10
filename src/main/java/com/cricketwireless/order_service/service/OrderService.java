package com.cricketwireless.order_service.service;

import com.cricketwireless.order_service.dtos.CreateOrderRequest;
import com.cricketwireless.order_service.dtos.OrderResponse;
import com.cricketwireless.order_service.dtos.UpdateOrderRequest;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(final CreateOrderRequest createOrderRequest);

    OrderResponse updateOrder(long id, final UpdateOrderRequest updateOrderRequest);

    OrderResponse patchOrder(Long id, UpdateOrderRequest request);

    List<OrderResponse> getOrders();

    OrderResponse getOrderById(long id);

    void deleteOrderById(long id);

}
