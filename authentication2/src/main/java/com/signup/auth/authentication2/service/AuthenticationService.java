package com.signup.auth.authentication2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.signup.auth.authentication2.config.EmailNotConfirmedException;
import com.signup.auth.authentication2.entity.User;
import com.signup.auth.authentication2.model.AuthenticationRequest;
import com.signup.auth.authentication2.model.AuthenticationResponse;
import com.signup.auth.authentication2.mail.MailSender;
import com.signup.auth.authentication2.model.RegisterRequest;
import com.signup.auth.authentication2.repository.UserRepository;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MailSender mailSender;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .registrationTime(new Date())
                .emailConfirmed(false)
                .build();
        var savedUser = repository.save(user);
        var expirationTime = LocalDateTime.now().plusMinutes(1);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken, expirationTime);
        String link = "http://localhost:8080/api/v1/auth/confirm?token=" + jwtToken;
        mailSender.send(request.getEmail(), buildVerificationEmail(savedUser.getFirstname(), link));
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!user.isEmailConfirmed()) {
            throw new EmailNotConfirmedException("Email has not been confirmed");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user);
        var existingToken = tokenRepository.findByToken(jwtToken);
        LocalDateTime expirationTime = existingToken.isPresent() ? existingToken.get().getExpirationTime() : null;
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken, expirationTime);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken, LocalDateTime expirationTime) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .expirationTime(expirationTime)
                .build();
        tokenRepository.save(token);
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

    public boolean confirmEmail(String token) {

        try {
            UserDetails userDetails = jwtService.extractUserDetails(token);

            User user = repository.findByEmail(userDetails.getUsername()).orElse(null);
            if (user == null) {
                return false;
            }
            Date expirationDate = jwtService.extractExpiration(token);

            LocalDateTime expirationTime = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            if (expirationTime == null && expirationTime.isBefore(LocalDateTime.now())) {
                return false;
            }
            user.setEmailConfirmed(true);
            repository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
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
