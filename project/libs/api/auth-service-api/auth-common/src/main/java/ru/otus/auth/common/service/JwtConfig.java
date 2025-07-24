package ru.otus.auth.common.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String secret;

    private String refreshSecret;

    private Long expirationInMs;

    private Long refreshExpirationInMs;

}