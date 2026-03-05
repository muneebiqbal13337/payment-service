package com.paymentapi.payment_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a payment transaction in the system.
 * Maps to the 'payments' table in the database.
 */
@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Full name of the person making the payment. */
    @NotBlank(message = "Payer name is required")
    @Size(min = 2, max = 100, message = "Payer name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String payerName;

    /** Email address of the payer. */
    @NotBlank(message = "Payer email is required")
    @Email(message = "Payer email must be a valid email address")
    @Column(nullable = false)
    private String payerEmail;

    /** Payment amount. Must be greater than zero. */
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Amount must have at most 2 decimal places")
    @Column(nullable = false)
    private BigDecimal amount;

    /** ISO 4217 currency code e.g. USD, EUR, GBP. */
    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
    @Column(nullable = false)
    private String currency;

    /** Current status of the payment. */
    @Column(nullable = false)
    private String status;

    /**
     * Unique key provided by the client to prevent duplicate payments.
     * If a request is retried with the same key, the original payment is returned.
     */
    @NotBlank(message = "Idempotency key is required")
    @Column(unique = true, nullable = false)
    private String idempotencyKey;

    /** Timestamp when the payment was created. Set automatically. */
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * Automatically sets createdAt timestamp and default status
     * before the entity is persisted to the database.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = "PENDING";
    }
}