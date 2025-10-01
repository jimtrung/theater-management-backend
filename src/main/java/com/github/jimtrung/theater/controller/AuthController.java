package com.github.jimtrung.theater.controller;

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
  public ResponseEntity<String> signUp(@RequestBody User user) {
    userService.signUp(user);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body("User signed up successfully");
  }

  @PostMapping("/signin")
  public ResponseEntity<TokenPair> signIn(@RequestBody User user) {
    TokenPair tokenPair = userService.signIn(user);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(tokenPair);
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> refresh(@RequestBody String refreshToken) {
    String newAccessToken = userService.refresh(refreshToken);

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
