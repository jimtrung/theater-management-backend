package com.github.jimtrung.theater.view;

import com.github.jimtrung.theater.util.SessionTokenUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.UUID;

public class HomeUI {
  private JPanel panel1;
  private JLabel titleLabel;
  private JButton signInButton;
  private JButton signUpButton;
  private JButton settingsButton;

  private final JPanel container;

  public HomeUI(JPanel container) {
    this.container = container;

    panel1.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

    panel1.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent e) {
        handleOnOpen();
      }
    });

    signUpButton.addActionListener(_ -> {
      // Open SignUpUI
      CardLayout cl = (CardLayout) container.getLayout();
      cl.show(container, "signup");
    });

    signInButton.addActionListener(_ -> {
      CardLayout cl = (CardLayout) container.getLayout();
      cl.show(container, "signin");
    });

    settingsButton.addActionListener(_ -> {
      CardLayout cl = (CardLayout) container.getLayout();
      cl.show(container, "settings");
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
