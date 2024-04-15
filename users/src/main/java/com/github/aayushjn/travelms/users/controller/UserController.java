package com.github.aayushjn.travelms.users.controller;

import com.github.aayushjn.travelms.common.exceptions.AlreadyExistsException;
import com.github.aayushjn.travelms.common.exceptions.DoesNotExistException;
import com.github.aayushjn.travelms.users.exceptions.AlreadyLoggedInException;
import com.github.aayushjn.travelms.users.exceptions.InvalidCredentialException;
import com.github.aayushjn.travelms.users.model.User;
import com.github.aayushjn.travelms.users.model.request.*;
import com.github.aayushjn.travelms.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.github.aayushjn.travelms.common.Constants.HEADER_AUTH_KEY;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponse> userSignup(@Valid @RequestBody UserSignUpRequest user) throws AlreadyExistsException {
        return new ResponseEntity<>(new UserSignUpResponse(service.userSignup(user)), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> userSignIn(@Valid @RequestBody UserLoginRequest user) throws DoesNotExistException, InvalidCredentialException, AlreadyLoggedInException {
        return new ResponseEntity<>(new UserLoginResponse(service.userLogin(user)), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> userLogout(@RequestHeader(HEADER_AUTH_KEY) String authKey) throws InvalidCredentialException {
        service.userLogout(authKey);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("")
    public ResponseEntity<Void> updateUser(@RequestHeader(HEADER_AUTH_KEY) String authKey,
                                             @Valid @RequestBody UserUpdateRequest user) throws DoesNotExistException, InvalidCredentialException {
        service.updateUser(authKey, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("")
    public ResponseEntity<User> getUser(@RequestHeader(HEADER_AUTH_KEY) String authKey) throws DoesNotExistException, InvalidCredentialException {
        return new ResponseEntity<>(service.getUser(authKey), HttpStatus.OK);
    }
}
