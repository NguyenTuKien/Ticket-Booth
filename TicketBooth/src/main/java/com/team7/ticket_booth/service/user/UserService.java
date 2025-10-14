package com.team7.ticket_booth.service.user;

import com.team7.ticket_booth.exception.NotFoundException;
import com.team7.ticket_booth.model.User;
import com.team7.ticket_booth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByUsername(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
