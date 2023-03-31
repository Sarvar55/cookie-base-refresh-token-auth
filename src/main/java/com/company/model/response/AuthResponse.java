package com.company.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @project: refreshtokenIncookie
 * @author: Sarvar55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    @Builder.Default
    private String tokenType = "Bearer";
    private String token;
}
