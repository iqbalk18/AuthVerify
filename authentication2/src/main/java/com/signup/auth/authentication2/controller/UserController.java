package com.signup.auth.authentication2.controller;

import com.signup.auth.authentication2.config.CustomException;
import com.signup.auth.authentication2.config.EmailNotConfirmedException;
import com.signup.auth.authentication2.config.TokenExpiredException;
import com.signup.auth.authentication2.config.TokenNotFoundException;
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
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @GetMapping(path = "confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        try {
            service.confirmToken(token);
            return ResponseEntity.ok("User registration confirmed successfully.");
        } catch (TokenExpiredException e) {
            return ResponseEntity.badRequest().body("Token has expired. Please register again.");
        } catch (TokenNotFoundException e) {
            return ResponseEntity.badRequest().body("Invalid token. Please check your email for the correct link.");
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body("Some other error occurred.");
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = service.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (EmailNotConfirmedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED) .body("Email has not been confirmed");
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Email and Password");
        }
    }
}
