package com.team7.ticket_booth.controller.web;

import com.team7.ticket_booth.dto.response.PaymentResponseDTO;
import com.team7.ticket_booth.model.Order;
import com.team7.ticket_booth.model.Ticket;
import com.team7.ticket_booth.model.User;
import com.team7.ticket_booth.service.OrderService;
import com.team7.ticket_booth.repository.PaymentRepository;
import com.team7.ticket_booth.service.TicketService;
import com.team7.ticket_booth.service.VNPayService;
import com.team7.ticket_booth.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import jakarta.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Controller
public class CheckoutController {
    private final OrderService orderService;
    private final TicketService ticketService;
    private final UserService userService;
    private final PaymentRepository paymentRepository;
    private final VNPayService vnPayService;

    // GET hiển thị trang chi tiết đơn hàng
    @GetMapping("/checkout")
    public String viewOrder(@RequestParam("orderId") UUID orderId, Model model, HttpServletRequest request) {
        Order order = orderService.getOrderEntityById(orderId);
        List<Ticket> tickets = ticketService.getTicketByOrderId(order.getId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        String email = null;
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                currentUser = userService.findByUsername(authentication.getName());
                email = currentUser.getEmail();
            } catch (Exception ignored) {}
        }
    int totalCost = orderService.getCost(order.getId());
    var paymentEntity = paymentRepository.findByOrderId(order.getId())
                .orElseThrow(() -> new RuntimeException("Payment not found for order " + orderId));
    // Nếu phương thức là BANK và chưa có URL VNPay thì tạo URL ngay để hiển thị nút thanh toán
    if ("BANK".equalsIgnoreCase(paymentEntity.getMethod()) && (paymentEntity.getPaymentUrl() == null || paymentEntity.getPaymentUrl().isBlank())) {
        String orderInfo = "TICKETBOOTH " + order.getId();
        String url = vnPayService.createPaymentUrl(request, totalCost, order.getId().toString().replace("-", ""), orderInfo);
        // set tạm vào entity để DTO thấy ngay, đồng thời service cũng đã lưu DB
        paymentEntity.setPaymentUrl(url);
    }
        PaymentResponseDTO payment = new PaymentResponseDTO(paymentEntity, totalCost);
        model.addAttribute("tickets", tickets);
        model.addAttribute("order", order);
        model.addAttribute("payment", payment);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("email", email);
        return "checkout";
    }

    @GetMapping("/payment_return")
    public String handleReturnUrl(Model model, @RequestParam Map<String, String> allParams) {

        // TODO: Gọi service để kiểm tra chữ ký (logic tương tự IPN)
        boolean isSignatureValid = vnPayService.validateSignature(allParams);

        if (isSignatureValid) {
            String responseCode = allParams.get("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                model.addAttribute("message", "Giao dịch thành công!");
                model.addAttribute("status", "success");
            } else {
                model.addAttribute("message", "Giao dịch không thành công. Lỗi: " + responseCode);
                model.addAttribute("status", "fail");
            }
        } else {
            model.addAttribute("message", "Chữ ký không hợp lệ!");
            model.addAttribute("status", "fail");
        }

        return "payment-result"; // Trả về tên của file view (ví dụ: payment-result.html)
    }
}
