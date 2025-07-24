package ru.otus.order.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.order.service.model.OrderStatus;
import ru.otus.order.service.model.dto.OrderResponseDto;
import ru.otus.order.service.model.entity.Cart;
import ru.otus.order.service.model.entity.Order;
import ru.otus.order.service.model.entity.OrderItem;
import ru.otus.order.service.model.entity.OrderItemId;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    default Order map(Cart cart) {
        Order order = Order.builder()
                .userId(cart.getUserId())
                .status(OrderStatus.CREATED)
                .createdAt(OffsetDateTime.now())
                .items(new HashSet<>())
                .build();

        cart.getItems().values().forEach(i -> {
            OrderItem item = OrderItem.builder()
                    .id(new OrderItemId(null, i.getProductId()))
                    .name(i.getName())
                    .price(i.getPrice())
                    .quantity(i.getQuantity())
                    .order(order)
                    .build();

            order.getItems().add(item);
        });

        var totalPrice = order.getItems().stream()
                .map(item -> {
                    var quantity = item.getQuantity() != null ? item.getQuantity() : 1;
                    var price = item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO;
                    return price.multiply(new BigDecimal(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(totalPrice);
        return order;
    }

    OrderResponseDto map(Order order);

    @Mapping(target = "productId", source = "id.productId")
    OrderResponseDto.Item map(OrderItem orderItem);
}
