package com.worker.tracker.dto;

import com.worker.tracker.enums.SettlementStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OvertimeHistoryResponse {

    private LocalDate date;

    private Double overtimeHours;

    private BigDecimal amount;

    private SettlementStatus status;

    public OvertimeHistoryResponse(
            LocalDate date,
            Double overtimeHours,
            BigDecimal amount,
            SettlementStatus status
    ) {
        this.date = date;
        this.overtimeHours = overtimeHours;
        this.amount = amount;
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getOvertimeHours() {
        return overtimeHours;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public SettlementStatus getStatus() {
        return status;
    }
}