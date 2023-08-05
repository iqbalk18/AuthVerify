package com.signup.auth.authentication2.controller;

import com.signup.auth.authentication2.model.AuthenticationRequest;
import com.signup.auth.authentication2.model.AuthenticationResponse;
import com.signup.auth.authentication2.model.RegisterRequest;
import com.signup.auth.authentication2.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
