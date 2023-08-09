package com.signup.auth.authentication2.controller;

import com.signup.auth.authentication2.exception.CustomException;
import com.signup.auth.authentication2.exception.EmailNotConfirmedException;
import com.signup.auth.authentication2.exception.TokenExpiredException;
import com.signup.auth.authentication2.exception.TokenNotFoundException;
import com.signup.auth.authentication2.model.*;
import com.signup.auth.authentication2.service.AuthenticationService;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final AuthenticationService service;

    @PostMapping("/register/mail")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/register/phone")
    public ResponseEntity<AuthenticationResponse> registerPhone(@RequestBody RegisterRequestPhone requestPhone) {
        return ResponseEntity.ok(service.registerWithPhone(requestPhone));
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED) .body(new ErrorResponse("error","Account not found, please registration!!!"));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("error","Invalid Email and Password"));
        }
    }
}
