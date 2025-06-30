package com.alkemy.disney_AlkemyChallenge.Security;

import com.alkemy.disney_AlkemyChallenge.Repository.UsuarioRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IJWTUtilityService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final IJWTUtilityService jwtUtilityService;
    private final UsuarioRepository usuarioRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/api-docs/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .requestMatchers(
                                        "/audiovisuals",
                                        "/audiovisuals/featured",
                                        "/audiovisuals/{id}",
                                        "/characters",
                                        "/characters/featured",
                                        "/characters/{id}",
                                        "/categories",
                                        "/genre",
                                        "/genre/**"
                                ).permitAll()
                                .requestMatchers("/profile", "/profile/**").authenticated()
                                .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JWTAuthorizationFilter(jwtUtilityService, usuarioRepository), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint((request, response, authException) -> {
                                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                                }
                                )
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                                }
                                )
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
