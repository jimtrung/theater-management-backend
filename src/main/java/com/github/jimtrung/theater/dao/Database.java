package com.github.jimtrung.theater.dao;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
  private final Connection connection;

  public Database() throws SQLException {
    String dbUser = Dotenv.load().get("DB_USER");
    String dbUrl = Dotenv.load().get("DB_URL");
    String dbPassword = Dotenv.load().get("DB_PASS");
    connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
  }

  public Connection getConnection() {
    return connection;
  }
}
