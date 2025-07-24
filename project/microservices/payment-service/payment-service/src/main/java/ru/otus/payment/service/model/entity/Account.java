package ru.otus.payment.service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "account", schema = "payment")
public class Account {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @CreationTimestamp
    private OffsetDateTime createdAt = OffsetDateTime.now();
}