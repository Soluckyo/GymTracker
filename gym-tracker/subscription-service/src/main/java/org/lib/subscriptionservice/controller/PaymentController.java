package org.lib.subscriptionservice.controller;

import org.lib.subscriptionservice.dto.PaymentRequestDto;
import org.lib.subscriptionservice.entity.Payment;
import org.lib.subscriptionservice.entity.PaymentStatus;
import org.lib.subscriptionservice.service.IPaymentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final IPaymentService paymentService;

    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/")
    public PaymentStatus createPayment(@RequestBody PaymentRequestDto paymentRequestDTO) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Payment payment = paymentService.createPayment(paymentRequestDTO, userId);
        return payment.getPaymentStatus();
    }
}
