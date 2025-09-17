package com.github.jimtrung.theater.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeUI {
  private JPanel panel1;
  private JLabel titleLabel;
  private JButton signInButton;
  private JButton signUpButton;
  private JButton settingsButton;

  private JPanel container;

  public HomeUI(JPanel container) {
    this.container = container;

    signUpButton.addActionListener(e -> {
      // Open SignUpUI
      CardLayout cl = (CardLayout) container.getLayout();
      cl.show(container, "signup");
    });

    signInButton.addActionListener(e -> {
      CardLayout cl = (CardLayout) container.getLayout();
      cl.show(container, "signin");
    });

    settingsButton.addActionListener(e -> {
      CardLayout cl = (CardLayout) container.getLayout();
      cl.show(container, "settings");
    });
  }

  public JPanel getPanel1() {
    return panel1;
  }
}
