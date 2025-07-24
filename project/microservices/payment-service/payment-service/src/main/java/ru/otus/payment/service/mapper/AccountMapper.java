package ru.otus.payment.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.payment.service.model.dto.AccountResponseDto;
import ru.otus.payment.service.model.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponseDto map(Account account);

    @Mapping(target = "createdAt", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "amount", expression = "java(java.math.BigDecimal.ZERO)")
    Account map(Long userId);
}
