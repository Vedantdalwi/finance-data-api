package com.finance.dataprocessing.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.finance.dataprocessing.common.response.ApiResponse;
import com.finance.dataprocessing.user.dto.LoginRequestDto;
import com.finance.dataprocessing.user.dto.UserRequestDto;
import com.finance.dataprocessing.user.dto.UserResponseDto;
import com.finance.dataprocessing.user.dto.UserUpdateDto;
import com.finance.dataprocessing.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Tag(name = "User APIs", description = "User management and authentication APIs")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Create User",
            description = "Creates a new user with role and credentials"
    )
    @ApiResponses(value = {
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
                    )
            ),
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER', 'ANALYST')")
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(
            @Valid @RequestBody UserRequestDto request) {

        UserResponseDto user = userService.createUser(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User created successfully", user, Instant.now())
        );
    }


    @Operation(
            summary = "User Login",
            description = "Authenticates user using email and password"
    )
    @ApiResponses(value = {
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
                    )
            ),
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER', 'ANALYST')")
    public ResponseEntity<ApiResponse<UserResponseDto>> login(
            @Valid @RequestBody LoginRequestDto request) {

        UserResponseDto user = userService.login(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Login successful", user, Instant.now())
        );
    }

    @Operation(
            summary = "Get User by ID",
            description = "Fetch user details by unique ID"
    )
    @ApiResponses(value = {
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User fetched successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
                    )
            ),
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER', 'ANALYST')")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable UUID id) {

        UserResponseDto user = userService.getUserById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User fetched successfully", user, Instant.now())
        );
    }


    @Operation(
            summary = "Get All Users",
            description = "Fetch all users in the system"
    )
    @ApiResponses(value = {
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Users fetched successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UserResponseDto.class)
                            )
                    )
            )
    })
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {

        List<UserResponseDto> users = userService.getAllUsers();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Users fetched successfully", users, Instant.now())
        );
    }

    @Operation(
            summary = "Update User",
            description = "Updates user details (only provided fields will be updated)"
    )
    @ApiResponses(value = {
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
                    )
            ),
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable UUID id,
            @RequestBody UserUpdateDto request) {

        UserResponseDto user = userService.updateUser(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User updated successfully", user, Instant.now())
        );
    }


    @Operation(
            summary = "Deactivate User",
            description = "Marks a user as INACTIVE"
    )
    @ApiResponses(value = {
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User deactivated successfully"
            ),
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable UUID id) {

        userService.deactivateUser(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User deactivated successfully", null, Instant.now())
        );
    }

    @Operation(
            summary = "Activate User",
            description = "Marks a user as ACTIVE"
    )
    @ApiResponses(value = {
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User activated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable UUID id) {

        userService.activateUser(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User activated successfully", null, Instant.now())
        );
    }
}
