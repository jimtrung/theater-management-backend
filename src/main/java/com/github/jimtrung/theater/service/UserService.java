package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.dao.UserDAO;
import com.github.jimtrung.theater.dto.TokenPair;
import com.github.jimtrung.theater.model.Provider;
import com.github.jimtrung.theater.model.User;
import com.github.jimtrung.theater.model.UserRole;
import com.github.jimtrung.theater.util.AuthTokenUtil;
import com.github.jimtrung.theater.util.EmailValidator;
import com.github.jimtrung.theater.util.OTPUtil;
import com.github.jimtrung.theater.util.TokenUtil;
import org.apache.coyote.BadRequestException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    if (user.getUsername().isEmpty()) throw new IllegalArgumentException("Username is empty");
    if (user.getEmail().isEmpty()) throw new IllegalArgumentException("Email is empty");
    if (user.getPhoneNumber().isEmpty()) throw new IllegalArgumentException("Phone number is empty");
    if (user.getPassword().isEmpty()) throw new IllegalArgumentException("Password is empty");
    if (!emailValidator.isValidEmail(user.getEmail())) throw new IllegalArgumentException("Invalid email addresses");

    User existingUser = null;
    try { existingUser = userDAO.getByField("username", user.getUsername()); } catch (RuntimeException ignored) {}
    try { existingUser = userDAO.getByField("email", user.getEmail()); } catch (RuntimeException ignored) {}
    try { existingUser = userDAO.getByField("phone_number", user.getPhoneNumber()); } catch (RuntimeException ignored) {}

    if (existingUser != null) throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");

    String token = tokenUtil.generateToken();
    Integer otp = otpUtil.generateOTP();
    String passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

    user.setPassword(passwordHash);
    user.setToken(token);
    user.setOtp(otp);
    user.setRole(UserRole.USER);
    user.setProvider(Provider.LOCAL);
    user.setVerified(false);

    try {
      emailValidator.sendVerificationEmail(user.getEmail(), "http://localhost:8080/page/verify/" + token);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
    }

    userDAO.insert(user);
  }

  public TokenPair signIn(User user) {
    if (user.getUsername().isEmpty()) throw new IllegalArgumentException("Username is empty");
    if (user.getPassword().isEmpty()) throw new IllegalArgumentException("Password is empty");

    User existingUser = userDAO.getByField("username", user.getUsername());
    if (existingUser == null) throw new IllegalArgumentException("User does not exist");
    if (!BCrypt.checkpw(user.getPassword(), existingUser.getPassword())) throw new IllegalArgumentException("Wrong password");

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
}
