package com.team7.ticket_booth.service;

import com.team7.ticket_booth.dto.request.PaymentRequestDTO;
import com.team7.ticket_booth.model.Order;
import com.team7.ticket_booth.model.Payment;
import com.team7.ticket_booth.model.enums.Method;
import com.team7.ticket_booth.model.enums.Status;
import com.team7.ticket_booth.repository.OrderRepository;
import com.team7.ticket_booth.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public Payment updatePaymentMethod(Payment payment, Method method) {
        if(Method.CASH.equals(method)) {
            payment.setMethod(Method.CASH);
        } else {
            payment.setMethod(Method.BANK_TRANSFER);
            payment.setStatus(Status.PENDING);
        }
        return paymentRepository.save(payment);
    }

    public Payment updatePaymentStatus(Payment payment, Status status) {
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }

    public Payment createPayment(PaymentRequestDTO dto) {
        Order order = orderRepository.findById(dto.getOrderId()).
                orElseThrow(() -> new RuntimeException("Order not found with id: " + dto.getOrderId()));
        Method method = Method.findByDescription(dto.getMethod());
        LocalDateTime now = LocalDateTime.now();
        Payment payment = new Payment(null, order, Method.CASH, Status.PENDING,
                null, null, now, now);
        if(method != Method.CASH) {
            payment = updatePaymentMethod(payment, method);
        }
        return paymentRepository.save(payment);
    }

}
