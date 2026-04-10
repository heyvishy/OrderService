package com.cricketwireless.order_service.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderRequest(
        @NotNull(message = "quantity should not be null")
        @Min(value = 1, message = "quantity must be at least 1")
        Long quantity) {}
