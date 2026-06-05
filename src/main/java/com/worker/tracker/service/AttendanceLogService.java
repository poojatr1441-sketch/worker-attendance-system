package com.worker.tracker.service;

import com.worker.tracker.dto.AttendanceLogResponse;
import com.worker.tracker.dto.PagedResponse;
import com.worker.tracker.entity.AttendanceLog;
import com.worker.tracker.repository.AttendanceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class AttendanceLogService {

    private final AttendanceRepository repo;

    public AttendanceLogService(AttendanceRepository repo) {
        this.repo = repo;
    }

    public PagedResponse<AttendanceLogResponse> getLogs(
            Long workerId,
            LocalDateTime from,
            LocalDateTime to,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<AttendanceLog> logs =
                repo.findLogs(workerId, from, to, pageable);

        var content = logs.getContent()
                .stream()
                .map(l -> new AttendanceLogResponse(
                        l.getId(),
                        l.getWorker().getId(),
                        l.getWorker().getName(),
                        l.getSite().getId(),
                        l.getSite().getSiteName(),
                        l.getClockIn(),
                        l.getClockOut(),
                        l.getTotalHours(),
                        l.getOvertimeHours(),
                        l.isFlagged()
                ))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                logs.getNumber(),
                logs.getSize(),
                logs.getTotalElements(),
                logs.getTotalPages()
        );
    }
}