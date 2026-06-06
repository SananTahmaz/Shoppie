package com.shoppie.services;

import com.shoppie.payloads.user.UserRegisterRequest;
import com.shoppie.payloads.user.UserResponse;
import com.shoppie.payloads.user.UserUpdateRequest;

import java.util.List;

public interface UserService {
    UserResponse register(UserRegisterRequest request);
    UserResponse getById(Long id);
    List<UserResponse> getAll();
    UserResponse update(Long id, UserUpdateRequest request);
    void delete(Long id);
    UserResponse freeze(Long id);
    UserResponse activate(Long id);
}
