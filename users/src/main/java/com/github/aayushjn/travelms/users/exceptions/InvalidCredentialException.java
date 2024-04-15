package com.github.aayushjn.travelms.users.exceptions;

public class InvalidCredentialException extends Exception {
    public InvalidCredentialException() {
        this("Invalid credentials provided");
    }

    public InvalidCredentialException(String message) {
        super(message);
    }
}
