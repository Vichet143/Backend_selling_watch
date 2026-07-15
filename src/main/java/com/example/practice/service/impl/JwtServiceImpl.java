package com.example.practice.service.impl;

import com.example.practice.config.security.AuthUser;
import com.example.practice.entity.VerificationCode;
import com.example.practice.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Override
    public String generateToken(AuthUser user){

        List<String> authorities = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String secretKey = "my-super-secret-key-that-is-at-least-48-characters-long-123456789";
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .claim("authorities", authorities)
                .expiration(Date.from(
                        LocalDateTime.now()
                                .plusHours(1)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()))
                .issuer("sellshop.com")
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
