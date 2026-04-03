package com.finance.dataprocessing.user.service;

import java.util.List;
import java.util.UUID;

import com.finance.dataprocessing.user.dto.LoginRequestDto;
import com.finance.dataprocessing.user.dto.UserRequestDto;
import com.finance.dataprocessing.user.dto.UserResponseDto;
import com.finance.dataprocessing.user.dto.UserUpdateDto;


public interface UserService {

	UserResponseDto createUser(UserRequestDto request);
	
	UserResponseDto login(LoginRequestDto request);

	UserResponseDto getUserById(UUID id);

    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUser(UUID id, UserUpdateDto request);

    void deactivateUser(UUID id);

    void activateUser(UUID id);



}
