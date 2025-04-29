package org.lib.subscriptionservice.service;

import org.lib.subscriptionservice.dto.PaymentRequestDTO;
import org.lib.subscriptionservice.entity.Payment;

public interface IPaymentService {
    Payment createPayment(PaymentRequestDTO paymentRequestDTO);
}
