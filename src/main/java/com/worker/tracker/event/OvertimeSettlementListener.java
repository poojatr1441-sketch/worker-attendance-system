package com.worker.tracker.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OvertimeSettlementListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OvertimeSettledEvent event) {

        // simulate SMS call
        System.out.println(
                "SMS SENT -> Worker: " + event.getWorkerId() +
                ", Month: " + event.getMonth() +
                ", Amount: " + event.getAmount()
        );

        // real system: call SMS API here
    }
}