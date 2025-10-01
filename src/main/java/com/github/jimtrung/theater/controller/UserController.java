package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.model.User;
import com.github.jimtrung.theater.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
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
  public ResponseEntity<String> signIn(@RequestBody User user) {
    userService.signIn(user);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body("User signed in successfully");
  }

  @GetMapping("/{id}")
  public User getUser(@PathVariable UUID id) throws Exception {
    return userService.getUser(id);
  }
}
