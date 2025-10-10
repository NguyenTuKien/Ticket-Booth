package com.team7.ticket_booth.controller.api;

import com.team7.ticket_booth.dto.request.CardRequestDTO;
import com.team7.ticket_booth.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final UserService userService;

    @PostMapping("/card")
    public ResponseEntity<Map<String, String>> processPayment(@RequestBody CardRequestDTO cardRequestDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            if (cardRequestDTO.getEmail() == null) {
                response.put("status", "error");
                response.put("message", "Missing user email");
                return ResponseEntity.badRequest().body(response);
            }
            userService.updateCardInfo(cardRequestDTO);
            response.put("status", "success");
            response.put("message", "Card information updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to update card information: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
