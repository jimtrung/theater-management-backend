package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.dao.UserDAO;
import com.github.jimtrung.theater.dto.TokenPair;
import com.github.jimtrung.theater.exception.system.DatabaseOperationException;
import com.github.jimtrung.theater.exception.user.InvalidCredentialsException;
import com.github.jimtrung.theater.exception.user.InvalidUserDataException;
import com.github.jimtrung.theater.exception.user.MismatchedAuthProviderException;
import com.github.jimtrung.theater.exception.user.UserAlreadyExistsException;
import com.github.jimtrung.theater.model.Provider;
import com.github.jimtrung.theater.model.User;
import com.github.jimtrung.theater.model.UserRole;
import com.github.jimtrung.theater.util.AuthTokenUtil;
import com.github.jimtrung.theater.util.EmailValidator;
import com.github.jimtrung.theater.util.OTPUtil;
import com.github.jimtrung.theater.util.TokenUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.UUID;

@Service
public class UserService {
    private final UserDAO userDAO;
    private final AuthTokenUtil authTokenUtil;
    private final TokenUtil tokenUtil;
    private final OTPUtil otpUtil;
    private final EmailValidator emailValidator;

    public UserService(UserDAO userDAO, AuthTokenUtil authTokenUtil, TokenUtil tokenUtil, OTPUtil otpUtil, EmailValidator emailValidator) {
        this.userDAO = userDAO;
        this.authTokenUtil = authTokenUtil;
        this.tokenUtil = tokenUtil;
        this.otpUtil = otpUtil;
        this.emailValidator = emailValidator;
    }

    public void signUp(User user) {
        if (user.getUsername().isEmpty()) throw new InvalidUserDataException("Username is empty");
        if (user.getEmail().isEmpty()) throw new InvalidUserDataException("Email is empty");
        if (user.getPhoneNumber().isEmpty()) throw new InvalidUserDataException("Phone number is empty");
        if (user.getPassword().isEmpty()) throw new InvalidUserDataException("Password is empty");
        if (!emailValidator.isValidEmail(user.getEmail()))
            throw new InvalidUserDataException("Invalid email addresses");

        User existingUser = null;
        try {
            existingUser = userDAO.getByUsernameOrEmailOrPhoneNumber(user.getUsername(), user.getEmail(), user.getPhoneNumber());
        } catch (Exception ignored) {
        }
        if (existingUser != null) throw new UserAlreadyExistsException("User already exists");

        String token = tokenUtil.generateToken();
        Integer otp = otpUtil.generateOTP();
        String passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        user.setPassword(passwordHash);
        user.setToken(token);
        user.setOtp(otp);
        user.setRole(UserRole.user);
        user.setProvider(Provider.local);
        user.setVerified(false);

        try {
            emailValidator.sendVerificationEmail(user.getEmail(), "http://localhost:8080/page/verify/" + token);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification email", e);
        }

        userDAO.insert(user);
    }

    public TokenPair signIn(User user) {
        if (user.getUsername().isEmpty()) throw new InvalidUserDataException("Username is empty");
        if (user.getPassword().isEmpty()) throw new InvalidUserDataException("Password is empty");

        User existingUser = userDAO.getByField("username", user.getUsername());
        if (existingUser == null) throw new InvalidCredentialsException("User does not exist");

        if (!BCrypt.checkpw(user.getPassword(), existingUser.getPassword()))
            throw new InvalidCredentialsException("Wrong password");

        String refreshToken = authTokenUtil.generateRefreshToken(existingUser.getId());
        String accessToken = authTokenUtil.generateAccessToken(existingUser.getId());

        return new TokenPair(accessToken, refreshToken);
    }

    public User getUser(UUID userId) {
        return userDAO.getByField("id", userId);
    }

    public String refresh(String refreshToken) {
        if (authTokenUtil.isTokenExpired(refreshToken)) {
            return null;
        }

        UUID userId = authTokenUtil.parseToken(refreshToken);
        return authTokenUtil.generateAccessToken(userId);
    }

    public boolean isAdmin(UUID userId) {
        User existingUser = userDAO.getByField("id", userId);
        if (existingUser == null) throw new InvalidCredentialsException("User does not exist");
        return existingUser.getRole() == UserRole.administrator;
    }
}
