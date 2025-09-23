package com.github.jimtrung.theater.view;

import javax.swing.*;
import java.awt.*;

public class HomeUI {
  private JPanel panel1;
  private JLabel titleLabel;
  private JButton signInButton;
  private JButton signUpButton;
  private JButton settingsButton;

  public HomeUI() {
    panel1.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
  }

  public JPanel getPanel1() {
    return panel1;
  }

  public JButton getSignInButton() {
    return signInButton;
  }

  public JButton getSignUpButton() {
    return signUpButton;
  }

  public JButton getSettingsButton() {
    return settingsButton;
  }
}
