package com.github.jimtrung.theater;

import com.github.jimtrung.theater.controller.UserController;
import com.github.jimtrung.theater.dao.Database;
import com.github.jimtrung.theater.dao.UserDAO;
import com.github.jimtrung.theater.view.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    Database dtb = null;
    try {
      dtb = new Database();
    } catch (Exception e) {
      System.out.println(e);
      System.exit(1);
    }

    UserDAO userDAO = new UserDAO(dtb.getConnection());
    UserController userController = new UserController(userDAO);

    StackPane root = new StackPane();
    ScreenController screenController = new ScreenController(root);

    // Home
    FXMLLoader homeLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/home.fxml")));
    screenController.addScreen("home", homeLoader);
    HomeController homeController = homeLoader.getController();
    homeController.setScreenController(screenController);

    // Sign Up
    FXMLLoader signUpLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/signup.fxml")));
    screenController.addScreen("signup", signUpLoader);
    SignUpController signUpController = signUpLoader.getController();
    signUpController.setScreenController(screenController);
    signUpController.setUserController(userController);

    // Sign In
    FXMLLoader signInLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/signin.fxml")));
    screenController.addScreen("signin", signInLoader);
    SignInController signInController = signInLoader.getController();
    signInController.setScreenController(screenController);
    signInController.setUserController(userController);

    // Profile
    FXMLLoader profileLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/profile.fxml")));
    screenController.addScreen("profile", profileLoader);
    ProfileController profileController = profileLoader.getController();
    profileController.setScreenController(screenController);
    profileController.setUserController(userController);

    // Start with home screen
    screenController.activate("home");

    Scene scene = new Scene(screenController.getRoot(), 1400, 700);
    stage.setTitle("Theater Management");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
