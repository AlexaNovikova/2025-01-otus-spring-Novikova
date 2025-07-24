package ru.otus.lib.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationModel {

    private Long orderId;

    private String email;

    private NotificationType type;

    private String topic;

    private String message;

    public static SendNotificationModel orderCreated(Long orderId, String email) {
        return createMessage(orderId, email, NotificationType.ORDER_CREATED);
    }

    public static SendNotificationModel orderCancelledByProductsNotAvailable(Long orderId, String email) {
        return createMessage(orderId, email, NotificationType.PRODUCTS_NOT_AVAILABLE);
    }

    public static SendNotificationModel orderCancelledByPaymentError(Long orderId, String email) {
        return createMessage(orderId, email, NotificationType.PAYMENT_FAILED);
    }

    public static SendNotificationModel orderIsCollecting(Long orderId, String email) {
        return createMessage(orderId, email, NotificationType.ORDER_IS_COLLECTING);
    }

    public static SendNotificationModel orderIsReady(Long orderId, String email) {
        return createMessage(orderId, email, NotificationType.ORDER_IS_READY);
    }

    public static SendNotificationModel orderIsDelivered(Long orderId, String email) {
        return createMessage(orderId, email, NotificationType.ORDER_IS_DELIVERED);
    }

    private static SendNotificationModel createMessage(Long orderId,
                                                       String email,
                                                       NotificationType type) {
        var topic = type.getTopic();
        var message = type.getMessage().formatted(orderId);
        return SendNotificationModel.builder()
                .email(email)
                .type(type)
                .topic(topic)
                .message(message)
                .build();
    }
}
