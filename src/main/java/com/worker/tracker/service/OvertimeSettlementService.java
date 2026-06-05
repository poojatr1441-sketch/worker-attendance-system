package com.worker.tracker.service;

import com.worker.tracker.dto.OvertimeHistoryResponse;
import com.worker.tracker.dto.OvertimeSummaryResponse;
import com.worker.tracker.entity.OvertimeEntry;
import com.worker.tracker.enums.SettlementStatus;
import com.worker.tracker.event.OvertimeSettledEvent;
import com.worker.tracker.external.WageRateClient;
import com.worker.tracker.repository.OvertimeRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class OvertimeSettlementService {

    private final OvertimeRepository repo;
    private final ApplicationEventPublisher publisher;
    private final WageRateClient wageRateClient;

    public OvertimeSettlementService(
            OvertimeRepository repo,
            ApplicationEventPublisher publisher,
            WageRateClient wageRateClient
    ) {
        this.repo = repo;
        this.publisher = publisher;
        this.wageRateClient = wageRateClient;
    }

    @Transactional
    public BigDecimal settle(Long workerId, String month) {

        YearMonth targetMonth = YearMonth.parse(month);
        YearMonth currentMonth = YearMonth.now();

        if (targetMonth.equals(currentMonth)) {
            throw new RuntimeException("Cannot settle current month");
        }

        LocalDate startDate = targetMonth.atDay(1);
        LocalDate endDate = targetMonth.atEndOfMonth();

        List<OvertimeEntry> entries =
                repo.findMonthlyEntries(
                        workerId,
                        startDate,
                        endDate,
                        SettlementStatus.PENDING
                );

        if (entries.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal externalMultiplier =
                wageRateClient.getLatestWageRateMultiplier();

        double totalHours = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OvertimeEntry entry : entries) {

            totalHours += entry.getOvertimeHours();

            BigDecimal adjustedAmount =
                    entry.getAmount().multiply(externalMultiplier);

            totalAmount = totalAmount.add(adjustedAmount);
        }

        if (totalHours > 60) {
            totalAmount = totalAmount.multiply(
                    BigDecimal.valueOf(60.0 / totalHours)
            );
        }

        for (OvertimeEntry entry : entries) {
            entry.setSettlementStatus(SettlementStatus.SETTLED);
        }

        repo.saveAll(entries);

        publisher.publishEvent(
                new OvertimeSettledEvent(
                        workerId,
                        month,
                        totalAmount
                )
        );

        return totalAmount;
    }

    public OvertimeSummaryResponse getSummary(
            Long workerId,
            String month
    ) {

        YearMonth ym = YearMonth.parse(month);

        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<OvertimeEntry> entries =
                repo.findMonthlyEntries(
                        workerId,
                        start,
                        end,
                        SettlementStatus.PENDING
                );

        double totalHours = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        List<OvertimeSummaryResponse.DailyBreakdown> breakdown =
                new java.util.ArrayList<>();

        for (OvertimeEntry e : entries) {

            totalHours += e.getOvertimeHours();
            totalAmount = totalAmount.add(e.getAmount());

            breakdown.add(
                    new OvertimeSummaryResponse.DailyBreakdown(
                            e.getDate(),
                            e.getOvertimeHours(),
                            e.getAmount()
                    )
            );
        }

        return new OvertimeSummaryResponse(
                workerId,
                month,
                totalHours,
                totalAmount,
                breakdown
        );
    }

    public List<OvertimeHistoryResponse> getHistory(
            Long workerId
    ) {

        return repo.findByWorkerIdOrderByDateDesc(workerId)
                .stream()
                .map(entry -> new OvertimeHistoryResponse(
                        entry.getDate(),
                        entry.getOvertimeHours(),
                        entry.getAmount(),
                        entry.getSettlementStatus()
                ))
                .toList();
    }
}