package com.example.practice.controller;

import com.example.practice.config.security.AuthUser;
import com.example.practice.dto.LoginRequest;
import com.example.practice.dto.LoginResponseShowToken;
import com.example.practice.dto.ResponseMessageDTO;
import com.example.practice.dto.UserDTO;
import com.example.practice.dto.VerifyOtpRequest;
import com.example.practice.entity.User;
import com.example.practice.exception.ApiException;
import com.example.practice.mapper.UserMapper;
import com.example.practice.repository.UserRepository;
import com.example.practice.service.EmailVerificationService;
import com.example.practice.service.JwtService;
import com.example.practice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final EmailVerificationService emailVerificationService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @RequestBody User user
    ) {

        if (userRepository.existsByEmail(user.getEmail())) {

            return ResponseEntity
                    .badRequest()
                    .body("Email already exists");
        }

        User createdUser =
                userService.createuser(user);

        UserDTO userDto =
                userMapper.toUserDto(createdUser);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ) {

        System.out.println("==============================");
        System.out.println("LOGIN REQUEST");
        System.out.println("==============================");

        System.out.println(
                "Email: " + request.getEmail()
        );

        if (request.getEmail() == null ||
                request.getEmail().isBlank()) {

            return ResponseEntity
                    .badRequest()
                    .body("Email is required");
        }

        if (request.getPassword() == null ||
                request.getPassword().isBlank()) {

            return ResponseEntity
                    .badRequest()
                    .body("Password is required");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        System.out.println(
                "Authentication successful"
        );

        User user =
                userService.findUserEntityByEmail(
                        request.getEmail()
                );

        System.out.println(
                "User found: " + user.getEmail()
        );

        emailVerificationService.sendVerificationCode(
                user.getEmail()
        );

        emailVerificationService.update(
                user.getEmail(),
                false
        );

        ResponseMessageDTO<?> responseMessageDTO =
                new ResponseMessageDTO<>(
                        true,
                        "OTP has been sent to your email.",
                        user
                );

        return ResponseEntity.ok(
                responseMessageDTO
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestBody VerifyOtpRequest request
    ) {

        System.out.println("==============================");
        System.out.println("VERIFY OTP");
        System.out.println("==============================");

        AuthUser user =
                userService.findUserByEmail(
                        request.getEmail()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        boolean valid =
                emailVerificationService.verifyCode(
                        user.getEmail(),
                        request.getOtp()
                );

        if (!valid) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            new ApiException(
                                    HttpStatus.BAD_REQUEST,
                                    "false",
                                    "Invalid OTP"
                            )
                    );
        }

        String token =
                jwtService.generateToken(user);

        UserDTO userDtoResponse =
                userMapper.toUserDtoResponse(user);

        LoginResponseShowToken loginResponseShowToken =
                new LoginResponseShowToken(
                        userDtoResponse,
                        token
                );

        ResponseMessageDTO<?> responseMessageDTO =
                new ResponseMessageDTO<>(
                        true,
                        "login success.",
                        loginResponseShowToken
                );


        emailVerificationService.update(
                request.getEmail(),
                true
        );

        return ResponseEntity.ok(
                responseMessageDTO
        );
    }

    @GetMapping("/test")
    public String test() {

        return "Auth controller working";
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<?> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                userService.findById(id)
        );
    }
}