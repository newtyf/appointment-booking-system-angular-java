package com.monarca.appointments.service;

import com.monarca.appointments.dto.AuthResponse;
import com.monarca.appointments.dto.LoginRequest;
import com.monarca.appointments.dto.RegisterRequest;
import com.monarca.appointments.dto.UserResponse;
import com.monarca.appointments.exception.BadRequestException;
import com.monarca.appointments.model.User;
import com.monarca.appointments.repository.UserRepository;
import com.monarca.appointments.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setHashedPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("client"); // Default role

        user = userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole());

        // Create user response
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );

        return new AuthResponse(token, userResponse);
    }

    public AuthResponse login(LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getHashedPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole());

        // Create user response
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );

        return new AuthResponse(token, userResponse);
    }
}
