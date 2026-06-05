package com.worker.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ActiveWorkerResponse {
    private Long workerId;
    private Long siteId;
    private LocalDateTime clockInTime;
}