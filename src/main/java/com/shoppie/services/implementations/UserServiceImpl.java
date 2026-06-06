package com.shoppie.services.implementations;

import com.shoppie.entities.User;
import com.shoppie.enums.UserRole;
import com.shoppie.enums.UserStatus;
import com.shoppie.exceptions.IncompatiblePasswordException;
import com.shoppie.exceptions.IncompatibleStatusChangeException;
import com.shoppie.exceptions.ResourceAlreadyExistsException;
import com.shoppie.exceptions.ResourceNotFoundException;
import com.shoppie.mappers.UserMapper;
import com.shoppie.payloads.user.UserRegisterRequest;
import com.shoppie.payloads.user.UserResponse;
import com.shoppie.payloads.user.UserUpdateRequest;
import com.shoppie.repositories.UserRepository;
import com.shoppie.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(UserRegisterRequest request) {
        User user = repository
                .findByEmailContainsIgnoreCase(request.email())
                .orElseThrow(
                        () -> new ResourceAlreadyExistsException(
                                String.format("User already exists with email: %s", request.email())
                        )
                );

        if (!passwordEncoder.matches(request.password(), request.confirmPassword())) {
            throw new IncompatiblePasswordException("Passwords are not compatible");
        }

        user.setEncodedPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
        user.setActivatedAt(LocalDateTime.now());
        User savedUser = repository.save(user);
        return mapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        User user = repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id: %d", id)));

        return mapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id: %d", id)));

        mapper.updateEntity(request, user);
        return mapper.toResponse(user);
    }

    @Override
    public void delete(Long id) {
        User user = repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id: %d", id)));

        repository.delete(user);
    }

    @Override
    public UserResponse freeze(Long id) {
        User user = repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id: %d", id)));

        if (user.getStatus() == UserStatus.FROZEN) {
            throw new IncompatibleStatusChangeException("User is already frozen");
        }

        user.setStatus(UserStatus.FROZEN);
        user.setFrozenAt(LocalDateTime.now());
        return mapper.toResponse(user);
    }

    @Override
    public UserResponse activate(Long id) {
        User user = repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id: %d", id)));

        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new IncompatibleStatusChangeException("User is already active");
        }

        user.setStatus(UserStatus.ACTIVE);
        user.setActivatedAt(LocalDateTime.now());
        user.setFrozenAt(null);
        return mapper.toResponse(user);
    }
}
