package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.dto.ForgotPasswordRequest;
import com.github.jimtrung.theater.dto.RefreshRequest;
import com.github.jimtrung.theater.dto.SignUpRequest;
import com.github.jimtrung.theater.dto.TokenPair;
import com.github.jimtrung.theater.model.User;
import com.github.jimtrung.theater.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest req) {
        User newUser = new User();
        newUser.setUsername(req.username());
        newUser.setEmail(req.email());
        newUser.setPassword(req.password());

        userService.signUp(newUser);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("Đăng ký thành công");
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenPair> signIn(@RequestBody SignUpRequest req) {
        User user = new User();
        user.setUsername(req.username());
        user.setPassword(req.password());

        TokenPair tokenPair = userService.signIn(user);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(tokenPair);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody RefreshRequest refreshRequest) {
        String newAccessToken = userService.refresh(refreshRequest.refreshToken());

        if (newAccessToken == null) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Refresh token đã hết hạn");
        }

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(newAccessToken);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        // TODO: Create logic

        return ResponseEntity
            .status(200)
            .body("Email đặt lại mật khẩu đã được gửi thành công");
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.checkUsernameExists(username));
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.checkEmailExists(email));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token) {
        try {
            userService.verifyUser(token);
            String successHtml = """
                <html>
                <head>
                    <title>Xác thực thành công</title>
                    <style>
                        body { font-family: Arial, sans-serif; text-align: center; margin-top: 50px; }
                        h1 { color: #4CAF50; }
                    </style>
                </head>
                <body>
                    <h1>Xác thực tài khoản thành công!</h1>
                    <p>Bạn có thể quay lại ứng dụng và đăng nhập.</p>
                </body>
                </html>
                """;
            return ResponseEntity.ok(successHtml);
        } catch (Exception e) {
             String errorHtml = """
                <html>
                <head>
                    <title>Xác thực thất bại</title>
                    <style>
                        body { font-family: Arial, sans-serif; text-align: center; margin-top: 50px; }
                        h1 { color: #f44336; }
                    </style>
                </head>
                <body>
                    <h1>Xác thực thất bại!</h1>
                    <p>Link xác thực không hợp lệ hoặc đã hết hạn.</p>
                </body>
                </html>
                """;
            return ResponseEntity.badRequest().body(errorHtml);
        }
    }
}
