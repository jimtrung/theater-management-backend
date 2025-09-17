package com.github.jimtrung.theater.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignInUI {
  private JTextField usernameField;
  private JTextField passwordField;
  private JButton signInButton;
  private JButton backButton;
  private JLabel passwordLabel;
  private JLabel usernameLabel;
  private JLabel titleLabel;
  private JPanel panel1;

  private JPanel container;

  public SignInUI(JPanel container) {
    this.container = container;

    backButton.addActionListener(e -> {
      CardLayout cl = (CardLayout) container.getLayout();
      cl.show(container, "home");
    });
  }

  public JPanel getPanel1() {
    return panel1;
  }
}
