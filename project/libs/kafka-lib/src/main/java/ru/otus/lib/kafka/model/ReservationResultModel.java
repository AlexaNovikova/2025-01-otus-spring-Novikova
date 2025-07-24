package ru.otus.lib.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResultModel {

    private Long orderId;

    private ProcessStatus status;

    private String info;

    public static ReservationResultModel success(Long orderId) {
        var reservationResultModel = new ReservationResultModel();
        reservationResultModel.setOrderId(orderId);
        reservationResultModel.setStatus(ProcessStatus.SUCCESS);
        return reservationResultModel;
    }

    public static ReservationResultModel error(Long orderId, String info) {
        var reservationResultModel = new ReservationResultModel();
        reservationResultModel.setOrderId(orderId);
        reservationResultModel.setInfo(info);
        reservationResultModel.setStatus(ProcessStatus.ERROR);
        return reservationResultModel;
    }
}
