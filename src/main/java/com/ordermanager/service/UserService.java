package com.ordermanager.service;

import com.ordermanager.model.User;
import com.ordermanager.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User find(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    public void save(User user) {
        // Validações de negócio
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }

        userRepository.save(user);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found")
                );

        userRepository.delete(user);
    }

}