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
    //thiet ke form xac nhan dat ve
    private String getBillTempalate(){
        return """
        <!DOCTYPE html>
                <html lang="vi">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Xác nhận Đặt vé Thành công</title>
                </head>
                <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f0f0f0;">
              
                <div style="max-width: 400px; margin: 20px auto; background-color: white; border: 1px solid #ccc; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
              
                    <div style="display: flex; justify-content: space-between; align-items: center; padding: 10px; background-color: white; border-bottom: 1px solid #eee;">
                         <h3 style="margin: 0; font-size: 16px; font-weight: bold; color: #333;">THÔNG TIN CHI TIẾT</h3>
                    </div>
              
                    <div style="padding: 15px;">
                        <h2 style="margin: 0 0 5px 0; font-size: 18px; font-weight: bold; color: #333;">{{movie_title}} <span style="font-size: 10px; color: gold; border: 1px solid gold; padding: 1px 3px; border-radius: 2px;">{{movie_rating}}</span></h2>
                        <p style="margin: 0 0 15px 0; font-size: 14px; color: #666;">
                            **{{show_date}}**<br>
                            {{show_time}}
                        </p>
              
                        <div style="display: flex; flex-wrap: wrap; margin-bottom: 15px;">
                            <div style="width: 50%; padding-right: 10px; box-sizing: border-box;">
                                <p style="margin: 0; font-size: 12px; color: #999;">Rạp CGV</p>
                                <p style="margin: 2px 0 10px 0; font-size: 16px; font-weight: bold; color: #333;">{{cinema_name}}</p>
              
                                <p style="margin: 0; font-size: 12px; color: #999;">Ghế</p>
                                <p style="margin: 2px 0 0 0; font-size: 16px; font-weight: bold; color: #333;">{{seats}}</p>
                            </div>
                            <div style="width: 50%; padding-left: 10px; box-sizing: border-box;">
                                <p style="margin: 0; font-size: 12px; color: #999;">Phòng chiếu</p>
                                <p style="margin: 2px 0 10px 0; font-size: 16px; font-weight: bold; color: #333;">{{screen_room}}</p>
                            </div>
                        </div>
              
                        <p style="margin: 0 0 15px 0; font-size: 14px; color: #666;">{{concessions}}</p>
                    </div>
              
                    <div style="background-color: #ffc107; text-align: center; padding: 15px 0; border-top: 1px solid #e0b400; border-bottom: 1px solid #e0b400;">
                        <p style="margin: 0; font-size: 18px; font-weight: bold; color: #333;">Lấy ngay</p>
                    </div>
              
                    <div style="padding: 20px 15px; text-align: center;">
                        <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII="\s
                             alt="Mô phỏng Barcode"\s
                             style="width: 90%; height: 60px; background-color: black; margin-bottom: 10px; border-radius: 3px;">
                        <p style="margin: 0 0 15px 0; font-size: 20px; font-weight: bold; color: #333; letter-spacing: 5px;">{{ticket_code}}</p>
                        <p style="margin: 0; font-size: 14px; color: #666;">
                            Vui lòng đưa mã số này đến quầy vé CGV để nhận vé của bạn.
                        </p>
                    </div>
              
                    <div style="border-top: 1px solid #eee; padding: 15px;">
                        <p style="margin: 0; font-size: 14px; color: #999;">Tổng Cộng</p>
                        <p style="margin: 5px 0 20px 0; font-size: 24px; font-weight: bold; color: #cc0000;">
                            {{total_price}} <span style="font-size: 16px; font-weight: normal; color: #333;">{{payment_method}}</span>
                        </p>
              
                        <p style="margin: 0; font-size: 12px; color: #cc0000; font-weight: bold;">
                            Lưu ý <span style="color: #333; font-weight: normal;">CGV không chấp nhận hoàn tiền hoặc đổi vé đã thanh toán thành công trên website</span>
                        </p>
                    </div>
              
                    <div style="display: flex; justify-content: space-around; align-items: center; padding: 10px 0; background-color: #f9f9f9; border-top: 1px solid #eee;">
                        <span style="font-size: 24px; color: #666;">&#x25c0;</span> <span style="font-size: 24px; color: #666;">&#x25cf;</span> <span style="font-size: 24px; color: #666;">&#x25a1;</span> </div>
              
                </div>
              
                </body>
                </html>
      """;
    }
    // 4. Send Bill Email
    @Async
    public void sendBillEmail(String to, String movieTitle, String movieRating, String showDate, String showTime,
                              String cinemaName, String seats, String screenRoom, String concessions,
                              String ticketCode, String totalPrice, String paymentMethod) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // Thêm UTF-8 cho tiếng Việt

        helper.setTo(to);
        helper.setSubject("Xác nhận Đặt Vé Thành Công: " + movieTitle);
        helper.setFrom("your_email@gmail.com");

        String htmlContent = getBillTempalate()
                .replace("{{movie_title}}", movieTitle)
                .replace("{{movie_rating}}", movieRating)
                .replace("{{show_date}}", showDate)
                .replace("{{show_time}}", showTime)
                .replace("{{cinema_name}}", cinemaName)
                .replace("{{seats}}", seats)
                .replace("{{screen_room}}", screenRoom)
                .replace("{{concessions}}", concessions)
                .replace("{{ticket_code}}", ticketCode)
                .replace("{{total_price}}", totalPrice)
                .replace("{{payment_method}}", paymentMethod);

        helper.setText(htmlContent, true);

        // Lưu ý: Tùy chỉnh điều kiện gửi email theo logic ứng dụng của bạn
        mailSender.send(message);
    }
}

