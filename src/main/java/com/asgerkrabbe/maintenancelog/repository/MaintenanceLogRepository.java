package com.asgerkrabbe.maintenancelog.repository;

import com.asgerkrabbe.maintenancelog.entity.MaintenanceLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLog, Long> {
}