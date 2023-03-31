package com.company.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @project: refreshtokenIncookie
 * @author: Sarvar55
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class RefreshTokenExpired extends RuntimeException {
    public RefreshTokenExpired(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
