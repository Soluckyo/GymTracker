package org.lib.subscriptionservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.lib.subscriptionservice.entity.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class InfoSubscriptionDto {
    private String namePlan;
    private String duration;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Status status;
}
