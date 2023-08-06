package com.signup.auth.authentication2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.signup.auth.authentication2.config.EmailNotConfirmedException;
import com.signup.auth.authentication2.config.TokenExpiredException;
import com.signup.auth.authentication2.config.TokenNotFoundException;
import com.signup.auth.authentication2.entity.User;
import com.signup.auth.authentication2.model.AuthenticationRequest;
import com.signup.auth.authentication2.model.AuthenticationResponse;
import com.signup.auth.authentication2.mail.MailSender;
import com.signup.auth.authentication2.model.RegisterRequest;
import com.signup.auth.authentication2.repository.UserRepository;
import com.signup.auth.authentication2.token.ConfirmationTokenService;
import com.signup.auth.authentication2.token.Token;
import com.signup.auth.authentication2.token.TokenRepository;
import com.signup.auth.authentication2.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
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
