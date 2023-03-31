package com.company.controller;

import com.company.auth.AuthService;
import com.company.model.request.AuthRequest;
import com.company.model.request.RegisterRequest;
import com.company.model.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @project: refreshtokenIncookie
 * @author: Sarvar55
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/1.0/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
        return authService.refreshToken(request);
    }
}
