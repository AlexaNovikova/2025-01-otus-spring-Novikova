package ru.otus.lib.kafka.model;

import lombok.Getter;

@Getter
public enum NotificationType {

    ORDER_CREATED("Ваш заказ создан",
            "Здравствуйте! Спасибо за заказ!" +
                    " Ваш заказ № %d принят в обработку."),
    PRODUCTS_NOT_AVAILABLE("Ваш заказ отменен",
            "К сожалению, некоторые продукты закончились на складе. " +
                    "Выберите замены, " +
                    "либо удалите отсутствующие продукты из корзины" +
                    " и попробуйте оформить заказ еще раз! Спасибо за понимание!"),
    PAYMENT_FAILED("Оплата не прошла",
            "К сожалению, оплата не прошла, " +
                    "возможно недостаточно средств на счете." +
                    " Пополните счет и попробуйте оформить заказ еще раз."),
    ORDER_IS_COLLECTING("Заказ принят",
            "Ваш заказ № %d принят! Мы уже собираем его!"),
    ORDER_IS_READY("Заказ собран", "Ваш заказ № %d готов! Ожидайте курьера!"),
    ORDER_IS_DELIVERED("Заказ доставлен", "Ваш заказ № %d получен.");

    private final String topic;

    private final String message;

    NotificationType(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }

}
