package org.lib.subscriptionservice.service;

import org.lib.subscriptionservice.dto.InfoSubscriptionDTO;
import org.lib.subscriptionservice.entity.Payment;
import org.lib.subscriptionservice.entity.Status;
import org.lib.subscriptionservice.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISubscriptionService {
    Subscription createSubscriptionFromPayment(Payment payment);
    boolean deleteSubscription(Long subscriptionId);
    Page<Subscription> getAllSubscriptionsAdmin(Pageable pageable);
    Page<Subscription> getAllActiveSubscriptionsUser(Pageable pageable);
    InfoSubscriptionDTO getInfoSubscription(Long userId);
    Subscription updateStatusSubscription(String subscriptionId, Status status);
}
