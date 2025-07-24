package ru.otus.payment.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.common.error.ShopException;
import ru.otus.lib.kafka.model.CreateAccountModel;
import ru.otus.lib.kafka.model.PaymentResultModel;
import ru.otus.lib.kafka.model.PaymentProcessModel;
import ru.otus.lib.kafka.service.BusinessTopics;
import ru.otus.lib.kafka.service.KafkaProducerService;
import ru.otus.payment.service.mapper.AccountMapper;
import ru.otus.payment.service.model.dto.AccountResponseDto;
import ru.otus.payment.service.model.dto.PutMoneyRequestDto;
import ru.otus.payment.service.model.entity.Payment;
import ru.otus.payment.service.repository.AccountRepository;
import ru.otus.payment.service.repository.PaymentRepository;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    private final PaymentRepository paymentRepository;

    private final AccountMapper mapper;

    private final KafkaProducerService kafkaProducerService;

    public AccountResponseDto get(Long userId) {
        var account = repository.findById(userId);
        if (account.isEmpty()) {
            getErrorLog(userId);
            throw new ShopException("account.not.found", "Счет не найден");
        }
        return mapper.map(account.get());
    }

    @Transactional
    public AccountResponseDto putMoney(Long userId, PutMoneyRequestDto dto) {
        var accountOpt = repository.findById(userId);
        if (accountOpt.isEmpty()) {
            getErrorLog(userId);
            throw new ShopException("account.not.found", "Счет не найден");
        }

        var account = accountOpt.get();
        account.setAmount(account.getAmount().add(dto.getAmount()));
        var updatedAccount = repository.save(account);
        return mapper.map(updatedAccount);
    }

    private static void getErrorLog(Long userId) {
        log.error("Account for the user with id {} not found", userId);
    }

    @Transactional
    public void handlePaymentProcess(PaymentProcessModel model) {
        var userId = model.getUserId();
        var orderId = model.getOrderId();
        var accountOpt = repository.findById(userId);
        if (accountOpt.isEmpty()) {
            getErrorLog(userId);
            var errorModel = PaymentResultModel.error(orderId, "Account not found");
            kafkaProducerService.send(BusinessTopics.ORDER_PAYMENT_CONFIRMATION, errorModel);
            return;
        }
        var paymentAlreadyPassed = paymentRepository
                .existsByOrderIdAndAccountUserId(orderId, accountOpt.get().getUserId());
        if (paymentAlreadyPassed) {
            log.warn("Payment already processed. Skip it");
            return;
        }

        var account = accountOpt.get();
        var currentUserMoneyAmount = account.getAmount();
        var amountAfterPayment = currentUserMoneyAmount.subtract(model.getAmount());

        if (amountAfterPayment.compareTo(BigDecimal.ZERO) < 0) {
            log.error("Not enough money on account for user with id: {}" +
                    " to pay for the order with id: {}", userId, orderId);
            var errorModel = PaymentResultModel.error(orderId, "Not enough money");
            kafkaProducerService.send(BusinessTopics.ORDER_PAYMENT_CONFIRMATION, errorModel);
        } else {
            log.debug("Current amount is enough to pay for the order");
            account.setAmount(amountAfterPayment);

            var payment = new Payment();
            payment.setAccount(account);
            payment.setOrderId(orderId);
            payment.setAmount(model.getAmount());
            repository.save(account);
            paymentRepository.save(payment);

            var successModel = PaymentResultModel.success(orderId);
            kafkaProducerService.send(BusinessTopics.ORDER_PAYMENT_CONFIRMATION, successModel);
        }
    }

    @Transactional
    public void handleCreateAccount(CreateAccountModel model) {
        var userId = model.getUserId();
        log.debug("Trying to create new account for user with id: {}", userId);
        var accountOpt = repository.findById(userId);
        if (accountOpt.isPresent()) {
            log.warn("Account for the user with id {} already exists", userId);
            return;
        }

        log.debug("Account for user with id {} not found. Creating it...", userId);
        var account = mapper.map(userId);
        repository.save(account);
    }
}
