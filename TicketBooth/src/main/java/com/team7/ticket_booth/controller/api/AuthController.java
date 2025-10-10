package com.team7.ticket_booth.controller.api;

import com.team7.ticket_booth.dto.request.SignupRequestDTO;
import com.team7.ticket_booth.model.User;
import com.team7.ticket_booth.model.enums.Role;
import com.team7.ticket_booth.service.user.AuthService;
import com.team7.ticket_booth.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequestDTO request) {
        // Check if username already exists
        if (authService.findByUsername(request.getUsername()) != null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Tên đăng nhập đã tồn tại");
            return ResponseEntity.badRequest().body(error);
        }

        // Create user from DTO
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .role(Role.GUEST)
                .build();

        User registered = authService.register(user);

        // Return success response without sensitive data
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Đăng ký thành công");
        response.put("username", registered.getUsername());
        response.put("email", registered.getEmail());

        return ResponseEntity.ok(response);
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
