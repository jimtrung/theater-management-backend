package com.github.jimtrung.theater.view;

import com.github.jimtrung.theater.util.SessionTokenUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.UUID;

public class HomeController {
  private ScreenController screenController;

  public void setScreenController(ScreenController screenController) {
    this.screenController = screenController;
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
  private Label titleLabel;

  @FXML
  private Button signupButton;

  @FXML
  private Button signinButton;

  @FXML
  private Button settingsButton;

  @FXML
  public void handleSignUpButton(ActionEvent event) {
    screenController.activate("signup");
  }

  @FXML
  public void handleSignInButton(ActionEvent event) {
    screenController.activate("signin");
  }
}
