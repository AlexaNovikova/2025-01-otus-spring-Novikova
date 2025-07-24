package ru.otus.auth.service.mapper;

import org.mapstruct.Mapper;
import ru.otus.auth.service.model.dto.register.RegisterRequestDto;
import ru.otus.auth.service.model.entity.User;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    User toUser(RegisterRequestDto dto);
}
