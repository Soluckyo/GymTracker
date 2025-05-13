package org.lib.subscriptionservice.service;

import org.lib.subscriptionservice.dto.PaymentRequestDto;
import org.lib.subscriptionservice.entity.Payment;

import java.util.UUID;

public interface IPaymentService {
    Payment createPayment(PaymentRequestDto paymentRequestDTO, UUID userId);
}
