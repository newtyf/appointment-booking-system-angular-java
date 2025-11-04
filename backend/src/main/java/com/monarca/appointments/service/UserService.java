package com.monarca.appointments.service;

import com.monarca.appointments.exception.ResourceNotFoundException;
import com.monarca.appointments.model.User;
import com.monarca.appointments.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }
}
