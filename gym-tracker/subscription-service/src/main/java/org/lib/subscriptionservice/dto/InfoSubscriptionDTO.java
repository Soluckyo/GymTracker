package org.lib.subscriptionservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.lib.subscriptionservice.entity.Status;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
public class InfoSubscriptionDTO {
    private String namePlan;
    private String duration;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Status status;
}
