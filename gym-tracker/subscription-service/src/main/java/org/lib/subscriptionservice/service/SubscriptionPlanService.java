package org.lib.subscriptionservice.service;

import org.lib.subscriptionservice.dto.ChangeStatusOfSubscriptionPlanDto;
import org.lib.subscriptionservice.dto.CreateSubscriptionPlanDto;
import org.lib.subscriptionservice.entity.Duration;
import org.lib.subscriptionservice.entity.Status;
import org.lib.subscriptionservice.entity.SubscriptionPlan;
import org.lib.subscriptionservice.exception.IllegalDurationException;
import org.lib.subscriptionservice.exception.IllegalSubscriptionType;
import org.lib.subscriptionservice.exception.IllegalVisitLimitException;
import org.lib.subscriptionservice.exception.StatusAlreadySetException;
import org.lib.subscriptionservice.exception.SubscriptionPlanNotFoundException;
import org.lib.subscriptionservice.repository.SubscriptionPlanRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SubscriptionPlanService implements ISubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlanService(SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }


    public SubscriptionPlan createSubscriptionPlan(CreateSubscriptionPlanDto dto) {
        switch(dto.getType()){
            case LIMITED:
                if(dto.getVisitLimit() == null || dto.getVisitLimit() <= 0){
                    throw new IllegalVisitLimitException("Лимит занятий должен быть больше 0!");
                }
                break;
            case UNLIMITED:
                if(dto.getVisitLimit() != null || dto.getDurationPlan() == null){
                    throw new IllegalDurationException("Продолжительность абонемента не может быть null!");
                }
                break;
            case SINGLE:
                dto.setVisitLimit(1);
                dto.setDurationPlan(Duration.NONE);
                break;
            default:
                throw new IllegalSubscriptionType("Неизвестный тип абонемента!");
        }

        SubscriptionPlan subscriptionPlan = SubscriptionPlan.builder()
                .planName(dto.getPlanName())
                .type(dto.getType())
                .durationPlan(dto.getDurationPlan())
                .visitLimit(dto.getVisitLimit())
                .cost(dto.getCost())
                .status(Status.ACTIVE)
                .build();

        return subscriptionPlanRepository.save(subscriptionPlan);
    }

    public Status changeStatusOfSubscriptionPlan(ChangeStatusOfSubscriptionPlanDto dto) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(dto.getPlanId()).orElseThrow(
                () -> new SubscriptionPlanNotFoundException("Тарифный план не найден!"));
        Status status = Status.valueOf(dto.getStatus());
        if(plan.getStatus().equals(status)){
            throw new StatusAlreadySetException("Установленный статус не должен соответствовать новому статусу!");
        }
        plan.setStatus(status);
        return plan.getStatus();
    }
}
