package com.paymentapi.payment_service.controller;

import com.paymentapi.payment_service.model.Payment;
import com.paymentapi.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing payment transactions.
 * Exposes endpoints for creating, retrieving, and updating payments.
 * Base URL: /api/payments
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Creates a new payment transaction.
     * If a payment with the same idempotency key already exists,
     * the existing payment is returned without creating a duplicate.
     *
     * @param payment the payment details from the request body
     * @return the created or existing payment with HTTP 201
     */
    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) {
        Payment created = paymentService.createPayment(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves all payment transactions.
     *
     * @return list of all payments with HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    /**
     * Retrieves a single payment by its ID.
     *
     * @param id the payment ID from the URL path
     * @return the payment if found with HTTP 200, or HTTP 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates the status of an existing payment.
     * Valid statuses: PENDING, COMPLETED, FAILED, REFUNDED
     *
     * @param id     the payment ID from the URL path
     * @param status the new status from the query parameter
     * @return the updated payment with HTTP 200
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Payment> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Payment updated = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}