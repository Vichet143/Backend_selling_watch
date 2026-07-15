package com.example.practice.controller;

import com.example.practice.config.security.AuthUser;
import com.example.practice.dto.*;
import com.example.practice.entity.User;
import com.example.practice.entity.VerificationCode;
import com.example.practice.exception.ApiException;
import com.example.practice.mapper.UserMapper;
import com.example.practice.repository.VerificationCodeRepository;
import com.example.practice.service.EmailVerificationService;
import com.example.practice.service.JwtService;
import com.example.practice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final EmailVerificationService emailVerificationService;
    private final JwtService jwtService;
    private final VerificationCodeRepository verificationCodeRepository;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User createuser = userService.createuser(user);
        UserDTO userDto = userMapper.toUserDto(createuser);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        System.out.println("Step 1: Login called");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        AuthUser user = userService.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        emailVerificationService.sendVerificationCode(user.getEmail());

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "OTP has been sent to your email.",
                user
        );

        if (responseMessageDTO.getMessage().equals("OTP has been sent to your email.")) {
            emailVerificationService.update(user.getEmail(),false);
        }

        return ResponseEntity.ok(responseMessageDTO);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp( @RequestBody VerifyOtpRequest request ) {

        //findUserByEmail
        AuthUser user = userService.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean valid = emailVerificationService.verifyCode(
                user.getEmail(),
                request.getOtp()
        );

        if (!valid) {
            return ResponseEntity.badRequest()
                    .body(new ApiException(HttpStatus.BAD_REQUEST, "false", "Invalid OTP"));
        }

        // Generate JWT here
        String token = jwtService.generateToken(user);

        UserDTO userDtoResponse = userMapper.toUserDtoResponse(user);

        LoginResponseShowToken loginResponseShowToken = new LoginResponseShowToken(userDtoResponse, token);
        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "login success.",
                loginResponseShowToken
        );
        System.out.println(responseMessageDTO.getMessage().equals("login success."));
        //check verify
        if (responseMessageDTO.getMessage().equals("login success.")) {
            emailVerificationService.update(request.getEmail(),true);
        }
        return ResponseEntity.ok(responseMessageDTO);
    }

    @GetMapping("/test")
    public String test() {
        return "Auth controller working";
    }
}
