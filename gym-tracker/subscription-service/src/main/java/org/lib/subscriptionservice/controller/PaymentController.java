package org.lib.subscriptionservice.controller;

import org.lib.subscriptionservice.dto.PaymentRequestDTO;
import org.lib.subscriptionservice.entity.Payment;
import org.lib.subscriptionservice.entity.PaymentStatus;
import org.lib.subscriptionservice.service.IPaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final IPaymentService paymentService;

    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/")
    public PaymentStatus createPayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        Payment payment = paymentService.createPayment(paymentRequestDTO);
        return payment.getPaymentStatus();
    }
}
