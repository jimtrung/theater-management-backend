package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.dao.UserDAO;
import com.github.jimtrung.theater.model.Provider;
import com.github.jimtrung.theater.model.User;
import com.github.jimtrung.theater.model.UserRole;
import com.github.jimtrung.theater.util.OTPUtil;
import com.github.jimtrung.theater.util.SessionTokenUtil;
import com.github.jimtrung.theater.util.TokenUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Time;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserController {
  /* Attribute */
  private UserDAO userDAO;

  /* Constructor */
  public UserController(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  /* Functions */
  public void signUp(User user) throws Exception {
    // Check if fields are empty
    if (user.getUsername().isEmpty()) {
      throw new Exception("Username is empty");
    }

    if (user.getEmail().isEmpty()) {
      throw new Exception("Email is empty");
    }

    if (user.getPhoneNumber().isEmpty()) {
      throw new Exception("Phone number is empty");
    }

    if (user.getPassword().isEmpty()) {
      throw new Exception("Password is empty");
    }

    // Kiểm tra kiểu dữ liệu (Kiểm tra ở UI)

    // Kiểm tra người dùng tồn tại (email, username, sdt)
    User existingUser = null;
    try {
      existingUser = userDAO.getByField("username", user.getUsername());
    } catch (RuntimeException e) {
      // Do nothing
    }

    try {
      existingUser = userDAO.getByField("email", user.getEmail());
    } catch (RuntimeException e) {
      // Do nothing
    }

    try {
      existingUser = userDAO.getByField("phoneNumber", user.getPhoneNumber());
    } catch (RuntimeException e) {
      // Do nothing
    }

    if (existingUser != null) {
      throw new Exception("User already exists");
    }

    // Băm mật khẩu
    String passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
    user.setPassword(passwordHash);

    // Tạo token xác nhận email
    user.setToken(TokenUtil.generateToken());

    // OTP xác nhận sdt
    user.setOtp(OTPUtil.generateOTP());

    // Thêm dữ liệu khác
    user.setRole(UserRole.USER);
    user.setProvider(Provider.LOCAL);
    user.setVerified(false);

    // Thêm người dùng vào database
    userDAO.insert(user);

    // Lấy thông tin người dùng
    User userData = userDAO.getByField("username", user.getUsername());

    // Gửi email, OTP

    // Tạo thread để xóa token và otp sau 5 mins
    // Create a single-thread executor
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    executorService.submit(() -> {
      try {
        Thread.sleep(300_000); // 5 minutes
        userDAO.updateByField(userData.getId(), "token", "");
        userDAO.updateByField(userData.getId(), "otp", null);
        System.out.println("Email verification token and OTP has been removed successfully");
      } catch (Exception e) {
        System.out.println("Task threw exception: " + e.getMessage());
        e.printStackTrace();
      }
    });

    executorService.shutdown();
  }

  public void signIn(User user) throws Exception {
    // Check if fields are empty
    if (user.getUsername().isEmpty()) {
      throw new Exception("Username is empty");
    }

    if (user.getPassword().isEmpty()) {
      throw new Exception("Password is empty");
    }

    // Get user data based on username
    User existingUser = userDAO.getByField("username", user.getUsername());

    // Compare password
    if (!BCrypt.checkpw(user.getPassword(), existingUser.getPassword())) {
      throw new Exception("Wrong password");
    }

    // Create a session token
    SessionTokenUtil.generateAndStoreToken(existingUser.getId());
  }

  public User getUser(UUID userId) throws Exception {
    return userDAO.getByField("id", userId);
  }
}
