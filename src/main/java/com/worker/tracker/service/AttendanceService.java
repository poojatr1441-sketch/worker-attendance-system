package com.worker.tracker.service;

import com.worker.tracker.dto.ClockInRequest;
import com.worker.tracker.dto.ActiveWorkerData;
import com.worker.tracker.entity.*;
import com.worker.tracker.enums.SettlementStatus;
import com.worker.tracker.repository.*;
import com.worker.tracker.redis.ActiveWorkerRedisService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.YearMonth;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class AttendanceService {

    private final WorkerRepository workerRepo;
    private final SiteRepository siteRepo;
    private final AttendanceRepository attendanceRepo;
    private final OvertimeRepository overtimeRepo;
    private final ActiveWorkerRedisService redisService;

    public AttendanceService(
            WorkerRepository workerRepo,
            SiteRepository siteRepo,
            AttendanceRepository attendanceRepo,
            OvertimeRepository overtimeRepo,
            ActiveWorkerRedisService redisService
    ) {
        this.workerRepo = workerRepo;
        this.siteRepo = siteRepo;
        this.attendanceRepo = attendanceRepo;
        this.overtimeRepo = overtimeRepo;
        this.redisService = redisService;
    }

    @Transactional
    public void clockIn(ClockInRequest request) {

        Worker worker = workerRepo.findById(request.getWorkerId())
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        if (!worker.isActive()) {
            throw new RuntimeException("Worker inactive");
        }

        Site site = siteRepo.findById(request.getSiteId())
                .orElseThrow(() -> new RuntimeException("Site not found"));

        if (!site.isActive()) {
            throw new RuntimeException("Site inactive");
        }

        if (redisService.isActive(worker.getId())) {
            throw new RuntimeException("Worker already clocked in");
        }

        AttendanceLog log = AttendanceLog.builder()
                .worker(worker)
                .site(site)
                .clockIn(LocalDateTime.now())
                .build();

        attendanceRepo.save(log);

        redisService.clockIn(
                worker.getId(),
                new ActiveWorkerData(site.getId(), log.getClockIn())
        );
    }

    @Transactional
    public void clockOut(Long workerId) {

        AttendanceLog log = attendanceRepo.findLatestActiveLog(workerId)
                .orElseThrow(() -> new RuntimeException("No active clock-in found"));

        LocalDateTime now = LocalDateTime.now();

        log.setClockOut(now);

        double hours = Duration.between(log.getClockIn(), now)
                .toMinutes() / 60.0;

        log.setTotalHours(hours);

        double overtimeHours = Math.max(0, hours - 8);

        YearMonth currentMonth = YearMonth.from(now);

        Double monthlyHours =
                overtimeRepo.getTotalMonthlyOvertimeHours(
                        workerId,
                        currentMonth.atDay(1),
                        currentMonth.atEndOfMonth()
                );

        double remainingCap =
                Math.max(0, 60 - monthlyHours);

        double cappedOvertime =
                Math.min(overtimeHours, remainingCap);

        log.setOvertimeHours(cappedOvertime);

        if (hours > 16) {
            log.setFlagged(true);
        }

        attendanceRepo.save(log);

        redisService.clockOut(workerId);

        if (cappedOvertime > 0) {

        	BigDecimal rate = log.getWorker().getDailyWageRate();

        	BigDecimal amount;

        	if (cappedOvertime <= 2) {

        	    amount = rate
        	            .multiply(BigDecimal.valueOf(cappedOvertime))
        	            .multiply(BigDecimal.valueOf(1.5));

        	} else {

        	    BigDecimal firstTwoHours = rate
        	            .multiply(BigDecimal.valueOf(2))
        	            .multiply(BigDecimal.valueOf(1.5));

        	    BigDecimal remainingHours = rate
        	            .multiply(BigDecimal.valueOf(cappedOvertime - 2))
        	            .multiply(BigDecimal.valueOf(2.0));

        	    amount = firstTwoHours.add(remainingHours);
        	}

        	OvertimeEntry entry = OvertimeEntry.builder()
        	        .worker(log.getWorker())
        	        .attendance(log)
        	        .date(now.toLocalDate())
        	        .overtimeHours(cappedOvertime)
        	        .overtimeRate(
        	                cappedOvertime <= 2
        	                        ? 1.5
        	                        : 2.0
        	        )
        	        .amount(amount)
        	        .settlementStatus(SettlementStatus.PENDING)
        	        .build();

        	overtimeRepo.save(entry);
        }
    }
}