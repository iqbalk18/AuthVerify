package com.signup.auth.authentication2.service;

import com.signup.auth.authentication2.exception.EmailNotConfirmedException;
import com.signup.auth.authentication2.exception.TokenExpiredException;
import com.signup.auth.authentication2.exception.TokenNotFoundException;
import com.signup.auth.authentication2.entity.User;
import com.signup.auth.authentication2.model.*;
import com.signup.auth.authentication2.mail.MailSender;
import com.signup.auth.authentication2.phone.SmsSender;
import com.signup.auth.authentication2.repository.UserRepository;
import com.signup.auth.authentication2.token.ConfirmationTokenService;
import com.signup.auth.authentication2.token.Token;
import com.signup.auth.authentication2.token.TokenRepository;
import com.signup.auth.authentication2.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MailSender mailSender;
    private final ConfirmationTokenService confirmationTokenService;
    private final SmsSender smsSender;

    public AuthenticationResponse register(RegisterRequest request) {
        Optional<User> existingUserOptional = repository.findByEmail(request.getEmail());
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setFirstname(request.getFirstname());
            existingUser.setLastname(request.getLastname());
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
            existingUser.setRole(request.getRole());
            repository.save(existingUser);
        } else {
            User newUser = User.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .enabled(false)
                    .build();
            repository.save(newUser);
        }

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        String link = "http://localhost:8080/api/v1/auth/confirm?token=" + jwtToken;
        mailSender.send(request.getEmail(), buildVerificationEmail(request.getFirstname(), link));
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse registerWithPhone(RegisterRequestPhone request) {
        Optional<User> existingUserOptional = repository.findByPhone(request.getPhone());
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setFirstname(request.getFirstname());
            existingUser.setLastname(request.getLastname());
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
            existingUser.setRole(request.getRole());
            repository.save(existingUser);
        } else {
            User newUser = User.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .enabled(false)
                    .build();
            repository.save(newUser);
        }
        var user = repository.findByPhone(request.getPhone())
                .orElseThrow(() -> new UsernameNotFoundException("Phone number not found"));

        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        String link = "http://localhost:8080/api/v1/auth/confirm?token=" + jwtToken;
        String smsMessage = "Click the link below to activate your account: " + link;

        RegisterRequestPhone smsRequest = RegisterRequestPhone.builder()
                .phone(user.getPhone())
                .message(smsMessage)
                .build();

        smsSender.sendSms(smsRequest);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user;

        if (request.getEmailOrPhone().contains("@")) {
            user = repository.findByEmail(request.getEmailOrPhone())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } else {
            user = repository.findByPhone(request.getEmailOrPhone())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }

        if (!user.isEnabled()) {
            throw new EmailNotConfirmedException("Email has not been confirmed");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmailOrPhone(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }


    public ForgotPasswordResponse requestPasswordReset(ForgotPasswordRequest request) {
        User user;

        if (request.getEmailOrPhone().contains("@")) {
            user = repository.findByEmail(request.getEmailOrPhone())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } else {
            user = repository.findByPhone(request.getEmailOrPhone())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }

        String resetToken = generatePasswordResetToken(user);
        confirmationTokenService.saveConfirmationToken(createPasswordResetToken(user, resetToken));

        return new ForgotPasswordResponse("Password reset token generated successfully");
    }


    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        Token resetToken = confirmationTokenService.getToken(request.getToken())
                .orElseThrow(() -> new TokenNotFoundException("Token not found"));

        LocalDateTime expiredAt = resetToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token has expired. Please request a new password reset.");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);

        confirmationTokenService.setConfirmedAt(request.getToken());

        return ChangePasswordResponse.builder()
                .message("Password changed successfully")
                .build();
    }

    private Token createPasswordResetToken(User user, String token) {
        var expiredAt = LocalDateTime.now().plusMinutes(15);
        return Token.builder()
                .user(user)
                .token(token)
                .tokenType(TokenType.RESET_PASSWORD)
                .expired(false)
                .revoked(false)
                .confirmedAt(LocalDateTime.now())
                .expiresAt(expiredAt)
                .createdAt(LocalDateTime.now())
                .build();
    }
    private String generatePasswordResetToken(User user) {
        String resetToken = "your_generated_reset_token_here";
        return resetToken;
    }

    private void saveUserToken(User user, String jwtToken) {
        var expiredAt = LocalDateTime.now().plusMinutes(1);
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .confirmedAt(LocalDateTime.now())
                .expiresAt(expiredAt)
                .createdAt(LocalDateTime.now())
                .build();
        confirmationTokenService.saveConfirmationToken(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    @Transactional
    public String confirmToken(String token) {
        Token confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new TokenNotFoundException("token not found"));

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token has expired. Please register again.");
        }

        confirmationTokenService.setConfirmedAt(token);
        User user = confirmationToken.getUser();
        user.setEnabled(true);
        jwtService.enableAppUser(confirmationToken.getUser().getEmail());
        return "confirmed";
    }

    private String buildVerificationEmail(String name, String link) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Confirm your email</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Helvetica, Arial, sans-serif;\n" +
                "            font-size: 16px;\n" +
                "            margin: 0;\n" +
                "            color: #0b0c0c;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 580px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "            background-color: #f0f0f0;\n" +
                "            border-radius: 10px;\n" +
                "        }\n" +
                "        .logo {\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .logo img {\n" +
                "            max-width: 200px;\n" +
                "        }\n" +
                "        .header {\n" +
                "            font-size: 24px;\n" +
                "            font-weight: 700;\n" +
                "            margin-bottom: 20px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .confirmation-text {\n" +
                "            font-size: 18px;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .confirmation-link {\n" +
                "            display: block;\n" +
                "            width: 200px;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 15px;\n" +
                "            background-color: #6c63ff; /* Change to purple color */\n" +
                "            color: #ffffff;\n" +
                "            text-align: center;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 20px; /* Rounded corners */\n" +
                "            font-size: 16px;\n" +
                "            font-weight: 700;\n" +
                "            box-shadow: 0 2px 4px rgba(108, 99, 255, 0.2); /* Subtle box shadow */\n" +
                "        }\n" +
                "        .footer {\n" +
                "            font-size: 14px;\n" +
                "            text-align: center;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"logo\">\n" +
                "            <img src=\"https://example.com/logo.png\" alt=\"Logo\">\n" +
                "        </div>\n" +
                "        <div class=\"header\">Confirm your email</div>\n" +
                "        <div class=\"confirmation-text\">Hi " + name + ",</div>\n" +
                "        <div class=\"confirmation-text\">Thank you for registering. Please click on the below link to activate your account:</div>\n" +
                "        <div>\n" +
                "            <a class=\"confirmation-link\" href=\"" + link + "\">Activate Now</a>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">Link will expire in 15 minutes. See you soon!</div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
