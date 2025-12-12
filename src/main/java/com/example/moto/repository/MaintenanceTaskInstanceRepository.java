package com.example.moto.repository;

import com.example.moto.entity.MaintenanceStatus;
import com.example.moto.entity.MaintenanceTaskInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceTaskInstanceRepository extends JpaRepository<MaintenanceTaskInstance, Long> {
    List<MaintenanceTaskInstance> findByMotorcycleId(Long motorcycleId);
    List<MaintenanceTaskInstance> findAllByOrderByStatusAscNextDueDateAscNextDueOdometerKmAsc();
    List<MaintenanceTaskInstance> findByMotorcycleIdOrderByStatusAscNextDueDateAsc(Long motorcycleId);
    List<MaintenanceTaskInstance> findByStatus(MaintenanceStatus status);
}
