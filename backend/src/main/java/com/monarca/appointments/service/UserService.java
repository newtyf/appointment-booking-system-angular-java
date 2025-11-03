package com.monarca.appointments.service;

import com.monarca.appointments.dto.UserResponse;
import com.monarca.appointments.exception.ResourceNotFoundException;
import com.monarca.appointments.model.User;
import com.monarca.appointments.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return toResponse(user);
    }

    public List<UserResponse> getUsersByRole(String role) {
        return userRepository.findByRole(role).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }
}
