package ru.otus.lib.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationProcessModel {

    private Long orderId;

    private Map<Long, Integer> productQuantityMap;

    public static ReservationProcessModel startReservation(Long orderId,
                                                           Map<Long, Integer> productsQuantityMap) {
        return ReservationProcessModel.builder()
                .orderId(orderId)
                .productQuantityMap(productsQuantityMap)
                .build();
    }

}
