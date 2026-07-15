package com.example.practice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String otp) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <body style="font-family: Arial, sans-serif; background:#f4f4f4; padding:20px;">

                <div style="
                    max-width:500px;
                    margin:auto;
                    background:white;
                    padding:30px;
                    border-radius:10px;
                    text-align:center;
                ">

                    <h2 style="color:#2563eb;">
                        Email Verification
                    </h2>

                    <p>
                        Hello,
                    </p>

                    <p>
                        We received a request to verify your account.
                        Use the OTP below:
                    </p>


                    <div style="
                        font-size:32px;
                        font-weight:bold;
                        letter-spacing:8px;
                        color:#111827;
                        background:#f3f4f6;
                        padding:15px;
                        border-radius:8px;
                        margin:20px 0;
                    ">
                        %s
                    </div>


                    <p>
                        This OTP will expire in 
                        <b>5 minutes</b>.
                    </p>


                    <p style="color:#6b7280;font-size:14px;">
                        If you did not request this verification,
                        you can ignore this email.
                    </p>


                    <hr>

                    <p style="color:#9ca3af;font-size:12px;">
                        © 2026 KVC SHOP
                    </p>

                </div>

                </body>
                </html>
                """.formatted(otp);


            helper.setText(htmlContent, true);

            mailSender.send(message);


        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}