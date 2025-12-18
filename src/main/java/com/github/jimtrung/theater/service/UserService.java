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
        if (user.getUsername().isEmpty()) throw new InvalidUserDataException("Tên đăng nhập không được để trống");
        if (user.getEmail().isEmpty()) throw new InvalidUserDataException("Email không được để trống");
        if (user.getPassword().isEmpty()) throw new InvalidUserDataException("Mật khẩu không được để trống");
        if (!emailValidator.isValidEmail(user.getEmail()))
            throw new InvalidUserDataException("Địa chỉ email không hợp lệ");

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Người dùng với tên đăng nhập này đã tồn tại");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Người dùng với email này đã tồn tại");
        }

        String token = tokenUtil.generateToken();
        String passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        user.setPassword(passwordHash);
        user.setToken(token);
        user.setRole(UserRole.user);
        user.setVerified(false);
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());

        // TODO: Async this
        try {
            emailValidator.sendVerificationEmail(user.getEmail(), "http://localhost:8080/auth/verify?token=" + token);
        } catch (Exception e) {
            throw new RuntimeException("Gửi email xác thực thất bại", e);
        }

        userRepository.save(user);
    }

    public TokenPair signIn(User user) {
        if (user.getUsername().isEmpty()) throw new InvalidUserDataException("Tên đăng nhập không được để trống");
        if (user.getPassword().isEmpty()) throw new InvalidUserDataException("Mật khẩu không được để trống");

        User existingUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Người dùng không tồn tại"));

        if (!BCrypt.checkpw(user.getPassword(), existingUser.getPassword()))
            throw new InvalidCredentialsException("Sai mật khẩu");

        String refreshToken = authTokenUtil.generateRefreshToken(existingUser.getId());
        String accessToken = authTokenUtil.generateAccessToken(existingUser.getId());

        return new TokenPair(accessToken, refreshToken);
    }

    public User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("Người dùng không tồn tại"));
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

    public void verifyUser(String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new InvalidUserDataException("Token không hợp lệ hoặc đã hết hạn"));

        user.setVerified(true);
        userRepository.save(user);
    }
}
