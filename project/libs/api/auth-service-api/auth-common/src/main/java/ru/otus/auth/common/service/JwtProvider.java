package ru.otus.auth.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.otus.common.ShopUser;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtConfig properties;

    private Key signingKey;

    private Key refreshSigningKey;

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        signingKey = getSigningKey(properties.getSecret());
        if (properties.getRefreshSecret() != null) {
            refreshSigningKey = getSigningKey(properties.getRefreshSecret());
        }
    }

    @SneakyThrows
    public String generateToken(ShopUser shopUser) {
        var expirationTime = properties.getExpirationInMs();
        return generateToken(shopUser, expirationTime, signingKey);
    }

    @SneakyThrows
    public String generateToken(ShopUser shopUser, Long expirationTime, Key key) {

        var subject = shopUser != null ? objectMapper.writeValueAsString(shopUser) : null;
        var roles = shopUser != null ? shopUser.getRoles() : null;
        var expiration = expirationTime != -1 ?
                new Date(System.currentTimeMillis() + expirationTime) : null;

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .claim("roles", roles)
                .signWith(key);

        return builder.compact();
    }

    @SneakyThrows
    public String generateRefreshToken(ShopUser shopUser) {
        var expirationTime = properties.getRefreshExpirationInMs();
        return generateToken(shopUser, expirationTime, signingKey);
    }


    public Pair<Boolean, String> isValidToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build()
                    .parseClaimsJws(token);
            return Pair.of(true, "");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT signature");
            return Pair.of(false, "Недействительная подпись JWT токена");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            return Pair.of(false, "Неподдерживаемый JWT token");
        } catch (ExpiredJwtException ex) {
            log.debug("Expired JWT token");
            return Pair.of(false, "Истек срок действия JWT токена");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            return Pair.of(false, "JWT клэймы не указаны");
        } catch (Exception e) {
            log.error("Error get user from token", e);
            return Pair.of(false, "Ошибка во время обработки JWT токена");
        }
    }

    @SneakyThrows
    public ShopUser getShopUserFromToken(String token) {
        var subject = Jwts.parserBuilder().setSigningKey(signingKey).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return objectMapper.readValue(subject, ShopUser.class);
    }

    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);

        if (claims.get("authorities") != null) {
            @SuppressWarnings("unchecked")
            List<String> authorities = claims.get("authorities", List.class);
            return authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}