package com.signup.auth.authentication2.controller;

import com.signup.auth.authentication2.config.EmailNotConfirmedException;
import com.signup.auth.authentication2.model.AuthenticationRequest;
import com.signup.auth.authentication2.model.AuthenticationResponse;
import com.signup.auth.authentication2.model.RegisterRequest;
import com.signup.auth.authentication2.service.AuthenticationService;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam String token) {
        boolean isConfirmed = service.confirmEmail(token);
        if (isConfirmed) {
            return ResponseEntity.ok("Email confirmed successfully. You can now login.");
        } else {
            return ResponseEntity.badRequest().body("Invalid token or token expired.");
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = service.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (EmailNotConfirmedException ex) {
            return ResponseEntity.badRequest().body("Email has not been confirmed");
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Email and Password");
        }
    }
}
