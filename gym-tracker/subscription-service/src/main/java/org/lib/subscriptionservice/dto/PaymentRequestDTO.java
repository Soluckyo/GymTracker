package org.lib.subscriptionservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class PaymentRequestDTO {
    private Long subscriptionPlanId;
    private BigDecimal amount;
}
