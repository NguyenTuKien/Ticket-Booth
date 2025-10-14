package com.team7.ticket_booth.controller.api;

import com.team7.ticket_booth.dto.request.CardRequestDTO;
import com.team7.ticket_booth.service.VNPayService;
import com.team7.ticket_booth.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final UserService userService;
    private final VNPayService vnPayService;

    @PostMapping("/create_payment")
    public ResponseEntity<?> createPayment(HttpServletRequest request,
                                           @RequestParam("amount") int amount,
                                           @RequestParam("orderId") String orderId,
                                           @RequestParam("vnp_OrderInfo") String orderInfo) throws IOException {

        // Gọi service để tạo URL thanh toán VNPay cho đơn hàng
        String paymentUrl = vnPayService.createPaymentUrl(request, amount, orderId, orderInfo);

        // Tạo một đối tượng Response để trả về JSON
        Map<String, String> response = Map.of(
                "code", "00",
                "message", "success",
                "data", paymentUrl
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/vnpay-ipn")
    public ResponseEntity<Map<String, String>> handleIpn(@RequestParam Map<String, String> allParams) {
        // Gọi service để xử lý logic IPN
        Map<String, String> response = vnPayService.processIpnResponse(allParams);
        return ResponseEntity.ok(response);
    }

}
