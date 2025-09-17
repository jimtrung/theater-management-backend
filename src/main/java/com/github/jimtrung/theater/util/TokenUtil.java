package com.github.jimtrung.theater.util;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenUtil {
  public static String generateToken() {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[32]; // 256-bit token
    random.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }
}
