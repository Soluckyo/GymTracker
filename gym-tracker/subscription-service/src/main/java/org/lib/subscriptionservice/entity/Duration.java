package org.lib.subscriptionservice.entity;

import java.time.LocalDate;

public enum Duration {
    YEAR(365),
    HALF_YEAR(181),
    THREE_MONTHS(90),
    MONTH(30),
    NONE(0);


    private final int days;

    Duration(int days) {
        this.days = days;
    }

    public LocalDate calculateEndDate(LocalDate startDate) {
        return startDate.plusDays(days);
    }
}
