package com.example.practice.controller;

import com.example.practice.config.TelegramConfig;
import com.example.practice.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/telegram")
public class TelegramController {

    private final TelegramService telegramService;
    private final TelegramConfig telegramConfig;

    @PostMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("Telegram bot token exists: "
                + (telegramConfig.getBotToken() != null));

        System.out.println("Telegram bot token length: "
                + (telegramConfig.getBotToken() != null
                ? telegramConfig.getBotToken().length()
                : 0));

        System.out.println("Telegram chat ID: "
                + telegramConfig.getChatId());

        telegramService.sendMessage(
                "🚀 Hello! Telegram is connected to Spring Boot."
        );

        return ResponseEntity.ok("Message sent");
    }
}
