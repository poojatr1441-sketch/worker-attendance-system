package com.worker.tracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OvertimeSummaryResponse {

    private Long workerId;
    private String month;

    private double totalHours;
    private BigDecimal totalAmount;

    private List<DailyBreakdown> breakdown;

    public OvertimeSummaryResponse(Long workerId, String month,
                                   double totalHours,
                                   BigDecimal totalAmount,
                                   List<DailyBreakdown> breakdown) {
        this.workerId = workerId;
        this.month = month;
        this.totalHours = totalHours;
        this.totalAmount = totalAmount;
        this.breakdown = breakdown;
    }

    public static class DailyBreakdown {
        private LocalDate date;
        private double hours;
        private BigDecimal amount;

        public DailyBreakdown(LocalDate date, double hours, BigDecimal amount) {
            this.date = date;
            this.hours = hours;
            this.amount = amount;
        }

        public LocalDate getDate() { return date; }
        public double getHours() { return hours; }
        public BigDecimal getAmount() { return amount; }
    }

    public Long getWorkerId() { return workerId; }
    public String getMonth() { return month; }
    public double getTotalHours() { return totalHours; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public List<DailyBreakdown> getBreakdown() { return breakdown; }
}