package com.github.jimtrung.theater;

import com.github.jimtrung.theater.controller.UserController;
import com.github.jimtrung.theater.dao.Database;
import com.github.jimtrung.theater.dao.UserDAO;
import com.github.jimtrung.theater.util.SessionTokenUtil;
import com.github.jimtrung.theater.view.*;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class Main extends JFrame {
  public Main() {
    Database dtb = null;
    try {
      dtb = new Database();
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      System.exit(0);
    }
    UserDAO userDAO = new UserDAO(dtb.getConnection());
    UserController userController = new UserController(userDAO);

    setTitle("Theater Management");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);

    JPanel container = new JPanel(new CardLayout());

    HomeUI homeUI = new HomeUI();
    new HomeController(homeUI, container);
    container.add(homeUI.getPanel1(), "home");

    SignUpUI signUpUI = new SignUpUI();
    new SignUpController(signUpUI, container, userController);
    container.add(signUpUI.getPanel1(), "signup");

    SignInUI signInUI = new SignInUI();
    new SignInController(signInUI, container, userController);
    container.add(signInUI.getPanel1(), "signin");

    ProfileUI profileUI = new ProfileUI(container);
    container.add(profileUI.getPanel1(), "profile");

    setContentPane(container);
    setVisible(true);

    UUID userId;

    try {
      userId = SessionTokenUtil.loadAndDecodeToken();
    } catch (Exception e) {
      userId = null;
    }

    if (userId != null) {
      CardLayout cl = (CardLayout) container.getLayout();
      cl.show(container, "profile");
      container.revalidate();
      container.repaint();
    }
  }

  public static void main(String[] args) {
    new Main();
  }
}