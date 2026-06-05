package com.worker.tracker.external;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WageRateClient {

    public BigDecimal getLatestWageRateMultiplier() {
        return BigDecimal.valueOf(1.5);
    }
}