package com.signup.auth.authentication2.authenticate.controller;

import com.signup.auth.authentication2.authenticate.exception.CustomException;
import com.signup.auth.authentication2.authenticate.exception.EmailNotConfirmedException;
import com.signup.auth.authentication2.authenticate.exception.TokenExpiredException;
import com.signup.auth.authentication2.authenticate.exception.TokenNotFoundException;
import com.signup.auth.authentication2.authenticate.model.ErrorResponse;
import com.signup.auth.authentication2.authenticate.model.forgotmodel.ChangePasswordRequest;
import com.signup.auth.authentication2.authenticate.model.forgotmodel.ChangePasswordResponse;
import com.signup.auth.authentication2.authenticate.model.forgotmodel.ForgotPasswordRequest;
import com.signup.auth.authentication2.authenticate.model.forgotmodel.ForgotPasswordResponse;
import com.signup.auth.authentication2.authenticate.model.loginmodel.AuthenticationRequest;
import com.signup.auth.authentication2.authenticate.model.loginmodel.AuthenticationResponse;
import com.signup.auth.authentication2.authenticate.model.registermodel.RegisterRequest;
import com.signup.auth.authentication2.authenticate.service.AuthenticationService;
import com.signup.auth.authentication2.authenticate.model.registermodel.RegisterRequestPhone;


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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED) .body(new ErrorResponse("error","Account not found, please registration with email or phone number"));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("error","Invalid Email, Phone Number and Password"));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> requestPasswordReset(@RequestBody ForgotPasswordRequest request) {
        ForgotPasswordResponse response = service.requestPasswordReset(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        ChangePasswordResponse response = service.forgotChangePassword(request);
        return ResponseEntity.ok(response);
    }
}
