package org.lib.subscriptionservice.repository;

import org.lib.subscriptionservice.entity.Status;
import org.lib.subscriptionservice.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, UUID> {

    List<SubscriptionPlan> findByStatusContains(Status status);

    Optional<SubscriptionPlan> findById(UUID subscriptionPlanId);
}
