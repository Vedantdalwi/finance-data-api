package com.finance.dataprocessing.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.finance.dataprocessing.common.response.ApiResponse;
import com.finance.dataprocessing.user.dto.LoginRequestDto;
import com.finance.dataprocessing.user.dto.UserRequestDto;
import com.finance.dataprocessing.user.dto.UserResponseDto;
import com.finance.dataprocessing.user.dto.UserUpdateDto;
import com.finance.dataprocessing.user.service.UserService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(
            @Valid @RequestBody UserRequestDto request) {

        UserResponseDto user = userService.createUser(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User created successfully", user, Instant.now())
        );
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDto>> login(
            @Valid @RequestBody LoginRequestDto request) {

        UserResponseDto user = userService.login(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Login successful", user, Instant.now())
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable UUID id) {

        UserResponseDto user = userService.getUserById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User fetched successfully", user, Instant.now())
        );
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {

        List<UserResponseDto> users = userService.getAllUsers();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Users fetched successfully", users, Instant.now())
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable UUID id,
            @RequestBody UserUpdateDto request) {

        UserResponseDto user = userService.updateUser(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User updated successfully", user, Instant.now())
        );
    }


    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable UUID id) {

        userService.deactivateUser(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User deactivated successfully", null, Instant.now())
        );
    }


    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable UUID id) {

        userService.activateUser(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User activated successfully", null, Instant.now())
        );
    }
}
