package com.cricketwireless.order_service.controller;

import com.cricketwireless.order_service.dtos.OrderResponse;
import com.cricketwireless.order_service.exception.GlobalExceptionHandler;
import com.cricketwireless.order_service.exception.ResourceNotFoundException;
import com.cricketwireless.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerIntegrationTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        StandaloneMockMvcBuilder builder = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler());
        mockMvc = builder.build();
    }

    @Test
    void createOrder_shouldReturnOkWithResponseBody() throws Exception {
        when(orderService.createOrder(any())).thenReturn(
                new OrderResponse(1L, "John", "Phone", 2L, new BigDecimal("499.99"))
        );

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName":"John",
                                  "productName":"Phone",
                                  "quantity":2,
                                  "price":499.99
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.customerName").value("John"))
                .andExpect(jsonPath("$.productName").value("Phone"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.price").value(499.99));
    }

    @Test
    void createOrder_shouldReturnBadRequestWhenValidationFails() throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName":"",
                                  "productName":"Phone",
                                  "quantity":0,
                                  "price":0.00
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.path").value("/api/v1/orders"));
    }

    @Test
    void updateOrder_shouldReturnNotFoundWhenOrderMissing() throws Exception {
        when(orderService.updateOrder(any(Long.class), any()))
                .thenThrow(new ResourceNotFoundException("Order not found for id: 99"));

        mockMvc.perform(put("/api/v1/orders/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "quantity":5
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ORDER_NOT_FOUND"))
                .andExpect(jsonPath("$.message", containsString("Order not found for id: 99")));
    }

    @Test
    void getOrders_shouldReturnList() throws Exception {
        when(orderService.getOrders()).thenReturn(List.of(
                new OrderResponse(1L, "A", "P1", 1L, new BigDecimal("1.00")),
                new OrderResponse(2L, "B", "P2", 2L, new BigDecimal("2.00"))
        ));

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1))
                .andExpect(jsonPath("$[1].orderId").value(2));
    }

    @Test
    void getOrderById_shouldReturnOrder() throws Exception {
        when(orderService.getOrderById(10L)).thenReturn(
                new OrderResponse(10L, "Jane", "SIM", 3L, new BigDecimal("19.99"))
        );

        mockMvc.perform(get("/api/v1/orders/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(10))
                .andExpect(jsonPath("$.customerName").value("Jane"));
    }

    @Test
    void deleteOrder_shouldReturnNoContent() throws Exception {
        doNothing().when(orderService).deleteOrderById(11L);
        mockMvc.perform(delete("/api/v1/orders/{id}", 11L))
                .andExpect(status().isNoContent());
    }
}
