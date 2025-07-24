package ru.otus.lib.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationTypeModel {

    private Long orderId;

    private ReleaseType type;

    public static ReservationTypeModel initCollecting(Long orderId) {
        return ReservationTypeModel.builder()
                .orderId(orderId)
                .type(ReleaseType.COLLECTING)
                .build();
    }

    public static ReservationTypeModel initRelease(Long orderId) {
        return ReservationTypeModel.builder()
                .orderId(orderId)
                .type(ReleaseType.RELEASE)
                .build();
    }
}
