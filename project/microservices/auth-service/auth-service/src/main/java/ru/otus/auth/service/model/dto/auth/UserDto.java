package ru.otus.auth.service.model.dto.auth;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private String id;

    private String username;

    private List<String> roles;
}