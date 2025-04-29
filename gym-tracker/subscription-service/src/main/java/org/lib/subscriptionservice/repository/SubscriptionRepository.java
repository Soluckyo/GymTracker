package org.lib.subscriptionservice.repository;

import org.lib.subscriptionservice.entity.Subscription;
import org.lib.subscriptionservice.entity.SubscriptionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserId(Long userId);

    Page<Subscription> findBySubscriptionPlanIn(List<SubscriptionPlan> activePlan, Pageable pageable);
}
