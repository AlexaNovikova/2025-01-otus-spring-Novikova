package ru.otus.auth.service.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private String id;

    private String lastName;

    private String firstName;

    private String username;

    private String email;
}
