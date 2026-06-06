package com.shoppie.controllers;

import com.shoppie.globals.ApiResponse;
import com.shoppie.payloads.user.UserRegisterRequest;
import com.shoppie.payloads.user.UserResponse;
import com.shoppie.payloads.user.UserUpdateRequest;
import com.shoppie.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "UserController", description = "User features for Shoppie")
public class UserController {
    private final UserService service;

    @PostMapping
    @Operation(summary = "Register user")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody UserRegisterRequest request) {
        UserResponse response = service.register(request);
        return new ResponseEntity<>(ApiResponse.success("User registered successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ApiResponse<UserResponse> getById(@PathVariable Long id) {
        return ApiResponse.success("User was fetched successfully", service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public ApiResponse<List<UserResponse>> getAll() {
        return ApiResponse.success("User list was fetched successfully", service.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        UserResponse response = service.update(id, request);
        return new ResponseEntity<>(ApiResponse.success("User was updated successfully", response), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(ApiResponse.success("User was deleted successfully", null), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/freeze")
    @Operation(summary = "Freeze user")
    public ResponseEntity<ApiResponse<UserResponse>> freeze(@PathVariable Long id) {
        UserResponse response = service.freeze(id);
        return new ResponseEntity<>(ApiResponse.success("User was frozen successfully", response), HttpStatus.OK);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate user")
    public ResponseEntity<ApiResponse<UserResponse>> activate(@PathVariable Long id) {
        UserResponse response = service.activate(id);
        return new ResponseEntity<>(ApiResponse.success("User was activated successfully", response), HttpStatus.OK);
    }
}
