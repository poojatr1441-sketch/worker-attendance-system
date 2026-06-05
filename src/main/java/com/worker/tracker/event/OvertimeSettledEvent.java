package com.worker.tracker.event;

import java.math.BigDecimal;

public class OvertimeSettledEvent {

    private Long workerId;
    private String month;
    private BigDecimal amount;

    public OvertimeSettledEvent(Long workerId, String month, BigDecimal amount) {
        this.workerId = workerId;
        this.month = month;
        this.amount = amount;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public String getMonth() {
        return month;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}