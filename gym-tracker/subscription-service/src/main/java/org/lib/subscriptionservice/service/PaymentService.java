package org.lib.subscriptionservice.service;

import org.lib.subscriptionservice.dto.PaymentRequestDTO;
import org.lib.subscriptionservice.entity.Payment;
import org.lib.subscriptionservice.entity.PaymentStatus;
import org.lib.subscriptionservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final ISubscriptionService subscriptionService;

    public PaymentService(PaymentRepository paymentRepository, ISubscriptionService subscriptionService) {
        this.paymentRepository = paymentRepository;
        this.subscriptionService = subscriptionService;
    }

    @Transactional
    public Payment createPayment(PaymentRequestDTO paymentRequestDTO) {
        //Тут должно быть проверка и списание средств!!
        boolean paymentSuccess = true;

        Payment payment = Payment.builder()
                .userId(paymentRequestDTO.getUserId())
                .amount(paymentRequestDTO.getAmount())
                .paymentDate(LocalDateTime.now())
                .paymentStatus(paymentSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED)
                .build();
        paymentRepository.save(payment);

        if(paymentSuccess){
            subscriptionService.createSubscriptionFromPayment(payment);
        }
        return payment;
    }
}
