package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.dao.UserDAO;
import com.github.jimtrung.theater.model.Provider;
import com.github.jimtrung.theater.model.User;
import com.github.jimtrung.theater.model.UserRole;
import com.github.jimtrung.theater.util.OTPUtil;
import com.github.jimtrung.theater.util.SessionTokenUtil;
import com.github.jimtrung.theater.util.TokenUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
  private final UserDAO userDAO;
  private final SessionTokenUtil sessionTokenUtil;

  public UserService(UserDAO userDAO, SessionTokenUtil sessionTokenUtil) {
    this.userDAO = userDAO;
    this.sessionTokenUtil = sessionTokenUtil;
  }

  public void signUp(User user) {
    if (user.getUsername().isEmpty()) throw new IllegalArgumentException("Username is empty");
    if (user.getEmail().isEmpty()) throw new IllegalArgumentException("Email is empty");
    if (user.getPhoneNumber().isEmpty()) throw new IllegalArgumentException("Phone number is empty");
    if (user.getPassword().isEmpty()) throw new IllegalArgumentException("Password is empty");

    User existingUser = null;
    try { existingUser = userDAO.getByField("username", user.getUsername()); } catch (RuntimeException ignored) {}
    try { existingUser = userDAO.getByField("email", user.getEmail()); } catch (RuntimeException ignored) {}
    try { existingUser = userDAO.getByField("phoneNumber", user.getPhoneNumber()); } catch (RuntimeException ignored) {}

    if (existingUser != null) throw new IllegalArgumentException("User already exists");

    String passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
    user.setPassword(passwordHash);

    user.setToken(TokenUtil.generateToken());
    user.setOtp(OTPUtil.generateOTP());
    user.setRole(UserRole.USER);
    user.setProvider(Provider.LOCAL);
    user.setVerified(false);

    userDAO.insert(user);
  }

  public void signIn(User user) {
    if (user.getUsername().isEmpty()) throw new IllegalArgumentException("Username is empty");
    if (user.getPassword().isEmpty()) throw new IllegalArgumentException("Password is empty");

    User existingUser = userDAO.getByField("username", user.getUsername());
    if (!BCrypt.checkpw(user.getPassword(), existingUser.getPassword())) throw new IllegalArgumentException("Wrong password");

    sessionTokenUtil.generateToken(existingUser.getId());
  }

  public User getUser(UUID userId) {
    return userDAO.getByField("id", userId);
  }
}
