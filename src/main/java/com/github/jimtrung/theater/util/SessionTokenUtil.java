package com.github.jimtrung.theater.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class SessionTokenUtil {
  private final Key key;
  private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 1 day

  public SessionTokenUtil(@Value("${jwt.secret}") String secret) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String generateToken(UUID userId) {
    return Jwts.builder()
        .setSubject(userId.toString())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
        .signWith(key)
        .compact();
  }

  public UUID parseToken(String token) {
    String subject = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
    return UUID.fromString(subject);
  }
}
