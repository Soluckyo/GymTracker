package org.lib.subscriptionservice.controller;

import org.lib.subscriptionservice.dto.InfoSubscriptionDTO;
import org.lib.subscriptionservice.entity.Subscription;
import org.lib.subscriptionservice.service.ISubscriptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    private final ISubscriptionService subscriptionService;

    public SubscriptionController(ISubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/all")
    public Page<Subscription> getAllSubscriptionsAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return subscriptionService.getAllSubscriptionsAdmin(pageable);
    }

    @GetMapping("/active")
    public Page<Subscription> getActiveSubscriptionsUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return subscriptionService.getAllActiveSubscriptionsUser(pageable);
    }

    @GetMapping("/info/{userId}")
    public InfoSubscriptionDTO getSubscriptionsInfo(
            @PathVariable Long userId
    ){
        return subscriptionService.getInfoSubscription(userId);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteSubscription(@PathVariable Long userId){
        if(subscriptionService.deleteSubscription(userId)) {
            return new ResponseEntity<>("Абонемент успешно аннулирован", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Ошибка аннулирования, абонемент не найден", HttpStatus.NOT_FOUND);
        }
    }


}
