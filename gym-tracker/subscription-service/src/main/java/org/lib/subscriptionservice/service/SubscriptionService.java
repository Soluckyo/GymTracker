package org.lib.subscriptionservice.service;

import lombok.extern.slf4j.Slf4j;
import org.lib.subscriptionservice.dto.InfoSubscriptionDto;
import org.lib.subscriptionservice.entity.Payment;
import org.lib.subscriptionservice.entity.Status;
import org.lib.subscriptionservice.entity.Subscription;
import org.lib.subscriptionservice.entity.SubscriptionPlan;
import org.lib.subscriptionservice.exception.SubscriptionNotFoundException;
import org.lib.subscriptionservice.exception.SubscriptionPlanNotFoundException;
import org.lib.subscriptionservice.exception.UserAlreadyHasActiveSubscriptionException;
import org.lib.subscriptionservice.repository.SubscriptionPlanRepository;
import org.lib.subscriptionservice.repository.SubscriptionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
public class SubscriptionService implements ISubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }


    //TODO: продумать логику даты абонементов с определенным кол-вом занятий
    @Transactional(propagation = Propagation.MANDATORY)
    public Subscription createSubscriptionFromPayment(Payment payment) {
        log.info("Зашел в метод 'createSubscriptionFromPayment'");
        log.info("Платеж: {}", payment);
        if (payment.getSubscriptionPlanId() == null) {
            throw new SubscriptionPlanNotFoundException("subscriptionPlanId не был передан");
        }
        if(subscriptionRepository.findByUserIdAndStatus(payment.getUserId(), Status.ACTIVE).isPresent()){
            throw new UserAlreadyHasActiveSubscriptionException("У пользователя уже есть активная подписка!");
        }

        SubscriptionPlan plan = subscriptionPlanRepository.findById(payment.getSubscriptionPlanId())
                .orElseThrow(() -> new SubscriptionPlanNotFoundException("Не найден тарифный план!"));

        log.info("Прошел поиск тарифа: {}", plan);
        LocalDate now = LocalDate.now();

        Subscription subscription = Subscription.builder()
                .subscriptionPlan(plan)
                .userId(payment.getUserId())
                .status(Status.ACTIVE)
                .startDate(now)
                .endDate(plan.getDurationPlan().calculateEndDate(now))
                .build();

        log.info("Прошло создание абонемента: {}", subscription);

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

    public Page<Subscription> getAllSubscriptionsUser(UUID userId, Pageable pageable) {
        Page<Subscription> subscription = subscriptionRepository.findByUserId(userId, pageable);
        if(subscription.isEmpty()){
            throw new SubscriptionNotFoundException("Подписки у данного пользователя отсутствуют");
        }
        return subscription;

    }

    public InfoSubscriptionDto getInfoActiveSubscription(UUID userId) {
        Subscription subscription = subscriptionRepository.findByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new SubscriptionNotFoundException("У пользователя нет активной подписки"));

        return InfoSubscriptionDto.builder()
                .status(subscription.getStatus())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .namePlan(subscription.getSubscriptionPlan().getPlanName())
                .duration(subscription.getSubscriptionPlan().getDurationPlan().toString())
                .build();
    }

}
