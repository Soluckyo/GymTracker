package org.lib.subscriptionservice.service;

import org.lib.subscriptionservice.dto.PaymentRequestDTO;
import org.lib.subscriptionservice.entity.Payment;

import java.util.UUID;

public interface IPaymentService {
    Payment createPayment(PaymentRequestDTO paymentRequestDTO, UUID userId);
}
