package com.github.jimtrung.theater.view;

import javax.swing.*;

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

  private JPanel container;

  public ProfileUI(JPanel container) {
    this.container = container;

    panel1.setBorder(BorderFactory.createEmptyBorder(10, 80, 20, 80));


  }

  public JPanel getPanel1() {
    return panel1;
  }
}
