package com.github.jimtrung.theater.util;

public class OTPUtil {
  public static int generateOTP() {
    return Math.abs((int)(Math.random() * 1000000));
  }
}
