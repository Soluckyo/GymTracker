package org.lib.subscriptionservice.repository;

import org.lib.subscriptionservice.entity.Subscription;
import org.lib.subscriptionservice.entity.SubscriptionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByUserId(UUID userId);
    List<Subscription> findBySubscriptionPlanIn(List<SubscriptionPlan> activePlan, Pageable pageable);
    boolean existsById(UUID subscriptionId);
    void deleteById(UUID subscriptionId);
}
