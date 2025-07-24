package ru.otus.auth.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.otus.auth.common.service.JwtProvider;
import ru.otus.common.ShopUser;
import ru.otus.common.error.ErrorDto;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class HeaderAuthFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer ";

    public static final String HEADER_NAME = "Authorization";

    private static final String UNAUTHORIZED = "unauthorized";

    private final JwtProvider jwtProvider;

    private final ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader(HEADER_NAME);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {

            var jwt = authHeader.substring(BEARER_PREFIX.length());

            var isValidToken = jwtProvider.isValidToken(jwt);
            if (!isValidToken.getLeft()) {
                createUnauthorizedResponse(isValidToken, response);
                return;
            }

            var shopUserFromToken = jwtProvider.getShopUserFromToken(jwt);

            if (shopUserFromToken != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {
                createSecurityContextWithUserFromToken(shopUserFromToken, request);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void createSecurityContextWithUserFromToken(ShopUser userContext,
                                                        HttpServletRequest request) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userContext,
                        null,
                        null
                );

        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
    }

    private void createUnauthorizedResponse(Pair<Boolean, String> isValidToken,
                                            HttpServletResponse response) throws IOException {
        var errorResponse = ErrorDto.builder()
                .code(UNAUTHORIZED)
                .message(isValidToken.getRight())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}