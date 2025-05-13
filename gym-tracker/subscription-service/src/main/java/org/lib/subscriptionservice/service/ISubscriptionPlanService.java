package org.lib.subscriptionservice.service;

import org.lib.subscriptionservice.dto.CreateSubscriptionPlanDto;
import org.lib.subscriptionservice.entity.SubscriptionPlan;

public interface ISubscriptionPlanService {
    SubscriptionPlan createSubscriptionPlan(CreateSubscriptionPlanDto subscriptionPlanDTO);

}
