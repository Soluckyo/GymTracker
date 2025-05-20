package org.lib.subscriptionservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(name = "DTO для изменения статуса тарифа")
public class ChangeStatusOfSubscriptionPlanDto {

    @Schema(name = "UUID плана абонемента")
    private UUID planId;

    @Schema(name = "Новый статус плана абонемента")
    private String status;
}
