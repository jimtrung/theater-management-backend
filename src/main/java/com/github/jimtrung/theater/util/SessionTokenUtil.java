package com.github.jimtrung.theater.util;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

public class SessionTokenUtil {
  private static final Path TOKEN_FILE = Path.of("session.jwt");

  private static final Key key = Keys.hmacShaKeyFor(Dotenv.load().get("JWT_SECRET").getBytes());

  private static final long EXPIRATION = 1000 * 60 * 60 * 24;

  public static void generateAndStoreToken(UUID userId) throws Exception {
    String token = Jwts.builder()
        .setSubject(userId.toString())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
        .signWith(key)
        .compact();

    Files.writeString(TOKEN_FILE, token);
  }

  public static UUID loadAndDecodeToken() throws Exception {
    String token = Files.readString(TOKEN_FILE);

    String subject = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();

    return UUID.fromString(subject);
  }
}
