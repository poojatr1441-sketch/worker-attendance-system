package com.worker.tracker.controller;

import com.worker.tracker.dto.ClockInRequest;
import com.worker.tracker.dto.ActiveWorkerResponse;
import com.worker.tracker.service.ActiveWorkerService;
import com.worker.tracker.service.AttendanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final ActiveWorkerService activeWorkerService;
    
    public AttendanceController(
            AttendanceService attendanceService,
            ActiveWorkerService activeWorkerService
    ) {
        this.attendanceService = attendanceService;
        this.activeWorkerService = activeWorkerService;
    }

    // ---------------- CLOCK IN ----------------
    @PostMapping("/clock-in")
    public String clockIn(@RequestBody ClockInRequest request) {
        attendanceService.clockIn(request);
        return "Clock-in successful";
    }

    // ---------------- CLOCK OUT ----------------
    @PostMapping("/clock-out")
    public String clockOut(@RequestParam Long workerId) {
        attendanceService.clockOut(workerId);
        return "Clock-out successful";
    }

    // ---------------- ACTIVE WORKERS (Redis ONLY) ----------------
    @GetMapping("/active")
    public List<ActiveWorkerResponse> getActiveWorkers() {
        return activeWorkerService.getActiveWorkers();
    }
}