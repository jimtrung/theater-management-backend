package com.github.jimtrung.theater;

import com.github.jimtrung.theater.view.HomeUI;
import com.github.jimtrung.theater.view.ProfileUI;
import com.github.jimtrung.theater.view.SignInUI;
import com.github.jimtrung.theater.view.SignUpUI;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
  public Main() {
    setTitle("Theater Management");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);

    JPanel container = new JPanel(new CardLayout());

    HomeUI homeUI = new HomeUI(container);
    SignUpUI signUpUI = new SignUpUI(container);
    SignInUI signInUI = new SignInUI(container);
    ProfileUI profileUI = new ProfileUI(container);

    container.add(homeUI.getPanel1(), "home");
    container.add(signUpUI.getPanel1(), "signup");
    container.add(signInUI.getPanel1(), "signin");
    container.add(profileUI.getPanel1(), "profile");

    setContentPane(container);
    setVisible(true);
  }

  public static void main(String[] args) {
    new Main();
  }
}