package com.team7.ticket_booth.config;

import com.team7.ticket_booth.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // tắt CSRF cho API và form login
                .authorizeHttpRequests(auth -> auth
                        // Form login và public pages
                        .requestMatchers("/", "/home", "/login", "/signup", "/css/**", "/js/**", "/images/**", "/movie").permitAll()
                        // API public
                        .requestMatchers("/api/v1/auth/**", "/api/v1/movies/**").permitAll()
                        // Admin API
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        // Các request khác cần login
                        .anyRequest().authenticated()
                )
                // Form login HTML
                .formLogin(form -> form
                        .loginPage("/login")                  // GET hiển thị form
                        .loginProcessingUrl("/perform_login") // POST xử lý login
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .usernameParameter("username")        // field HTML
                        .passwordParameter("password")        // field HTML
                )
                // Session cho form login
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

        // JWT filter chỉ áp dụng cho API
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
