package com.cricketwireless.order_service.mapper;

import com.cricketwireless.order_service.domain.OrderEntity;
import com.cricketwireless.order_service.dtos.CreateOrderRequest;
import com.cricketwireless.order_service.dtos.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    /**
     * Maps an OrderEntity to an OrderResponse record.
     * @param orderEntity the source entity
     * @return the mapped DTO, or null if source is null
     */
    public OrderResponse toOrderResponse(final OrderEntity orderEntity) {
        if (orderEntity == null) {
            return null;
        }

        return new OrderResponse(
                orderEntity.getId(),
                orderEntity.getCustomerName(),
                orderEntity.getProductName(),
                orderEntity.getQuantity(),
                orderEntity.getPrice()
        );
    }


    /**
     * Maps an CreateOrderRequest record to an OrderEntity.
     * @param orderRequest
     * @return
     */
    public OrderEntity toOrderEntity(final CreateOrderRequest orderRequest) {
        if (orderRequest == null) {
            return null;
        }

        OrderEntity orderEntity = new OrderEntity();
        // Mapping fields from Record to Entity
        orderEntity.setCustomerName(orderRequest.customerName());
        orderEntity.setProductName(orderRequest.productName());
        orderEntity.setQuantity(orderRequest.quantity());
        orderEntity.setPrice(orderRequest.price());

        return orderEntity;
    }
}
