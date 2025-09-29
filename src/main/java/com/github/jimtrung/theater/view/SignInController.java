package com.github.jimtrung.theater.view;

import com.github.jimtrung.theater.controller.UserController;
import com.github.jimtrung.theater.model.User;
import com.github.jimtrung.theater.util.SessionTokenUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.UUID;

public class SignInController {
  private ScreenController screenController;
  private UserController userController;

  public void setScreenController(ScreenController screenController) {
    this.screenController = screenController;
  }

  public void setUserController(UserController userController) {
    this.userController = userController;
  }

  public void handleOnOpen() {
    UUID userId;
    try {
      userId = SessionTokenUtil.loadAndDecodeToken();
    } catch (Exception e) {
      userId = null;
    }
    if (userId != null) screenController.activate("profile");
  }

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  public void handleBackButton(ActionEvent event) {
    screenController.activate("home");
  }

  @FXML
  public void handleSignInButton(ActionEvent event) {
    User user = new User();
    user.setUsername(usernameField.getText());
    user.setPassword(passwordField.getText());

    try {
      userController.signIn(user);
    } catch (Exception e) {
      throw new RuntimeException("Failed to sign in");
    }

    screenController.activate("profile");
  }
}
