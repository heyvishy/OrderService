package com.cricketwireless.order_service.dtos;

import java.math.BigDecimal;

public record OrderResponse(
    Long orderId,
    String customerName,
    String productName,
    Long quantity,
    BigDecimal price
) {}
