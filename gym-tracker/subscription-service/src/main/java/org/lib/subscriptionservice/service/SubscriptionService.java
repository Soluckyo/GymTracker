package org.lib.subscriptionservice.service;

import org.lib.subscriptionservice.dto.InfoSubscriptionDto;
import org.lib.subscriptionservice.entity.Payment;
import org.lib.subscriptionservice.entity.Status;
import org.lib.subscriptionservice.entity.Subscription;
import org.lib.subscriptionservice.entity.SubscriptionPlan;
import org.lib.subscriptionservice.exception.SubscriptionNotFoundException;
import org.lib.subscriptionservice.exception.SubscriptionPlanNotFoundException;
import org.lib.subscriptionservice.repository.SubscriptionPlanRepository;
import org.lib.subscriptionservice.repository.SubscriptionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService implements ISubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Subscription createSubscriptionFromPayment(Payment payment) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(payment.getSubscriptionPlanId())
                .orElseThrow(() -> new SubscriptionPlanNotFoundException("Не найден тарифный план!"));

        //TODO: доделать дату окончания абонемента
        Subscription subscription = Subscription.builder()
                .subscriptionPlan(plan)
                .userId(payment.getUserId())
                .status(Status.ACTIVE)
                .startDate(LocalDateTime.now())
                .build();

        subscriptionRepository.save(subscription);
        return subscription;
    }

    public Subscription updateStatusSubscription(UUID subscriptionId, Status status) {
        return null;
    }

    public boolean deleteSubscription(UUID subscriptionId) {
        if(!subscriptionRepository.existsById(subscriptionId)) {
            throw new SubscriptionNotFoundException("Такого абонемента не существует");
        }
        subscriptionRepository.deleteById(subscriptionId);
        return true;
    }

    public Page<Subscription> getAllSubscriptionsAdmin(Pageable pageable) {
        Page<Subscription> subscriptions = subscriptionRepository.findAll(pageable);
        if(subscriptions.isEmpty()){
            throw new SubscriptionNotFoundException("Список абонементов пуст!");
        }
        return subscriptions;
    }

    public Page<Subscription> getAllActiveSubscriptionsUser(Pageable pageable) {
        List<SubscriptionPlan> activePlan = subscriptionPlanRepository.findByStatusContains(Status.ACTIVE);
        if(!activePlan.isEmpty()) {
            throw new SubscriptionPlanNotFoundException("Активных тарифных планов не найдено!");
        }
        return subscriptionRepository.findBySubscriptionPlanIn(activePlan, pageable);
    }

    public InfoSubscriptionDto getInfoSubscription(UUID userId) {
        Subscription subscription = subscriptionRepository.findByUserId(userId).orElseThrow(
                () -> new SubscriptionNotFoundException("Абонемент не найден!"));

        //TODO: доделать конечную дату
        return InfoSubscriptionDto.builder()
                .status(subscription.getStatus())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .namePlan(subscription.getSubscriptionPlan().getPlanName())
                .duration(subscription.getSubscriptionPlan().getDuration())
                .build();
    }

}
