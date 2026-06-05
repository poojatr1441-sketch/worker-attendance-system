package com.worker.tracker.controller;

import com.worker.tracker.dto.PagedResponse;
import com.worker.tracker.dto.AttendanceLogResponse;
import com.worker.tracker.service.AttendanceLogService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceLogController {

    private final AttendanceLogService service;

    public AttendanceLogController(AttendanceLogService service) {
        this.service = service;
    }

    @GetMapping("/log")
    public PagedResponse<AttendanceLogResponse> getLogs(
            @RequestParam Long workerId,
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        return service.getLogs(
                workerId,
                LocalDateTime.parse(from),
                LocalDateTime.parse(to),
                page,
                size
        );
    }
}