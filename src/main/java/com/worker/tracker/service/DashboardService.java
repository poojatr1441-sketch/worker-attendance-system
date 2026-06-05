package com.worker.tracker.service;

import com.worker.tracker.dto.DashboardSummaryResponse;
import com.worker.tracker.enums.SettlementStatus;
import com.worker.tracker.repository.AttendanceRepository;
import com.worker.tracker.repository.OvertimeRepository;
import com.worker.tracker.repository.SiteRepository;
import com.worker.tracker.repository.WorkerRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final WorkerRepository workerRepo;
    private final SiteRepository siteRepo;
    private final AttendanceRepository attendanceRepo;
    private final OvertimeRepository overtimeRepo;

    public DashboardService(
            WorkerRepository workerRepo,
            SiteRepository siteRepo,
            AttendanceRepository attendanceRepo,
            OvertimeRepository overtimeRepo
    ) {
        this.workerRepo = workerRepo;
        this.siteRepo = siteRepo;
        this.attendanceRepo = attendanceRepo;
        this.overtimeRepo = overtimeRepo;
    }

    public DashboardSummaryResponse getSummary() {

        return new DashboardSummaryResponse(
                workerRepo.count(),
                workerRepo.countByActiveTrue(),
                siteRepo.countByActiveTrue(),
                overtimeRepo.countBySettlementStatus(
                        SettlementStatus.PENDING
                ),
                attendanceRepo.countByFlaggedTrue()
        );
    }
}