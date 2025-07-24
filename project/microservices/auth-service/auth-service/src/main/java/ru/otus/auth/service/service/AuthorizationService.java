package ru.otus.auth.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.otus.auth.common.model.AuthorizationContext;
import ru.otus.auth.common.service.JwtProvider;
import ru.otus.auth.service.mapper.UserMapper;
import ru.otus.auth.service.model.dto.auth.AuthRefreshRequestDto;
import ru.otus.auth.service.model.dto.auth.AuthRequestDto;
import ru.otus.auth.service.model.dto.auth.AuthResponseDto;
import ru.otus.common.ShopUser;
import ru.otus.common.error.ShopException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private static final String UNPROCESSABLE_ENTITY = "unprocessable.entity";

    private final AuthenticationManager authenticationManager;

    private final UserMapper mapper;

    private final JwtProvider jwtProvider;

    private final UserService userService;


    public AuthResponseDto authenticate(AuthRequestDto dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.getUsername(),
                dto.getPassword())
        );

        var userDetails = userService.userDetailsService()
                .loadUserByUsername(dto.getUsername());
        var userContext = mapper.toUserContext((AuthorizationContext) userDetails);

        var token = jwtProvider.generateToken(userContext);
        var refreshToken = jwtProvider.generateRefreshToken(userContext);
        var profile = mapper.toDto(userContext);
        return AuthResponseDto.builder()
                .token(token)
                .refreshToken(refreshToken)
                .profile(profile)
                .build();
    }

    public AuthResponseDto refreshToken(AuthRefreshRequestDto dto) {
        var refreshJwt = dto.getRefreshToken();

        var isValidToken = jwtProvider.isValidToken(refreshJwt);
        if (!isValidToken.getLeft()) {
            log.error("Invalid refresh token");
            throw new ShopException(UNPROCESSABLE_ENTITY, isValidToken.getRight());
        }


        ShopUser user = jwtProvider.getShopUserFromToken(refreshJwt);

        var userDetails = userService.userDetailsService()
                .loadUserByUsername(user.getUsername());

        var userContext = mapper.toUserContext((AuthorizationContext) userDetails);

        var token = jwtProvider.generateToken(userContext);
        var refreshToken = jwtProvider.generateRefreshToken(userContext);
        var profile = mapper.toDto(userContext);

        return AuthResponseDto.builder()
                .token(token)
                .refreshToken(refreshToken)
                .profile(profile)
                .build();
    }


}
