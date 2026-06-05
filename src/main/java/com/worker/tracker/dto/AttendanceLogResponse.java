package com.worker.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AttendanceLogResponse {

    private Long attendanceId;

    private Long workerId;
    private String workerName;

    private Long siteId;
    private String siteName;

    private LocalDateTime clockIn;
    private LocalDateTime clockOut;

    private Double totalHours;
    private Double overtimeHours;

    private boolean flagged;
}