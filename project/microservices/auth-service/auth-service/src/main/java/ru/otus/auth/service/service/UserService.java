package ru.otus.auth.service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.auth.common.model.AuthorizationContext;
import ru.otus.auth.service.mapper.UserMapper;
import ru.otus.auth.service.model.dto.user.UpdateUserRequestDto;
import ru.otus.auth.service.model.dto.user.UserListResponseDto;
import ru.otus.auth.service.model.dto.user.UserResponseDto;
import ru.otus.auth.service.model.entity.User;
import ru.otus.auth.service.repository.UserRepository;
import ru.otus.auth.service.repository.UserRoleRepository;
import ru.otus.common.ShopUser;

import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper mapper;

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    @Transactional
    public UserResponseDto getById(Long id, ShopUser shopUser) {
        if (!Objects.equals(shopUser.getId(), id)) {
            log.error("Profile viewing is forbidden for user with id: {}", id);
            throw new AccessDeniedException("Profile viewing is forbidden");
        }
        var user = getUser(id);
        return mapper.toDto(user);
    }

    @Transactional
    public UserResponseDto update(Long id, UpdateUserRequestDto dto, ShopUser shopUser) {
        if (!Objects.equals(shopUser.getId(), id)) {
            log.error("Profile update is forbidden for user with id: {}", id);
            throw new AccessDeniedException("Profile viewing is forbidden");
        }

        var user = getUser(id);
        setNewValuesForUser(dto, user);
        userRepository.save(user);
        return mapper.toDto(user);
    }

    private void setNewValuesForUser(UpdateUserRequestDto dto, User currentUser) {
        if (dto.getLastName() != null) {
            currentUser.setLastName(dto.getLastName());
        }

        if (dto.getFirstName() != null) {
            currentUser.setFirstName(dto.getFirstName());
        }

        if (dto.getEmail() != null) {
            currentUser.setEmail(dto.getEmail());
        }
    }

    public UserListResponseDto getAll() {
        var users = userRepository.findAll
                (Sort.by("lastName", "firstName"));
        var usersDto = mapper.toDtos(users);
        return new UserListResponseDto(usersDto);
    }

    private User getUser(Long id) {
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            log.error("User with id {} not found", id);
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        return user.get();
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public AuthorizationContext getByUsername(String username) {
        var user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("User with userName {} not found", username);
            throw new UsernameNotFoundException("User not found");
        }

        var roles = userRoleRepository.findRoleNamesByUserId(user.getId());

        var authContext = mapper.toUserContext(user);
        authContext.setUsername(user.getUsername());
        authContext.setFirstName(user.getFirstName());
        authContext.setPassword(user.getPassword());
        authContext.setRoles(roles);
        authContext.setEmail(user.getEmail());
        return authContext;
    }
}
