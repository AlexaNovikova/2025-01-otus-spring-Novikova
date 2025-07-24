package ru.otus.lib.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResultModel {

    private Long orderId;

    private ProcessStatus status;

    private String reasonError;

    public static PaymentResultModel success(Long orderId) {
        var paymentConfirmationModel =
                new PaymentResultModel();
        paymentConfirmationModel.setOrderId(orderId);
        paymentConfirmationModel.setStatus(ProcessStatus.SUCCESS);
        return paymentConfirmationModel;

    }

    public static PaymentResultModel error(Long orderId, String reasonError) {
        var paymentConfirmationModel =
                new PaymentResultModel();
        paymentConfirmationModel.setOrderId(orderId);
        paymentConfirmationModel.setStatus(ProcessStatus.ERROR);
        paymentConfirmationModel.setReasonError(reasonError);
        return paymentConfirmationModel;
    }
}
