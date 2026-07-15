package com.example.practice.service;

import com.example.practice.config.security.AuthUser;
import com.example.practice.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<AuthUser> findUserByUsername(String username);
    User createuser(User user);
    Optional<AuthUser> findUserByEmail(String email);
}
