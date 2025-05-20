package org.lib.subscriptionservice.service;

import org.lib.subscriptionservice.dto.ChangeStatusOfSubscriptionPlanDto;
import org.lib.subscriptionservice.dto.CreateSubscriptionPlanDto;
import org.lib.subscriptionservice.entity.Status;
import org.lib.subscriptionservice.entity.SubscriptionPlan;

public interface ISubscriptionPlanService {
    SubscriptionPlan createSubscriptionPlan(CreateSubscriptionPlanDto subscriptionPlanDTO);

    Status changeStatusOfSubscriptionPlan(ChangeStatusOfSubscriptionPlanDto dto);

}
