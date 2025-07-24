package ru.otus.payment.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.payment.service.model.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
