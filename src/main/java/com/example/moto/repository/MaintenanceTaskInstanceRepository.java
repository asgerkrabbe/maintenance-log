package com.example.moto.repository;

import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.Motorcycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceTaskInstanceRepository extends JpaRepository<MaintenanceTaskInstance, Long> {
    List<MaintenanceTaskInstance> findByMotorcycle(Motorcycle motorcycle);
}
