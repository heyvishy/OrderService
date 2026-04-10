package com.cricketwireless.order_service.dtos;

import java.time.LocalDateTime;

public record ErrorResponse(
        String errorCode,
        String message,
        String path,
        LocalDateTime timestamp
) {
}
