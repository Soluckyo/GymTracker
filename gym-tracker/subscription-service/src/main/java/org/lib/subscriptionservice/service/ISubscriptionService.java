package org.lib.subscriptionservice.service;

import org.lib.subscriptionservice.dto.InfoSubscriptionDto;
import org.lib.subscriptionservice.entity.Payment;
import org.lib.subscriptionservice.entity.Status;
import org.lib.subscriptionservice.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ISubscriptionService {
    Subscription createSubscriptionFromPayment(Payment payment);
    boolean deleteSubscription(UUID subscriptionId);
    Page<Subscription> getAllSubscriptionsAdmin(Pageable pageable);
    Page<Subscription> getAllSubscriptionsUser(UUID userId, Pageable pageable);
    InfoSubscriptionDto getInfoActiveSubscription(UUID userId);
    Subscription updateStatusSubscription(UUID subscriptionId, Status status);
}
