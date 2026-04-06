package com.finance.dataprocessing.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finance.dataprocessing.common.exception.NotFoundException;
import com.finance.dataprocessing.jwt.JwtUtil;
import com.finance.dataprocessing.common.exception.ConflictException;
import com.finance.dataprocessing.user.dto.LoginRequestDto;
import com.finance.dataprocessing.user.dto.UserRequestDto;
import com.finance.dataprocessing.user.dto.UserResponseDto;
import com.finance.dataprocessing.user.dto.UserUpdateDto;
import com.finance.dataprocessing.user.entity.User;
import com.finance.dataprocessing.user.enums.UserStatus;
import com.finance.dataprocessing.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;

    @Override
    public UserResponseDto createUser(UserRequestDto request) {

        String email = request.getEmail().toLowerCase();
        String userName = request.getUserName().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already exists");
        }

        if (userRepository.existsByUserName(userName)) {
            throw new ConflictException("Username already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(email);
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        UserResponseDto response = mapToDto(savedUser);
        response.setToken(jwtUtil.generateToken(user.getEmail())); 
        return response;
    }
    
    @Override
    public UserResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));// todo : fix

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");// todo : fix
        }

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new RuntimeException("User is inactive");// todo : fix
        }

        user.setLastLoginAt(Instant.now());
        
        userRepository.save(user);
        
        UserResponseDto response = mapToDto(user);
        response.setToken(jwtUtil.generateToken(user.getEmail())); 
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto getUserById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return mapToDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUser(UUID id, UserUpdateDto request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        User updatedUser = userRepository.save(user);

        return mapToDto(updatedUser);
    }

    @Override
    public void deactivateUser(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setStatus(UserStatus.INACTIVE);

        userRepository.save(user);
    }

    @Override
    public void activateUser(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);
    }


    
    //Mapper

    private UserResponseDto mapToDto(User user) {

        UserResponseDto dto = new UserResponseDto();

        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setLastLoginAt(user.getLastLoginAt());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }
}