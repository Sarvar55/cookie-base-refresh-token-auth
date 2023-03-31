package com.company.security.service;

import com.company.config.AppEnv;
import com.company.util.CookieUtils;
import jakarta.annotation.security.DenyAll;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @project: refreshtokenIncookie
 * @author: Sarvar55
 */
@Service
@AllArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final CookieUtils cookieUtils;
    private final AppEnv appEnv;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie cookie = cookieUtils.getJwtFromCookies(request, appEnv.getJwtCookieName());
        if (cookie != null) {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            cookie.setPath("/api");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }
        SecurityContextHolder.clearContext();
    }
}
