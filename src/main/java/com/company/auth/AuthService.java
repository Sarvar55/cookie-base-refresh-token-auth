package com.company.auth;

import com.company.config.AppEnv;
import com.company.exceptions.RefreshTokenExpired;
import com.company.model.request.AuthRequest;
import com.company.model.request.RegisterRequest;
import com.company.model.response.AuthResponse;
import com.company.security.jwt.JwtHelper;
import com.company.user.User;
import com.company.user.UserPrincipal;
import com.company.user.UserRepository;
import com.company.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * @project: refreshtokenIncookie
 * @author: Sarvar55
 */
@Service
@Slf4j
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final CookieUtils cookieUtils;
    private final AppEnv appEnv;

    public ResponseEntity<?> register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request
                        .getPassword()))
                .username(request.getUsername())
                .build();
        userRepository.save(user);
        return ResponseEntity.ok("user registered succesfully");
    }

    @SneakyThrows
    public ResponseEntity<AuthResponse> authenticate(AuthRequest request) {
        Authentication authentication
                = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        var cookieData = generateCookieData(userPrincipal);

        var responseCookie = cookieUtils.generateCookieForRefresh(cookieData,
                appEnv.getJwtCookieName(), appEnv.getJwtRefreshExpirationMs());

        var jwtToken = jwtHelper.generateToken(userPrincipal);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(AuthResponse.builder().token(jwtToken).build());
    }

    @SneakyThrows
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
        Cookie cookie = cookieUtils.getJwtFromCookies(request, appEnv.getJwtCookieName());
        if (cookie == null)
            throw new UsernameNotFoundException("JWT cookie not found");

        String refreshToken = cookie.getValue().split("&")[0];
        String userEmail = cookie.getValue().split("&")[1];

        if (jwtHelper.isTokenValid(refreshToken)) {

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("unauthorization"));

            String jwtToken = jwtHelper.generateToken(new UserPrincipal(user));

            return ResponseEntity.ok(AuthResponse.builder().token(jwtToken).build());
        }

        throw new RefreshTokenExpired(refreshToken, "Refresh token has expired");
    }

    private String generateCookieData(UserPrincipal userPrincipal) {
        String refreshToken = jwtHelper.generateRefreshToken();
        var hcmaData = refreshToken + "&" + userPrincipal.getUsername();
        return hcmaData;
    }


}
