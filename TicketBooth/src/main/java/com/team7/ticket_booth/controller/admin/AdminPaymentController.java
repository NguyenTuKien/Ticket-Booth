package com.team7.ticket_booth.controller.admin;

import com.team7.ticket_booth.dto.request.CardRequestDTO;
import com.team7.ticket_booth.model.Payment;
import com.team7.ticket_booth.model.User;
import com.team7.ticket_booth.model.enums.Status;
import com.team7.ticket_booth.service.PaymentService;
import com.team7.ticket_booth.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/payments")
public class AdminPaymentController {
    private final PaymentService paymentService;
    private final UserService userService;

    @PutMapping("/success")
    public void orderSuccess(@RequestParam UUID orderId) {
        paymentService.updatePaymentStatus(orderId, Status.SUCCESS);
    }

    @PostMapping("/addCard")
    public void addCardToUser(CardRequestDTO dto) {
        userService.updateCardInfo(dto);
    }

}
