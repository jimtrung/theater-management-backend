package com.github.jimtrung.theater.view;

import com.github.jimtrung.theater.util.SessionTokenUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.UUID;

public class HomeController {
  private final HomeUI view;
  private final JPanel container;

  public HomeController(HomeUI view,  JPanel container) {
    this.view = view;
    this.container = container;

    view.getPanel1().addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent e) {
        handleOnOpen();
      }
    });

    view.getSignUpButton().addActionListener(_ -> show("signup"));
    view.getSignInButton().addActionListener(_ -> show("signin"));
    view.getSettingsButton().addActionListener(_ -> show("settings"));
  }

  private void handleOnOpen() {
    UUID userId;

    try {
      userId = SessionTokenUtil.loadAndDecodeToken();
    } catch (Exception e) {
      userId = null;
    }

    if (userId != null) show("signup");
  }

  private void show(String card) {
    CardLayout cl = (CardLayout) container.getLayout();
    cl.show(container, card);
  }
}
