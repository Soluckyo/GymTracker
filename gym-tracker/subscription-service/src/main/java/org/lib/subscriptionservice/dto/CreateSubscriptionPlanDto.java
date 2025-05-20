package org.lib.subscriptionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.subscriptionservice.entity.Duration;
import org.lib.subscriptionservice.entity.Status;
import org.lib.subscriptionservice.entity.SubscriptionType;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubscriptionPlanDto {
    private UUID planId;
    private String planName;
    private Integer visitLimit;
    private BigDecimal cost;
    private Status planStatus;
    private SubscriptionType type;
    private Duration durationPlan;
}
