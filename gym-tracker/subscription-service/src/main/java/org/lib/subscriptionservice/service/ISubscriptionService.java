package org.lib.subscriptionservice.service;

import org.lib.subscriptionservice.dto.InfoSubscriptionDTO;
import org.lib.subscriptionservice.entity.Payment;
import org.lib.subscriptionservice.entity.Status;
import org.lib.subscriptionservice.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ISubscriptionService {
    Subscription createSubscriptionFromPayment(Payment payment);
    boolean deleteSubscription(UUID subscriptionId);
    Page<Subscription> getAllSubscriptionsAdmin(Pageable pageable);
    Page<Subscription> getAllActiveSubscriptionsUser(Pageable pageable);
    InfoSubscriptionDTO getInfoSubscription(UUID userId);
    Subscription updateStatusSubscription(UUID subscriptionId, Status status);
}
