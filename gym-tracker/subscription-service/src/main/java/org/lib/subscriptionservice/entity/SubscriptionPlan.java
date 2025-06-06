package org.lib.subscriptionservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlan {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "subscription_plan_id", updatable = false, nullable = false)
    private UUID subscriptionPlanId;

    private String planName;

    @Enumerated(EnumType.STRING)
    private SubscriptionType type;

    @Enumerated(EnumType.STRING)
    private Duration durationPlan;

    private Integer visitLimit;

    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Override
    public String toString() {
        return "SubscriptionPlan{" +
                "subscriptionPlanId=" + subscriptionPlanId +
                ", planName='" + planName + '\'' +
                ", type=" + type +
                ", durationPlan=" + durationPlan +
                ", visitLimit=" + visitLimit +
                ", cost=" + cost +
                ", status=" + status +
                '}';
    }
}
