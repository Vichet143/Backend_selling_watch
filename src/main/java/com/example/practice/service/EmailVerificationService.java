package com.example.practice.service;

import com.example.practice.config.security.AuthUser;
import com.example.practice.entity.VerificationCode;

import java.util.Optional;

public interface EmailVerificationService {
    void sendVerificationCode(String email);
    String generateVerificationCode();
    Boolean verifyCode(String email, String otp);

    VerificationCode findEmail(String email);
    VerificationCode update(String email, Boolean text);
}
