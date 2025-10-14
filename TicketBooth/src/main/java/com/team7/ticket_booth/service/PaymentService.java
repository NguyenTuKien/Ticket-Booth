package com.team7.ticket_booth.service;

import com.team7.ticket_booth.dto.request.PaymentRequestDTO;
import com.team7.ticket_booth.model.Order;
import com.team7.ticket_booth.model.Payment;
import com.team7.ticket_booth.model.enums.Status;
import com.team7.ticket_booth.repository.OrderRepository;
import com.team7.ticket_booth.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public Payment updatePaymentMethod(Payment payment, String method) {
        payment.setMethod(method);
        return paymentRepository.save(payment);
    }

    public Payment updatePaymentStatus(UUID orderId, Status status) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found with order id: " + orderId));
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }

    public Payment updatePaymentMethod(UUID orderId, String method) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found with order id: " + orderId));
        payment.setMethod(method);
        return paymentRepository.save(payment);
    }

    public Payment updateProviderTxnId(UUID orderId, String providerTxnId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found with order id: " + orderId));
        payment.setProviderTxnId(providerTxnId);
        return paymentRepository.save(payment);
    }

    public Payment updatePaymentUrl(UUID orderId, String url) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found with order id: " + orderId));
        payment.setPaymentUrl(url);
        return paymentRepository.save(payment);
    }

    public Payment createPayment(PaymentRequestDTO dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + dto.getOrderId()));

        String method = dto.getMethod();
        if (method == null || method.isBlank()) {
            log.warn("Payment method null/blank từ request, fallback về 'BANK'. DTO raw: {}", dto);
            method = "BANK"; // fallback để không vi phạm NOT NULL & tránh constraint sai
        }
        log.debug("Persist payment với method='{}'", method);

        Payment payment = Payment.builder()
                .order(order)
                .method(method)
                .status(Status.PENDING)
                .build();

        return paymentRepository.save(payment);
    }

    public Payment getByOdrerId(UUID orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found with order id: " + orderId));
    }
}
