package ru.otus.order.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.order.service.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private Integer id;

    private Long userId;

    private OrderStatus status;

    private BigDecimal totalPrice;

    private List<Item> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private Long productId;
        private String name;
        private BigDecimal price;
        private Integer quantity;
    }
}
