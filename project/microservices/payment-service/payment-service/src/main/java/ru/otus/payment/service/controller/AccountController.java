package ru.otus.payment.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.common.ShopUser;
import ru.otus.lib.context.UserContext;
import ru.otus.payment.service.model.dto.AccountResponseDto;
import ru.otus.payment.service.model.dto.PutMoneyRequestDto;
import ru.otus.payment.service.service.AccountService;

@Slf4j
@Validated
@RestController
@RequestMapping(AccountController.BASE_PATH)
@RequiredArgsConstructor
public class AccountController {

    public final static String BASE_PATH = "/api/v1/account";

    private final AccountService service;

    @GetMapping
    public AccountResponseDto get(@UserContext ShopUser shopUser) {
        var userId = shopUser.getId();
        log.debug("Trying to check account info by user with id: {}", userId);
        return service.get(userId);
    }

    @PostMapping("/put-money")
    public AccountResponseDto putMoney(@UserContext ShopUser shopUser,
                                       @RequestBody PutMoneyRequestDto dto) {
        var userId = shopUser.getId();
        log.debug("Trying to put money on account by user with id: {}", userId);
        return service.putMoney(userId, dto);
    }
}
