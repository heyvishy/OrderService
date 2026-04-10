package com.cricketwireless.order_service.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateOrderRequest(
        @NotBlank(message = "customerName cannot be empty")
        String customerName,
        @NotBlank(message = "productName cannot be empty")
        String productName,
        @NotNull(message = "quantity should not be null")
        @Min(value = 1, message = "quantity must be at least 1")
        Long quantity,
        @NotNull(message = "price should not be null")
        @DecimalMin(value = "0.01", message = "price must be greater than 0")
        BigDecimal price
){}
