package com.github.jimtrung.theater.view;

import com.github.jimtrung.theater.controller.UserController;
import com.github.jimtrung.theater.model.User;
import com.github.jimtrung.theater.util.SessionTokenUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.UUID;

public class ProfileController {
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

    if (userId != null) {
      User userInfo = null;
      try {
        userInfo = userController.getUser(userId);
      } catch (Exception e) {
        System.out.println("Failed to fetch user: " + e.getMessage());
        System.exit(0);
      }

      usernameLabel.setText(userInfo.getUsername());
      emailLabel.setText(userInfo.getEmail());
      phoneNumberLabel.setText(userInfo.getPhoneNumber());
      passwordLabel.setText(userInfo.getPassword());
      verifiedLabel.setText(userInfo.getVerified().toString());
      createdAtLabel.setText(userInfo.getCreatedAt().toString());
    } else {
      screenController.activate("home");
    }
  }

  @FXML
  private TextField usernameLabel;

  @FXML
  private TextField emailLabel;

  @FXML
  private TextField phoneNumberLabel;

  @FXML
  private PasswordField passwordLabel;

  @FXML
  private TextField verifiedLabel;

  @FXML
  private TextField createdAtLabel;

  @FXML
  public void handleBackButton(ActionEvent event) {
    screenController.activate("home");
  }

  @FXML
  public void handleEditButton(ActionEvent event) {}
}
