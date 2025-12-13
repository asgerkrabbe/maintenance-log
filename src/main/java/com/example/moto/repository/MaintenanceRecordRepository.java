package com.example.moto.repository;

import com.example.moto.entity.MaintenanceRecord;
import com.example.moto.entity.Motorcycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
    List<MaintenanceRecord> findByMotorcycleOrderByDateDesc(Motorcycle motorcycle);
}
