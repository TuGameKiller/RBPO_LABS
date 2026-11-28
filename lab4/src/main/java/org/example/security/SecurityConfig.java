package org.example.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class    SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) //XSRF-TOKEN X-XSRF-TOKEN
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/csrf").permitAll()
                        .requestMatchers("/api/users/**").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.GET, "/api/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/waiting-requests/enqueue").hasAnyRole("READER", "LIBRARIAN")
                        .requestMatchers(HttpMethod.POST, "/api/loans/*/extend").hasAnyRole("READER", "LIBRARIAN")
                        .requestMatchers(HttpMethod.POST, "/api/**").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.PATCH, "/api/**").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("LIBRARIAN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()) //Authorization: Basic
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                        .accessDeniedHandler((req, res, e) ->
                                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
                );

        return http.build();
    }

    @RestController
    public static class CsrfController {
        @GetMapping("/api/csrf")
        public CsrfToken csrf(CsrfToken token) {
            return token;
        }
    }
}