package org.lib.subscriptionservice.service;

import org.lib.subscriptionservice.entity.Subscription;
import org.lib.subscriptionservice.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService implements ISubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Subscription createSubscription(Subscription subscription) {
        return null;
    }

    public Subscription updateSubscription(Subscription subscription) {
        return null;
    }

    public Subscription deleteSubscription(Subscription subscription) {
        return null;
    }

    public List<Subscription> getAllSubscriptions() {
        return null;
    }

    public void paymentSubscription(Subscription subscription) {
    }


}
