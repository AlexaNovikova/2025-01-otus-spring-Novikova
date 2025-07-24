package ru.otus.inventory.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.lib.kafka.model.ReservationProcessModel;
import ru.otus.lib.kafka.service.BusinessTopics;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryKafkaConsumerService {

    private final InventoryService inventoryService;

    @KafkaListener(topics = BusinessTopics.ORDER_RESERVATION_PROCESS, groupId = "${spring.kafka.consumer.group-id}")
    public void listenOrderReservationProcess(ReservationProcessModel model) {
        log.debug("Received new reservation: {}", model);
        try {
            inventoryService.processReservation(model);
        } catch (Exception exception) {
            log.error("Error while processing order reservation process", exception);
            throw exception;
        }
    }
}
