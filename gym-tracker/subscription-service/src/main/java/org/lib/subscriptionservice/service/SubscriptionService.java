package org.lib.subscriptionservice.service;

import org.lib.subscriptionservice.dto.InfoSubscriptionDTO;
import org.lib.subscriptionservice.dto.SubscriptionCreateDTO;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionService implements ISubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    public Subscription createSubscription(SubscriptionCreateDTO dto) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(dto.getSubscriptionPlanId())
                .orElseThrow(() -> new SubscriptionPlanNotFoundException("Не найден тарифный план!"));

        Subscription subscription = Subscription.builder()
                .subscriptionPlan(plan)
                .userId(dto.getUserId())
                .status(Status.ACTIVE)
                .startDate(LocalDateTime.now())
                .build();

        subscriptionRepository.save(subscription);
        return subscription;
    }

    public Subscription updateSubscription(Subscription subscription) {
        return null;
    }

    public boolean deleteSubscription(Long subscriptionId) {
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

    public InfoSubscriptionDTO getInfoSubscription(Long userId) {
        Subscription subscription = subscriptionRepository.findByUserId(userId).orElseThrow(
                () -> new SubscriptionNotFoundException("Абонемент не найден!"));

        return InfoSubscriptionDTO.builder()
                .status(subscription.getStatus())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .namePlan(subscription.getSubscriptionPlan().getPlanName())
                .duration(subscription.getSubscriptionPlan().getDuration())
                .build();
    }

}
