package com.worker.tracker.controller;

import com.worker.tracker.dto.OvertimeHistoryResponse;
import com.worker.tracker.dto.OvertimeSummaryResponse;
import com.worker.tracker.service.OvertimeSettlementService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/overtime")
public class OvertimeSettlementController {

    private final OvertimeSettlementService service;

    public OvertimeSettlementController(
            OvertimeSettlementService service
    ) {
        this.service = service;
    }

    // ---------------- SETTLEMENT ----------------

    @PostMapping("/settle/{workerId}")
    public BigDecimal settle(
            @PathVariable Long workerId,
            @RequestParam String month
    ) {
        return service.settle(workerId, month);
    }

    // ---------------- MONTHLY SUMMARY ----------------

    @GetMapping("/summary/{workerId}")
    public OvertimeSummaryResponse summary(
            @PathVariable Long workerId,
            @RequestParam String month
    ) {
        return service.getSummary(workerId, month);
    }

    // ---------------- HISTORY ----------------

    @GetMapping("/history/{workerId}")
    public List<OvertimeHistoryResponse> history(
            @PathVariable Long workerId
    ) {
        return service.getHistory(workerId);
    }
}