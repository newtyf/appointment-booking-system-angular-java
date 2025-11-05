package com.monarca.appointments.service;

import com.monarca.appointments.model.User;
import com.monarca.appointments.repository.UserRepository;
import com.monarca.appointments.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

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
    public User registerUser(User user) {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            return null;
        }

        // Validate role
        if (!isValidRole(user.getRole())) {
            throw new IllegalArgumentException("Invalid role. Must be one of: admin, receptionist, stylist, client");
        }

        // Hash password
        user.setHashedPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword(null); // Clear transient password
        
        // Set timestamps
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return userRepository.save(user);
    }

    public User authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();
        
        // Verify password
        if (!passwordEncoder.matches(password, user.getHashedPassword())) {
            return null;
        }

        return user;
    }

    public String generateToken(User user) {
        return jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole());
    }

    private boolean isValidRole(String role) {
        return role != null && (
                role.equals("admin") ||
                role.equals("receptionist") ||
                role.equals("stylist") ||
                role.equals("client")
        );
    }
}
