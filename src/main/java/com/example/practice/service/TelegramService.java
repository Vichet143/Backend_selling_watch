package com.example.practice.service;

import com.example.practice.config.TelegramConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TelegramService {

    private final TelegramConfig telegramConfig;
    private final RestTemplate restTemplate;

    public void sendMessage(String message) {

        String url = "https://api.telegram.org/bot"
                + telegramConfig.getBotToken()
                + "/sendMessage";

        Map<String, Object> body = new HashMap<>();
        body.put("chat_id", telegramConfig.getChatId());
        body.put("text", message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            url,
                            request,
                            String.class
                    );

            System.out.println("Telegram HTTP Status: "
                    + response.getStatusCode());

            System.out.println("Telegram Response: "
                    + response.getBody());

        } catch (Exception e) {
            System.out.println("Telegram Error:");
            e.printStackTrace();
        }
    }
}