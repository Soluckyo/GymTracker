package org.lib.subscriptionservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.lib.subscriptionservice.entity.Status;
import org.lib.subscriptionservice.entity.SubscriptionPlan;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@ToString
public class SubscriptionCreateDTO {
    private Long subscriptionPlanId;
    private Long userId;
    private Status status;
}

