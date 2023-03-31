package com.company.config;

import com.company.security.jwt.JwtTokenFilter;
import com.company.security.service.CustomLogoutHandler;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @project: refreshtokenIncookie
 * @author: Sarvar55
 */
@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JwtTokenFilter jwtTokenFilter;
    private final CustomLogoutHandler customLogoutHandler;
    private final AppEnv appEnv;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic()
                .disable()
                .csrf().disable()
                .cors().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, "/api/1.0/auth/**").permitAll()
                            .anyRequest().authenticated();

                }).sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/1.0/auth/logout")
                .invalidateHttpSession(true)
                .deleteCookies(appEnv.getJwtCookieName())
                .addLogoutHandler(customLogoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> {
                    SecurityContextHolder.clearContext();
                });

        return http.build();
    }
}
