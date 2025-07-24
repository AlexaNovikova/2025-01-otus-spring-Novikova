package ru.otus.auth.service.mapper;

import org.mapstruct.Mapper;
import ru.otus.auth.common.model.AuthorizationContext;
import ru.otus.auth.service.model.dto.auth.UserDto;
import ru.otus.auth.service.model.dto.user.CreateUserRequestDto;
import ru.otus.auth.service.model.dto.user.UserResponseDto;
import ru.otus.auth.service.model.entity.User;
import ru.otus.common.ShopUser;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    AuthorizationContext toUserContext(User user);

    User toEntity(CreateUserRequestDto dto);

    UserResponseDto toDto(User user);

    List<UserResponseDto> toDtos(List<User> users);

    UserDto toDto(ShopUser shopUser);

    ShopUser toUserContext(AuthorizationContext authContext);
}
