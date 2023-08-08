package com.signup.auth.authentication2.exception;

public class EmailNotConfirmedException extends RuntimeException{
    public EmailNotConfirmedException(String message) {
        super(message);
    }
}
