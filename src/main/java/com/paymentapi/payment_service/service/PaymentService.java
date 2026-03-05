package com.paymentapi.payment_service.service;

import com.paymentapi.payment_service.exception.ResourceNotFoundException;
import com.paymentapi.payment_service.model.Payment;
import com.paymentapi.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment createPayment(Payment payment) {
        Optional<Payment> existing = paymentRepository
            .findByIdempotencyKey(payment.getIdempotencyKey());
        
        if (existing.isPresent()) {
            return existing.get();
        }

        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment updatePaymentStatus(Long id, String status) {
    Payment payment = paymentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Payment", id));
    
    payment.setStatus(status);
    return paymentRepository.save(payment);
}
}