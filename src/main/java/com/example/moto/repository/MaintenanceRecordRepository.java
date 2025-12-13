package com.example.moto.repository;

import com.example.moto.entity.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
    List<MaintenanceRecord> findByMotorcycleIdOrderByDateDesc(Long motorcycleId);
    List<MaintenanceRecord> findByMotorcycleIdAndCategoryOrderByDateDesc(Long motorcycleId, String category);
}
