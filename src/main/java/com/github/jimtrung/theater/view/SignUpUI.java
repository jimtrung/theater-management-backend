package com.github.jimtrung.theater.view;

import javax.swing.*;
import java.awt.*;

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

  public SignUpUI() {
    panel1.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
  }

  public JPanel getPanel1() {
    return panel1;
  }

  public JButton getBackButton() {
    return backButton;
  }

  public JButton getSignUpButton() {
    return signUpButton;
  }

  public JTextField getUsernameField() {
    return usernameField;
  }

  public JTextField getEmailField() {
    return emailField;
  }

  public JTextField getPhoneNumberField() {
    return phoneNumberField;
  }

  public JPasswordField getPasswordField() {
    return passwordField;
  }
}
