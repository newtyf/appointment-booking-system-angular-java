package com.monarca.appointments.service;

import com.monarca.appointments.exception.BadRequestException;
import com.monarca.appointments.model.User;
import com.monarca.appointments.repository.UserRepository;
import com.monarca.appointments.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

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
    public Map<String, Object> register(User user) {
        // Check if user already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        // Encode password and set default role
        user.setHashedPassword(passwordEncoder.encode(user.getHashedPassword()));
        if (user.getRole() == null) {
            user.setRole("client");
        }

        user = userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole());

        // Create response
        Map<String, Object> response = new HashMap<>();
        response.put("access_token", token);
        response.put("user", user);

        return response;
    }

    public Map<String, Object> login(String email, String password) {
        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        // Verify password
        if (!passwordEncoder.matches(password, user.getHashedPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole());

        // Create response
        Map<String, Object> response = new HashMap<>();
        response.put("access_token", token);
        response.put("user", user);

        return response;
    }
}
