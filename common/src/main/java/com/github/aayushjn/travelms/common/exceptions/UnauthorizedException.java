package com.github.aayushjn.travelms.common.exceptions;

public class UnauthorizedException extends Exception {
    public UnauthorizedException() {
        this("Unauthorized");
    }

    public UnauthorizedException(String message) { super(message); }
}
