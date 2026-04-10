package com.cricketwireless.order_service.service.impl;

import com.cricketwireless.order_service.domain.OrderEntity;
import com.cricketwireless.order_service.dtos.CreateOrderRequest;
import com.cricketwireless.order_service.dtos.OrderResponse;
import com.cricketwireless.order_service.dtos.UpdateOrderRequest;
import com.cricketwireless.order_service.exception.ResourceNotFoundException;
import com.cricketwireless.order_service.mapper.OrderMapper;
import com.cricketwireless.order_service.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrder_shouldSaveEntityAndReturnResponse() {
        CreateOrderRequest request = new CreateOrderRequest("John Doe", "Phone", 2L, new BigDecimal("499.99"));
        OrderEntity entityToSave = orderEntity(1L, "John Doe", "Phone", 2L, new BigDecimal("499.99"));
        OrderResponse expectedResponse = orderResponse(1L, "John Doe", "Phone", 2L, new BigDecimal("499.99"));

        when(orderMapper.toOrderEntity(request)).thenReturn(entityToSave);
        when(orderRepository.save(entityToSave)).thenReturn(entityToSave);
        when(orderMapper.toOrderResponse(entityToSave)).thenReturn(expectedResponse);

        OrderResponse actual = orderService.createOrder(request);

        assertSame(expectedResponse, actual);
        verify(orderMapper).toOrderEntity(request);
        verify(orderRepository).save(entityToSave);
        verify(orderMapper).toOrderResponse(entityToSave);
    }

    @Test
    void updateOrder_shouldUpdateQuantityAndReturnResponse() {
        long orderId = 10L;
        UpdateOrderRequest request = new UpdateOrderRequest(7L);
        OrderEntity existing = orderEntity(orderId, "Jane", "SIM", 1L, new BigDecimal("49.99"));
        OrderResponse expectedResponse = orderResponse(orderId, "Jane", "SIM", 7L, new BigDecimal("49.99"));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existing));
        when(orderRepository.save(existing)).thenReturn(existing);
        when(orderMapper.toOrderResponse(existing)).thenReturn(expectedResponse);

        OrderResponse actual = orderService.updateOrder(orderId, request);

        assertSame(expectedResponse, actual);
        assertEquals(7L, existing.getQuantity());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(existing);
        verify(orderMapper).toOrderResponse(existing);
    }

    @Test
    void updateOrder_shouldThrowWhenOrderNotFound() {
        long orderId = 999L;
        UpdateOrderRequest request = new UpdateOrderRequest(3L);
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrder(orderId, request));
        verify(orderRepository).findById(orderId);
    }

    @Test
    void getOrders_shouldReturnMappedResponses() {
        OrderEntity order1 = orderEntity(1L, "A", "P1", 1L, new BigDecimal("1.00"));
        OrderEntity order2 = orderEntity(2L, "B", "P2", 2L, new BigDecimal("2.00"));
        OrderResponse response1 = orderResponse(1L, "A", "P1", 1L, new BigDecimal("1.00"));
        OrderResponse response2 = orderResponse(2L, "B", "P2", 2L, new BigDecimal("2.00"));

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));
        when(orderMapper.toOrderResponse(order1)).thenReturn(response1);
        when(orderMapper.toOrderResponse(order2)).thenReturn(response2);

        List<OrderResponse> result = orderService.getOrders();

        assertEquals(2, result.size());
        assertEquals(List.of(response1, response2), result);
        verify(orderRepository).findAll();
        verify(orderMapper).toOrderResponse(order1);
        verify(orderMapper).toOrderResponse(order2);
    }

    @Test
    void getOrderById_shouldReturnMappedResponseWhenFound() {
        long orderId = 11L;
        OrderEntity entity = orderEntity(orderId, "Alice", "Bundle", 1L, new BigDecimal("99.00"));
        OrderResponse expected = orderResponse(orderId, "Alice", "Bundle", 1L, new BigDecimal("99.00"));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(entity));
        when(orderMapper.toOrderResponse(entity)).thenReturn(expected);

        OrderResponse actual = orderService.getOrderById(orderId);

        assertSame(expected, actual);
        verify(orderRepository).findById(orderId);
        verify(orderMapper).toOrderResponse(entity);
    }

    @Test
    void getOrderById_shouldThrowWhenNotFound() {
        long orderId = 404L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(orderId));
        verify(orderRepository).findById(orderId);
    }

    @Test
    void deleteOrderById_shouldDeleteWhenFound() {
        long orderId = 15L;
        OrderEntity entity = orderEntity(orderId, "Bob", "Plan", 3L, new BigDecimal("19.99"));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(entity));

        orderService.deleteOrderById(orderId);

        verify(orderRepository).findById(orderId);
        verify(orderRepository).delete(entity);
    }

    @Test
    void deleteOrderById_shouldThrowWhenNotFound() {
        long orderId = 16L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrderById(orderId));
        verify(orderRepository).findById(orderId);
    }

    private static OrderEntity orderEntity(Long id, String customer, String product, Long quantity, BigDecimal price) {
        OrderEntity entity = new OrderEntity();
        entity.setId(id);
        entity.setCustomerName(customer);
        entity.setProductName(product);
        entity.setQuantity(quantity);
        entity.setPrice(price);
        return entity;
    }

    private static OrderResponse orderResponse(Long id, String customer, String product, Long quantity, BigDecimal price) {
        return new OrderResponse(id, customer, product, quantity, price);
    }
}
