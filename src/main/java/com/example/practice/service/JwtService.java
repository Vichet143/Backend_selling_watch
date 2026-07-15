package com.example.practice.service;

import com.example.practice.config.security.AuthUser;

public interface JwtService {
    String generateToken(AuthUser user);

}
