package com.github.jimtrung.theater.view;

import com.github.jimtrung.theater.controller.UserController;
import com.github.jimtrung.theater.dao.Database;
import com.github.jimtrung.theater.dao.UserDAO;
import com.github.jimtrung.theater.model.User;
import com.github.jimtrung.theater.util.SessionTokenUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.UUID;

public class SignUpUI {
  private JTextField usernameField;
  private JTextField emailField;
  private JPasswordField passwordField;
  private JButton signUpButton;
  private JButton backButton;
  private JPanel panel1;
  private JLabel titleLabel;
  private JLabel usernameLabel;
  private JLabel emailLabel;
  private JLabel passwordLabel;
  private JTextField phoneNumberField;
  private JLabel phoneNumberLabel;

  private JPanel container;

  public SignUpUI(JPanel container) {
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

    backButton.addActionListener(e -> {
      CardLayout cl = (CardLayout) container.getLayout();
      cl.show(container, "home");
    });

    signUpButton.addActionListener(e -> {
      User user = new User();
      user.setUsername(usernameField.getText());
      user.setEmail(emailField.getText());
      user.setPhoneNumber(phoneNumberField.getText());
      user.setPassword(new String(passwordField.getPassword()));

      try {
        userController.signUp(user);
      } catch (Exception ex) {
        System.out.println(ex);
        System.exit(0);
      }

      CardLayout cl = (CardLayout) container.getLayout();
      cl.show(container, "signin");
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
