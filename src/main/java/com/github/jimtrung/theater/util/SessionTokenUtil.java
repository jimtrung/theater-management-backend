package com.github.jimtrung.theater.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Base64;
import java.util.UUID;

public class SessionTokenUtil {
  public static void generateSessionToken(UUID userId) throws Exception {
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(128);
    SecretKey secretKey = keyGen.generateKey();

    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encrypted = cipher.doFinal(userId.toString().getBytes());

    String encoded =  Base64.getEncoder().encodeToString(encrypted);

    FileWriter fw = new FileWriter("session.enc");
  }

  public static String getKey() throws Exception {
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(128);
    SecretKey secretKey = keyGen.generateKey();

    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    BufferedReader br = new BufferedReader(new FileReader("session.enc"));
    String reader = br.readLine();

    byte[] decoded = Base64.getDecoder().decode(reader);
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    return new String(cipher.doFinal(decoded));
  }
}
