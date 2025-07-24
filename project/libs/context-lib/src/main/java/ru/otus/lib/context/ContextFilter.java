package ru.otus.lib.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.otus.common.ShopUser;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class ContextFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public static final String JWT_PAYLOAD_HEADER = "X-Jwt-Payload";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var payloadBase64 = request.getHeader(JWT_PAYLOAD_HEADER);

        if (!payloadBase64.isEmpty()) {
            Base64.Decoder decoder = Base64.getUrlDecoder();
            var payload = decoder.decode(payloadBase64);
            var userContext = objectMapper.readValue(payload, ShopUser.class);
            request.setAttribute("userContext", userContext);
        }

        filterChain.doFilter(request, response);
    }
}