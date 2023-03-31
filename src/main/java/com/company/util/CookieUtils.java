package com.company.util;

import com.company.config.AppEnv;
import com.company.security.jwt.JwtHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.spec.SecretKeySpec;


/**
 * @project: refreshtokenIncookie
 * @author: Sarvar55
 */
@Slf4j
@Component
public class CookieUtils {

    private final AppEnv appEnv;
    private final JwtHelper jwtHelper;

    public CookieUtils(AppEnv appEnv, JwtHelper jwtHelper) {
        this.appEnv = appEnv;
        this.jwtHelper = jwtHelper;
    }

    public Cookie getJwtFromCookies(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie;
        } else {
            return null;
        }
    }

    private ResponseCookie generateJwtCookie(String data, String cookieName, long maxAge) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, data).path("/api").maxAge(maxAge).httpOnly(true).build();
        return cookie;
    }

    public ResponseCookie generateCookieForRefresh(String data, String cookieName, long maxAge) {
        return generateJwtCookie(data, cookieName, maxAge);
    }

    public ResponseCookie getCleanJwtCookie(String cookieName) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, null).path("/api").build();
        return cookie;
    }


}
