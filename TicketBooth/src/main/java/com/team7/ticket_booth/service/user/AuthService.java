package com.team7.ticket_booth.service.user;

import com.team7.ticket_booth.model.User;
import com.team7.ticket_booth.model.enums.Role;
import com.team7.ticket_booth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(User user) {
        // Encode mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.GUEST);
        }
        // createdAt và updatedAt sẽ tự set bởi @CreationTimestamp / @UpdateTimestamp
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User getUserById(UUID userId){
        return userRepository.findById(userId).
                orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
