package org.lib.subscriptionservice.service;

import org.lib.subscriptionservice.dto.ChangeStatusOfSubscriptionPlanDto;
import org.lib.subscriptionservice.dto.CreateSubscriptionPlanDto;
import org.lib.subscriptionservice.entity.Status;

public interface ISubscriptionPlanService {
    CreateSubscriptionPlanDto createSubscriptionPlan(CreateSubscriptionPlanDto subscriptionPlanDTO);

    Status changeStatusOfSubscriptionPlan(ChangeStatusOfSubscriptionPlanDto dto);

}
