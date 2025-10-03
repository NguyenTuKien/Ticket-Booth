package com.team7.ticket_booth.controller;

import com.team7.ticket_booth.model.User;
import com.team7.ticket_booth.service.user.AuthService;
import com.team7.ticket_booth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registered = authService.register(user);
        return ResponseEntity.ok(registered);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // load UserDetails để tạo token
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
        // trả về JSON chứa token
        return ResponseEntity.ok(Map.of("token", jwtToken));
    }

    @GetMapping("/me")
    public ResponseEntity<User> me(Authentication authentication) {
        String username = authentication.getName();
        User user = authService.findByUsername(username);
        return ResponseEntity.ok(user);
    }
}
