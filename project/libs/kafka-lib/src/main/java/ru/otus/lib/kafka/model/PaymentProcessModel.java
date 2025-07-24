package ru.otus.lib.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProcessModel {

    private Long orderId;

    private Long userId;

    private BigDecimal amount;

    public static PaymentProcessModel start(Long orderId, Long userId, BigDecimal amount) {
        return new PaymentProcessModel(orderId, userId, amount);
    }
}
