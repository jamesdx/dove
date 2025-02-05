package com.helix.dove.auth.service;

import com.helix.dove.auth.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    
    User createUser(User user);
    
    Optional<User> getUserById(Long id);
    
    Optional<User> getUserByUsername(String username);
    
    Optional<User> getUserByEmail(String email);
    
    User updateUser(User user);
    
    void deleteUser(Long id);
    
    void updatePassword(Long userId, String newPassword);
    
    void updateEnabled(Long userId, boolean enabled);
    
    void recordLogin(Long userId);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
} 