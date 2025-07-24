package ru.otus.auth.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.Encoders;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import ru.otus.auth.common.service.JwtProvider;
import ru.otus.common.error.ErrorDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements WebFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    private static final String JWT_PAYLOAD_HEADER = "X-Jwt-Payload";

    private static final String UNAUTHORIZED = "unauthorized";

    private final JwtProvider jwtProvider;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        var authHeader = exchange
                .getRequest()
                .getHeaders()
                .getFirst(AUTHORIZATION_HEADER_NAME);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            var jwt = authHeader.substring(BEARER_PREFIX.length());
            var isValidToken = jwtProvider.isValidToken(jwt);
            if (!isValidToken.getLeft()) {

                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                var response = ErrorDto.builder()
                        .code(UNAUTHORIZED)
                        .message(isValidToken.getRight())
                        .build();

                byte[] bytes = objectMapper.writeValueAsBytes(response);
                return exchange.getResponse()
                        .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)
                        ));
            }

            var userContext = jwtProvider.getShopUserFromToken(jwt);
            var payload = Encoders.BASE64.encode(objectMapper.writeValueAsBytes(userContext));

            var modifiedRequest = exchange.getRequest().mutate()
                    .header(AUTHORIZATION_HEADER_NAME, authHeader)
                    .header(JWT_PAYLOAD_HEADER, payload)
                    .build();

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userContext,
                    null,
                    jwtProvider.extractAuthorities(jwt)
            );

            return chain.filter(exchange.mutate().request(modifiedRequest).build())
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
        }

        return chain.filter(exchange);
    }
}