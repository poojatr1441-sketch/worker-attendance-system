package com.worker.tracker.repository;

import com.worker.tracker.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    long countByActiveTrue();
}