package ru.otus.auth.service.service;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.auth.service.mapper.RegisterMapper;
import ru.otus.auth.service.model.dto.register.RegisterRequestDto;
import ru.otus.auth.service.model.dto.register.RegisterResponseDto;
import ru.otus.auth.service.model.entity.UserRole;
import ru.otus.auth.service.repository.RoleRepository;
import ru.otus.auth.service.repository.UserRepository;
import ru.otus.auth.service.repository.UserRoleRepository;
import ru.otus.common.Role;
import ru.otus.lib.kafka.model.CreateAccountModel;
import ru.otus.lib.kafka.service.BusinessTopics;
import ru.otus.lib.kafka.service.KafkaProducerService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final KafkaProducerService kafkaProducerService;

    private final UserRoleRepository userRoleRepository;

    private final RegisterMapper mapper;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponseDto register(RegisterRequestDto dto) {

        checkUserAlreadyExists(dto);

        var user = mapper.toUser(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        var createdUser = userRepository.save(user);

        var clientRole = roleRepository.findByName(Role.CLIENT.name());

        if (clientRole.isPresent()) {
            var userRole = new UserRole();
            userRole.setUser(createdUser);
            userRole.setRole(clientRole.get());
            userRoleRepository.save(userRole);
        }

        kafkaProducerService
                .send(BusinessTopics.PAYMENT_NEW_ACCOUNT, new CreateAccountModel(createdUser.getId()));

        return new RegisterResponseDto(createdUser.getId());
    }

    private void checkUserAlreadyExists(RegisterRequestDto dto)
            throws EntityExistsException {
        var email = dto.getEmail();
        var existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            log.error("User with email {} already exists", email);
            throw new EntityExistsException("Пользователь с таким email '" + email + "' уже существует");
        }

        var username = dto.getUsername();
        var existingUserByUsername = userRepository.findByUsername(username);
        if (existingUserByUsername != null) {
            log.error("User with username {} already exists", username);
            throw new EntityExistsException("Пользователь с таким именем '" + username + "' уже существует");
        }
    }
}
