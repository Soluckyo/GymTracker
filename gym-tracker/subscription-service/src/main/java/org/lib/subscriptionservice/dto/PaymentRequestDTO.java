package org.lib.subscriptionservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentRequestDTO {
    private Long userId;
    private Long subscriptionPlanId;
    private BigDecimal amount;
}
