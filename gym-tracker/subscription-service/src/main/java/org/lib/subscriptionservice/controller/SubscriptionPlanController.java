package org.lib.subscriptionservice.controller;

import org.lib.subscriptionservice.dto.ChangeStatusOfSubscriptionPlanDto;
import org.lib.subscriptionservice.dto.CreateSubscriptionPlanDto;
import org.lib.subscriptionservice.entity.Status;
import org.lib.subscriptionservice.service.ISubscriptionPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscription-plan")
public class SubscriptionPlanController {
    private final ISubscriptionPlanService subscriptionPlanService;
    public SubscriptionPlanController(ISubscriptionPlanService subscriptionPlanService) {
        this.subscriptionPlanService = subscriptionPlanService;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateSubscriptionPlanDto> createSubscriptionPlan(@RequestBody CreateSubscriptionPlanDto subscriptionPlan) {
        CreateSubscriptionPlanDto returnDto =
                subscriptionPlanService.createSubscriptionPlan(subscriptionPlan);
        return new ResponseEntity<>(returnDto, HttpStatus.CREATED);
    }

    @PutMapping("/change-status")
    public ResponseEntity<String> changeStatusOfSubscriptionPlan(@RequestBody ChangeStatusOfSubscriptionPlanDto dto) {
        Status status = subscriptionPlanService.changeStatusOfSubscriptionPlan(dto);
        return new ResponseEntity<>(status.toString(), HttpStatus.OK);
    }
}
