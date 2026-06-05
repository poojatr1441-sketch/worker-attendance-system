package com.worker.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardSummaryResponse {

    private long totalWorkers;

    private long activeWorkers;

    private long activeSites;

    private long pendingOvertimeEntries;

    private long flaggedAttendanceLogs;
}