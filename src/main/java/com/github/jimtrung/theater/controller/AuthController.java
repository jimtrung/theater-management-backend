package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.dto.RefreshRequest;
import com.github.jimtrung.theater.dto.SignUpRequest;
import com.github.jimtrung.theater.dto.TokenPair;
import com.github.jimtrung.theater.model.User;
import com.github.jimtrung.theater.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/signup")
  public ResponseEntity<String> signUp(@RequestBody SignUpRequest req) {
    User newUser = new User();
    newUser.setUsername(req.username());
    newUser.setEmail(req.email());
    newUser.setPhoneNumber(req.phoneNumber());
    newUser.setPassword(req.password());

    userService.signUp(newUser);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body("User signed up successfully");
  }

  @PostMapping("/signin")
  public ResponseEntity<TokenPair> signIn(@RequestBody SignUpRequest req) {
    User user = new User();
    user.setUsername(req.username());
    user.setPassword(req.password());

    TokenPair tokenPair = userService.signIn(user);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(tokenPair);
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> refresh(@RequestBody RefreshRequest refreshRequest) {
    String newAccessToken = userService.refresh(refreshRequest.refreshToken());

    if (newAccessToken == null) {
      return ResponseEntity
          .status(401)
          .body("Expired refresh token");
    }

    return ResponseEntity
        .status(201)
        .body(newAccessToken);
  }
}
