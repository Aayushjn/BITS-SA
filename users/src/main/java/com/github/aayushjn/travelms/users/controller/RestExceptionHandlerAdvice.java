package com.github.aayushjn.travelms.users.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.github.aayushjn.travelms.common.exceptions.DoesNotExistException;
import com.github.aayushjn.travelms.users.exceptions.AlreadyLoggedInException;
import com.github.aayushjn.travelms.users.exceptions.InvalidCredentialException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandlerAdvice {
    @ExceptionHandler(JsonParseException.class)
    ProblemDetail handleJsonParseException(JsonParseException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(DoesNotExistException.class)
    ProblemDetail handleDoesNotExistException(DoesNotExistException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidCredentialException.class)
    ProblemDetail handleInvalidCredentialException(InvalidCredentialException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(AlreadyLoggedInException.class)
    ProblemDetail handleAlreadyLoggedInException(AlreadyLoggedInException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
