package com.example.practice.service.impl;

import com.example.practice.config.security.AuthUser;
import com.example.practice.entity.VerificationCode;
import com.example.practice.exception.ApiException;
import com.example.practice.repository.VerificationCodeRepository;
import com.example.practice.service.EmailService;
import com.example.practice.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    @Override
    public void sendVerificationCode(String email) {
        String code = generateVerificationCode();

        VerificationCode verificationCode = verificationCodeRepository
                .findByEmail(email)
                .orElse(new VerificationCode());

        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(5));
        verificationCode.setVerified(false);

        verificationCodeRepository.save(verificationCode);

        emailService.sendEmail(
                email,
                "Verification Code",
                "Your OTP is: " + code
        );

    }

    @Override
    public String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    @Override
    public Boolean verifyCode(String email, String otp) {
        VerificationCode verificationCode = verificationCodeRepository
                .findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"false","OTP not found"));

        if (verificationCode.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"false","OTP is expired");
        }

        return verificationCode.getCode().equals(otp);
    }

    @Override
    public VerificationCode findEmail(String email) {
        return verificationCodeRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "false", "Email not found"));
    }

    @Override
    public VerificationCode update(String email, Boolean text) {
        try{
            VerificationCode email1 = findEmail(email);
            email1.setVerified(text);
            return verificationCodeRepository.save(email1);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false","Please check all the field verify");
        }

    }

}
