package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.dto.TokenPair;
import com.github.jimtrung.theater.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class OAuthController {
    private final UserService userService;

    public OAuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<TokenPair> user(@AuthenticationPrincipal OAuth2User principal) {
        TokenPair tokenPair = userService.logInOrSignUpOAuth(principal);

        return ResponseEntity
            .status(200)
            .body(tokenPair);
    }
}
