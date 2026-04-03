package com.finance.dataprocessing.user.dto;

import com.finance.dataprocessing.user.enums.UserRole;
import com.finance.dataprocessing.user.enums.UserStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {

    private String fullName;

    private UserRole role;

    private UserStatus status;
}
