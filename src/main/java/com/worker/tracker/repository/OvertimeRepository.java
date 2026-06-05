package com.worker.tracker.repository;

import com.worker.tracker.entity.OvertimeEntry;
import com.worker.tracker.enums.SettlementStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OvertimeRepository extends JpaRepository<OvertimeEntry, Long> {

    List<OvertimeEntry> findByWorkerIdAndDateBetween(
            Long workerId,
            LocalDate start,
            LocalDate end
    );

    List<OvertimeEntry> findByWorkerIdAndSettlementStatus(
            Long workerId,
            SettlementStatus status
    );

    // ✅ CLEAN MONTHLY QUERY (USED FOR SETTLEMENT)
    @Query("""
        SELECT o FROM OvertimeEntry o
        WHERE o.worker.id = :workerId
        AND o.date BETWEEN :startDate AND :endDate
        AND o.settlementStatus = :status
    """)
    List<OvertimeEntry> findMonthlyEntries(
            @Param("workerId") Long workerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") SettlementStatus status
    );
    @EntityGraph(attributePaths = {"worker"})
    List<OvertimeEntry> findByWorkerIdOrderByDateDesc(Long workerId);
    
    long countBySettlementStatus(SettlementStatus status);
    
    @Query("""
    	    SELECT COALESCE(SUM(o.overtimeHours),0)
    	    FROM OvertimeEntry o
    	    WHERE o.worker.id = :workerId
    	    AND o.date BETWEEN :startDate AND :endDate
    	""")
    	Double getTotalMonthlyOvertimeHours(
    	        @Param("workerId") Long workerId,
    	        @Param("startDate") LocalDate startDate,
    	        @Param("endDate") LocalDate endDate
    	);
}