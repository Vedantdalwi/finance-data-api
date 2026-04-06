package com.finance.dataprocessing.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

import com.finance.dataprocessing.user.enums.UserRole;
import com.finance.dataprocessing.user.enums.UserStatus;

@Getter
@Setter
public class UserResponseDto {

    private UUID id;

    private String fullName;

    private String userName;

    private String email;

    private UserRole role;

    private UserStatus status;
    
    private String token;

    private Instant lastLoginAt;

    private Instant createdAt;

    private Instant updatedAt;
}