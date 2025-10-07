package com.team7.ticket_booth.service;

import com.team7.ticket_booth.dto.request.PaymentRequestDTO;
import com.team7.ticket_booth.model.Order;
import com.team7.ticket_booth.model.Payment;
import com.team7.ticket_booth.model.enums.Method;
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

    public Payment updatePaymentMethod(Payment payment, Method method) {
        if(Method.CARD.equals(method)) {
            payment.setMethod(Method.CARD);
            payment.setStatus(Status.PENDING);
        } else if(Method.BANK.equals(method)) {
            payment.setMethod(Method.BANK);
            payment.setStatus(Status.PENDING);
        } else {
            payment.setMethod(Method.CASH);
            payment.setStatus(Status.PENDING);
        }
        return paymentRepository.save(payment);
    }

    public Payment updatePaymentStatus(UUID orderId, Status status) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found with order id: " + orderId));
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }

    public Payment createPayment(PaymentRequestDTO dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + dto.getOrderId()));

        Method method = dto.getMethod();
        if (method == null) {
            log.warn("Payment method null từ request, fallback về CASH. DTO raw: {}", dto);
            method = Method.CASH; // fallback để không vi phạm NOT NULL & tránh constraint sai
        }
        log.debug("Persist payment với method enum tên='{}'", method.name());

        Payment payment = Payment.builder()
                .order(order)
                .method(method)
                .status(Status.PENDING)
                .build();

        return paymentRepository.save(payment);
    }

}
