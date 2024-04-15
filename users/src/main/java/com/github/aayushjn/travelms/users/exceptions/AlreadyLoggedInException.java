package com.github.aayushjn.travelms.users.exceptions;

public class AlreadyLoggedInException extends Exception {
    public AlreadyLoggedInException() {
        super();
    }

    public AlreadyLoggedInException(String message) { super(message); }

    public AlreadyLoggedInException(int userId) {
        this("User " + userId + " already logged in");
    }
}
