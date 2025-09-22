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

public class ProfileUI {
  private JLabel titleLabel;
  private JLabel idFieldLabel;
  private JLabel idValueLabel;
  private JLabel usernameFieldLabel;
  private JLabel usernameValueLabel;
  private JLabel emailFieldLabel;
  private JLabel emailValueLabel;
  private JLabel phoneNumberFieldLabel;
  private JLabel phoneNumberValueLabel;
  private JLabel verifiedFieldLabel;
  private JLabel verifiedValueLabel;
  private JLabel createdAtFieldLabel;
  private JLabel createdAtValueLabel;
  private JPanel panel1;

  private final JPanel container;

  public ProfileUI(JPanel container) {
    this.container = container;

    panel1.setBorder(BorderFactory.createEmptyBorder(10, 80, 20, 80));

    panel1.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent e) {
        handleOnOpen();
      }
    });
  }

  public JPanel getPanel1() {
    return panel1;
  }

  private void handleOnOpen() {
    Database dtb = null;
    try {
      dtb = new Database();
    } catch (Exception e) {
      System.out.println(e);
      System.exit(0);
    }

    UserDAO userDAO = new UserDAO(dtb.getConnection());
    UserController userController = new UserController(userDAO);

    UUID userId;

    try {
      userId = SessionTokenUtil.loadAndDecodeToken();
    } catch (Exception e) {
      userId = null;
    }

    if (userId != null) {
      User userInfo = null;
      try {
        userInfo = userController.getUser(userId);
      } catch (Exception e) {
        System.out.println("Failed to fetch user: " + e.getMessage());
        System.exit(0);
      }

      idValueLabel.setText(userInfo.getId().toString());
      usernameValueLabel.setText(userInfo.getUsername());
      emailValueLabel.setText(userInfo.getEmail());
      phoneNumberValueLabel.setText(userInfo.getPhoneNumber());
      verifiedValueLabel.setText(userInfo.getVerified().toString());
      createdAtValueLabel.setText(userInfo.getCreatedAt().toString());

    } else {
      CardLayout cl = (CardLayout) container.getLayout();
      cl.show(container, "home");
    }
  }
}
