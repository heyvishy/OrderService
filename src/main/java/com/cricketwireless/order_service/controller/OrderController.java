package com.cricketwireless.order_service.controller;

import com.cricketwireless.order_service.dtos.CreateOrderRequest;
import com.cricketwireless.order_service.dtos.OrderResponse;
import com.cricketwireless.order_service.dtos.UpdateOrderRequest;
import com.cricketwireless.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody final CreateOrderRequest createOrderRequest) {
        log.info("Creating order for customer={}", createOrderRequest.customerName());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(createOrderRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable long id, @Valid @RequestBody final UpdateOrderRequest updateOrderRequest) {
        log.info("Updating order id={} with quantity={}", id, updateOrderRequest.quantity());
        return ResponseEntity.ok().body(orderService.updateOrder(id, updateOrderRequest));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(){
        return ResponseEntity.ok(orderService.getOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable long id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id){
        orderService.deleteOrderById(id);
    }

}
