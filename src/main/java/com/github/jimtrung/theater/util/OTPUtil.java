package com.github.jimtrung.theater.util;

import org.springframework.stereotype.Component;

@Component
public class OTPUtil {
  public int generateOTP() {
    return Math.abs((int)(Math.random() * 1000000));
  }
}
