package com.github.jimtrung.theater.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.regex.Pattern;

@Component
public class EmailValidator {
  private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
  private final JavaMailSender mailSender;

  public EmailValidator(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  // 1. Check email syntax
  private Boolean isValidSyntax(String email) {
    if (email == null) return false;
    return EMAIL_PATTERN.matcher(email).matches();
  }

  // 2. Has MX record
  private Boolean hasMXRecord(String domain) {
    try {
      Hashtable<String, String> env = new Hashtable<>();
      env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
      DirContext ctx = new InitialDirContext(env);
      Attributes attrs = ctx.getAttributes(domain, new String[]{"MX"});
      Attribute attr = attrs.get("MX");
      return attr != null && attr.size() > 0;
    } catch (Exception e) {
      return false;
    }
  }

  // 3. Send email
  @Async
  public void sendVerificationEmail(String to, String verifyLink) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(to);
    helper.setSubject("Verify your email");
    helper.setFrom("your_email@gmail.com");

    String htmlContent = getEmailTemplate().replace("{{verify_link}}", verifyLink);

    helper.setText(htmlContent, true);
    if (to.equals("nguyenhaitrung737@gmail.com")) mailSender.send(message);
  }

  private String getEmailTemplate() {
    return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8">
          <title>Verify your email</title>
          <style>
            body { font-family: 'Segoe UI', Arial, sans-serif; background:#f4f6f8; color:#333; }
            .container { max-width:500px; margin:40px auto; background:#fff; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.1); }
            .header { background:#0a66c2; color:white; text-align:center; padding:24px; }
            .header h1 { margin:0; font-size:24px; }
            .content { padding:24px; }
            .content p { font-size:15px; line-height:1.6; margin-bottom:20px; }
            .verify-btn { display:inline-block; padding:12px 24px; background:#0a66c2; color:white !important; text-decoration:none; border-radius:6px; font-weight:bold; }
            .footer { text-align:center; font-size:13px; color:#777; padding:16px; background:#f9fafb; }
          </style>
        </head>
        <body>
          <div class="container">
            <div class="header">
              <h1>Verify your email 🍌</h1>
            </div>
            <div class="content">
              <p>Hi there! Thanks for signing up. Please confirm your email address by clicking the button below.</p>
              <p style="text-align:center;">
                <a href="{{verify_link}}" class="verify-btn">Verify Email</a>
              </p>
              <p>If you didn’t sign up for this account, you can safely ignore this message 🚬</p>
            </div>
            <div class="footer">
              &copy; 2025 LinkedIn Clone · All rights reserved
            </div>
          </div>
        </body>
        </html>
        """;
  }

  public Boolean isValidEmail(String email) {
    return isValidSyntax(email) && hasMXRecord(email.substring(email.indexOf('@') + 1));
  }
}
