package com.github.jimtrung.theater.view;

import com.github.jimtrung.theater.controller.UserController;
import com.github.jimtrung.theater.dao.Database;
import com.github.jimtrung.theater.dao.UserDAO;
import com.github.jimtrung.theater.model.User;
import com.github.jimtrung.theater.util.SessionTokenUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.UUID;

public class SignInUI {
  private JTextField usernameField;
  private JButton signInButton;
  private JButton backButton;
  private JLabel passwordLabel;
  private JLabel usernameLabel;
  private JLabel titleLabel;
  private JPanel panel1;
  private JPasswordField passwordField;

  private final JPanel container;

  public SignInUI(JPanel container) {
    this.container = container;

    panel1.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

    panel1.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent e) {
        handleOnOpen();
      }
    });

    Database dtb = null;
    try {
      dtb = new Database();
    } catch (Exception e) {
      System.out.println(e);
      System.exit(0);
    }

    UserDAO userDAO = new UserDAO(dtb.getConnection());
    UserController userController = new UserController(userDAO);

    backButton.addActionListener(_ -> {
      CardLayout cl = (CardLayout) container.getLayout();
      cl.show(container, "home");
    });

    signInButton.addActionListener(_ -> {
      User user = new User();
      user.setUsername(usernameField.getText());
      user.setPassword(new String(passwordField.getPassword()));

      try {
        userController.signIn(user);
      } catch (Exception ex) {
        System.out.println(ex);
        System.exit(0);
      }

      CardLayout cl = (CardLayout) container.getLayout();
      cl.show(container, "profile");
    });
  }

  public JPanel getPanel1() {
    return panel1;
  }

  private void handleOnOpen() {
    UUID userId;

    try {
      userId = SessionTokenUtil.loadAndDecodeToken();
    } catch (Exception e) {
      userId = null;
    }

    if (userId != null) {
      CardLayout cl = (CardLayout) container.getLayout();
      cl.show(container, "profile");
    }
  }
}
