package com.worker.tracker.repository;

import com.worker.tracker.entity.AttendanceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttendanceLog, Long> {

    // ---------------- ACTIVE CLOCK-IN ----------------
    @Query("""
        SELECT a FROM AttendanceLog a
        WHERE a.worker.id = :workerId
        AND a.clockOut IS NULL
        ORDER BY a.clockIn DESC
    """)
    Optional<AttendanceLog> findLatestActiveLog(@Param("workerId") Long workerId);

    // ---------------- ATTENDANCE LOG (PAGINATED) ----------------
    @EntityGraph(attributePaths = {"worker", "site"})
    @Query("""
        SELECT a FROM AttendanceLog a
        WHERE a.worker.id = :workerId
        AND a.clockIn BETWEEN :from AND :to
    """)
    Page<AttendanceLog> findLogs(
            @Param("workerId") Long workerId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable
    );
    long countByFlaggedTrue();
}