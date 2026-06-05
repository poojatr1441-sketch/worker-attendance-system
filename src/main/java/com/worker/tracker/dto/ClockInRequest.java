package com.worker.tracker.dto;

import lombok.Data;

@Data
public class ClockInRequest {
    private Long workerId;
    private Long siteId;
}