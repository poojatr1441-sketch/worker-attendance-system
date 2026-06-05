package com.worker.tracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import com.worker.tracker.enums.Designation;

@Entity
@Table(
    name = "workers",
    indexes = {
        @Index(name = "idx_worker_phone", columnList = "phone"),
        @Index(name = "idx_worker_active", columnList = "active")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Designation designation;

    @Column(name = "daily_wage_rate", nullable = false)
    private BigDecimal dailyWageRate;

    @Column(nullable = false)
    private boolean active = true;
}