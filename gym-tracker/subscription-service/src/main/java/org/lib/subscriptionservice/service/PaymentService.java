package org.lib.subscriptionservice.service;

import lombok.extern.slf4j.Slf4j;
import org.lib.subscriptionservice.dto.PaymentRequestDto;
import org.lib.subscriptionservice.entity.Payment;
import org.lib.subscriptionservice.entity.PaymentStatus;
import org.lib.subscriptionservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class PaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final ISubscriptionService subscriptionService;

    public PaymentService(PaymentRepository paymentRepository, ISubscriptionService subscriptionService) {
        this.paymentRepository = paymentRepository;
        this.subscriptionService = subscriptionService;
    }

    @Transactional
    public Payment createPayment(PaymentRequestDto paymentRequestDTO, UUID userId) {
        //Тут должно быть проверка и списание средств!!
        //int random = (int) (Math.random() * 10);
        boolean paymentSuccess = true;

        log.info("Статус платежа: {}", paymentSuccess);

        Payment payment = Payment.builder()
                .userId(userId)
                .amount(paymentRequestDTO.getAmount())
             //   .subscriptionPlanId(paymentRequestDTO.getSubscriptionPlanId())
                .paymentDate(LocalDateTime.now())
                .paymentStatus(paymentSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED)
                .build();
        paymentRepository.save(payment);


        if(paymentSuccess){
            subscriptionService.createSubscriptionFromPayment(payment);
            log.info("Абонемент создан!");
        }
        return payment;
    }
}
