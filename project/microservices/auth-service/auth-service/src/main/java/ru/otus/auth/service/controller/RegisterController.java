package ru.otus.auth.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.auth.service.model.dto.register.RegisterRequestDto;
import ru.otus.auth.service.model.dto.register.RegisterResponseDto;
import ru.otus.auth.service.service.RegisterService;

@Slf4j
@Validated
@RestController
@RequestMapping(RegisterController.BASE_PATH)
@RequiredArgsConstructor
public class RegisterController {

    public final static String BASE_PATH = "/api/v1/register";

    private final RegisterService service;

    @PostMapping
    public RegisterResponseDto register(@Valid @RequestBody RegisterRequestDto dto) {
        log.debug("Trying to register user with userName: {}", dto.getUsername());
        return service.register(dto);
    }
}
