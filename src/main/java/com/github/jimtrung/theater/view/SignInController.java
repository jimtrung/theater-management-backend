package com.github.jimtrung.theater.view;

import com.github.jimtrung.theater.controller.UserController;
import com.github.jimtrung.theater.model.User;
import com.github.jimtrung.theater.util.SessionTokenUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.UUID;

public class SignInController {
  private final SignInUI view;
  private final JPanel container;
  private final UserController userController;

  public SignInController(SignInUI view, JPanel container, UserController userController) {
    this.view = view;
    this.container = container;
    this.userController = userController;

    view.getPanel1().addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent e) {
        handleOnOpen();
      }
    });

    view.getBackButton().addActionListener(_ -> show("home"));
    view.getSignInButton().addActionListener(_ -> handleSignIn());
  }

  private void handleOnOpen() {
    UUID userId;
    try {
      userId = SessionTokenUtil.loadAndDecodeToken();
    } catch (Exception e) {
      userId = null;
    }
    if (userId != null) show("home");
  }

  public void handleSignIn() {
    User user = new User();
    user.setUsername(view.getUsernameField().getText());
    user.setPassword(new String(view.getPasswordField().getPassword()));

    try {
      userController.signIn(user);
    } catch (Exception ex) {
      System.out.println(ex);
      System.exit(0);
    }

    CardLayout cl = (CardLayout) container.getLayout();
    cl.show(container, "profile");
  }

  private void show(String card) {
    CardLayout cl = (CardLayout) container.getLayout();
    cl.show(container, card);
  }
}
