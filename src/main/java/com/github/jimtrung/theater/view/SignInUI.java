package com.github.jimtrung.theater.view;

import javax.swing.*;
import java.awt.*;

public class SignInUI {
  private JTextField usernameField;
  private JButton signInButton;
  private JButton backButton;
  private JLabel passwordLabel;
  private JLabel usernameLabel;
  private JLabel titleLabel;
  private JPanel panel1;
  private JPasswordField passwordField;

  public SignInUI() {
    panel1.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
  }

  public JPanel getPanel1() {
    return panel1;
  }

  public JButton getBackButton() {
    return backButton;
  }

  public JButton getSignInButton() {
    return signInButton;
  }

  public JTextField getUsernameField() {
    return usernameField;
  }

  public JPasswordField getPasswordField() {
    return passwordField;
  }
}
