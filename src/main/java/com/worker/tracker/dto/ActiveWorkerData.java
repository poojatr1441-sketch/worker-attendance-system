package com.worker.tracker.dto;

import java.time.LocalDateTime;

public class ActiveWorkerData {
    private Long siteId;
    private LocalDateTime clockInTime;

    public ActiveWorkerData(Long siteId, LocalDateTime clockInTime) {
        this.siteId = siteId;
        this.clockInTime = clockInTime;
    }

    public Long getSiteId() {
        return siteId;
    }

    public LocalDateTime getClockInTime() {
        return clockInTime;
    }
}
