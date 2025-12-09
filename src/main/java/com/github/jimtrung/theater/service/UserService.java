package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.dto.TokenPair;
import com.github.jimtrung.theater.exception.user.InvalidCredentialsException;
import com.github.jimtrung.theater.exception.user.InvalidUserDataException;
import com.github.jimtrung.theater.exception.user.UserAlreadyExistsException;
import com.github.jimtrung.theater.model.User;
import com.github.jimtrung.theater.model.UserRole;
import com.github.jimtrung.theater.repository.UserRepository;
import com.github.jimtrung.theater.util.AuthTokenUtil;
import com.github.jimtrung.theater.util.EmailValidator;
import com.github.jimtrung.theater.util.TokenUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthTokenUtil authTokenUtil;
    private final TokenUtil tokenUtil;
    private final EmailValidator emailValidator;

    public UserService(UserRepository userRepository, AuthTokenUtil authTokenUtil, TokenUtil tokenUtil, EmailValidator emailValidator) {
        this.userRepository = userRepository;
        this.authTokenUtil = authTokenUtil;
        this.tokenUtil = tokenUtil;
        this.emailValidator = emailValidator;
    }

    public void signUp(User user) {
        if (user.getUsername().isEmpty()) throw new InvalidUserDataException("Username is empty");
        if (user.getEmail().isEmpty()) throw new InvalidUserDataException("Email is empty");
        if (user.getPassword().isEmpty()) throw new InvalidUserDataException("Password is empty");
        if (!emailValidator.isValidEmail(user.getEmail()))
            throw new InvalidUserDataException("Invalid email addresses");

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("User with this username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        String token = tokenUtil.generateToken();
        String passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        user.setPassword(passwordHash);
        user.setToken(token);
        user.setRole(UserRole.user);
        user.setVerified(false);
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());

        try {
            emailValidator.sendVerificationEmail(user.getEmail(), "http://localhost:8080/page/verify/" + token);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification email", e);
        }

        userRepository.save(user);
    }

    public TokenPair signIn(User user) {
        if (user.getUsername().isEmpty()) throw new InvalidUserDataException("Username is empty");
        if (user.getPassword().isEmpty()) throw new InvalidUserDataException("Password is empty");

        User existingUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("User does not exist"));

        if (!BCrypt.checkpw(user.getPassword(), existingUser.getPassword()))
            throw new InvalidCredentialsException("Wrong password");

        String refreshToken = authTokenUtil.generateRefreshToken(existingUser.getId());
        String accessToken = authTokenUtil.generateAccessToken(existingUser.getId());

        return new TokenPair(accessToken, refreshToken);
    }

    public User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("User does not exist"));
    }

    public String refresh(String refreshToken) {
        if (authTokenUtil.isTokenExpired(refreshToken)) {
            return null;
        }

        UUID userId = authTokenUtil.parseToken(refreshToken);
        return authTokenUtil.generateAccessToken(userId);
    }

    public boolean isAdmin(UUID userId) {
        User existingUser = getUser(userId);
        return existingUser.getRole() == UserRole.administrator;
    }

    public boolean checkUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
