package ru.otus.order.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {

    private Long userId;

    private BigDecimal totalPrice;

    private List<ItemResponseDto> items;
}
