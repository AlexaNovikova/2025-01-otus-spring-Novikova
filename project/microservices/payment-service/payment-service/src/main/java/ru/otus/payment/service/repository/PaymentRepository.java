package ru.otus.payment.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.payment.service.model.entity.Payment;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByOrderIdAndAccountUserId(Long orderId, Long accountId);
}
