package com.github.aayushjn.travelms.users.service;

import com.github.aayushjn.travelms.common.exceptions.AlreadyExistsException;
import com.github.aayushjn.travelms.common.exceptions.DoesNotExistException;
import com.github.aayushjn.travelms.users.exceptions.AlreadyLoggedInException;
import com.github.aayushjn.travelms.users.exceptions.InvalidCredentialException;
import com.github.aayushjn.travelms.users.model.Session;
import com.github.aayushjn.travelms.users.model.User;
import com.github.aayushjn.travelms.users.model.request.UserLoginRequest;
import com.github.aayushjn.travelms.users.model.request.UserSignUpRequest;
import com.github.aayushjn.travelms.users.model.request.UserUpdateRequest;
import com.github.aayushjn.travelms.users.repository.SessionRepository;
import com.github.aayushjn.travelms.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepo;

    private final SessionRepository sessionRepo;

    private final ApplicationContext context;

    public int userSignup(UserSignUpRequest userSignUpRequest) throws AlreadyExistsException {
        Optional<User> optional = userRepo.findByEmail(userSignUpRequest.email());
        if (optional.isPresent()) {
            throw new AlreadyExistsException("User with email '" + userSignUpRequest.email() + "' already registered");
        }

        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        User user = new User();
        user.setName(userSignUpRequest.name());
        user.setEmail(userSignUpRequest.email());
        user.setMobile(userSignUpRequest.mobile());
        user.setAddress(userSignUpRequest.address());
        user.setPassword(encoder.encode(userSignUpRequest.password()));

        return userRepo.save(user).getUserId();
    }

    public String userLogin(UserLoginRequest userLoginRequest) throws InvalidCredentialException, DoesNotExistException, AlreadyLoggedInException {
        User user = userRepo.findByEmail(userLoginRequest.email())
                .orElseThrow(() -> new DoesNotExistException("User with email '" + userLoginRequest.email() + "' not found"));
        if (sessionRepo.findByUserId(user.getUserId()).isPresent()) {
            throw new AlreadyLoggedInException(user.getUserId());
        }

        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        if (!(user.getEmail().equals(userLoginRequest.email()) && encoder.matches(userLoginRequest.password(), user.getPassword()))) {
            throw new InvalidCredentialException();
        }

        StringKeyGenerator keyGenerator = context.getBean(StringKeyGenerator.class);

        Session session = new Session();
        String authKey = keyGenerator.generateKey();
        session.setUser(user);
        session.setAuthKey(authKey);
        session.setSessionStartTime(LocalDateTime.now());
        sessionRepo.save(session);

        return authKey;
    }

    public void userLogout(String authKey) throws InvalidCredentialException {
        Session session = sessionRepo.findByAuthKey(authKey)
                .orElseThrow(InvalidCredentialException::new);
        sessionRepo.delete(session);
    }

    public void updateUser(String authKey, UserUpdateRequest userUpdateRequest) throws DoesNotExistException, InvalidCredentialException {
        Session session = sessionRepo.findByAuthKey(authKey)
                .orElseThrow(InvalidCredentialException::new);

        User dbUser = userRepo.findByUserId(session.getUser().getUserId())
                .orElseThrow(() -> new DoesNotExistException("User " + session.getUser().getUserId() + " not found"));
        dbUser.setName(userUpdateRequest.name());
        dbUser.setAddress(userUpdateRequest.address());
        dbUser.setMobile(userUpdateRequest.mobile());
        userRepo.save(dbUser);
    }

    public User getUser(String authKey) throws DoesNotExistException, InvalidCredentialException {
        Session session = sessionRepo.findByAuthKey(authKey)
                .orElseThrow(InvalidCredentialException::new);
        return userRepo.findByUserId(session.getUser().getUserId())
                .orElseThrow(() -> new DoesNotExistException("User " + session.getUser().getUserId() + " does not exist"));
    }
}
