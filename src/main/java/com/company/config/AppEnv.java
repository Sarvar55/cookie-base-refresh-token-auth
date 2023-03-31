package com.company.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @project: refreshtokenIncookie
 * @author: Sarvar55
 */
@Data
@ConfigurationProperties(prefix = "app")
public class AppEnv {
    private String secretKey;
    private Long expiryDateMs;
    private Long jwtRefreshExpirationMs;
    private String jwtCookieName;
}
