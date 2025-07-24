package ru.otus.payment.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.lib.kafka.model.CreateAccountModel;
import ru.otus.lib.kafka.model.PaymentProcessModel;
import ru.otus.lib.kafka.service.BusinessTopics;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentKafkaConsumerService {

    private final AccountService accountService;

    @KafkaListener(topics = BusinessTopics.ORDER_PAYMENT_PROCESS, groupId = "${spring.kafka.consumer.group-id}")
    public void listenOrderPaymentProcess(PaymentProcessModel model) {
        log.debug("Received new payment process: {}", model);
        try {
            accountService.handlePaymentProcess(model);
        } catch (Exception exception) {
            log.error("Error while processing order payment process", exception);
            throw exception;
        }
    }

    @KafkaListener(topics = BusinessTopics.PAYMENT_NEW_ACCOUNT, groupId = "${spring.kafka.consumer.group-id}")
    public void listenNewAccountCreate(CreateAccountModel model) {
        log.debug("Received new account creation: {}", model);
        try {
            accountService.handleCreateAccount(model);
        } catch (Exception exception) {
            log.error("Error while creating new account", exception);
            throw exception;
        }
    }
}
