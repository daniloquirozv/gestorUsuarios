package com.gestusers.gestorUsers.service;

import com.gestusers.gestorUsers.model.User;
import com.gestusers.gestorUsers.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> userProfile(Long userId) {
        return userRepository.findById(userId);
    }

    public User updateProfile(Long userId, User updatedUser) {
        return userRepository.findById(userId)
            .map(user -> {
                user.setUsername(updatedUser.getUsername());
                user.setEmail(updatedUser.getEmail());
                // Agrega otros campos que quieras actualizar
                return userRepository.save(user);
            }).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }
}
