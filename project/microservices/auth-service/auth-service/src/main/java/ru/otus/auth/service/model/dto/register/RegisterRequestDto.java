package ru.otus.auth.service.model.dto.register;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]*$")
    private String lastName;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]*$")
    private String firstName;

    @NotEmpty
    private String username;

    @NotEmpty
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    @NotEmpty
    private String password;
}
