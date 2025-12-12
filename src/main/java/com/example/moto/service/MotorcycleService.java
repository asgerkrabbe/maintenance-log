package com.example.moto.service;

import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MotorcycleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MotorcycleService {
    private final MotorcycleRepository motorcycleRepository;
    private final MaintenanceTaskService maintenanceTaskService;

    public MotorcycleService(MotorcycleRepository motorcycleRepository, MaintenanceTaskService maintenanceTaskService) {
        this.motorcycleRepository = motorcycleRepository;
        this.maintenanceTaskService = maintenanceTaskService;
    }

    public List<Motorcycle> findAll() {
        return motorcycleRepository.findAll();
    }

    public Optional<Motorcycle> findById(Long id) {
        return motorcycleRepository.findById(id);
    }

    public Motorcycle save(Motorcycle motorcycle) {
        Motorcycle saved = motorcycleRepository.save(motorcycle);
        maintenanceTaskService.recomputeStatusesForMotorcycle(saved);
        return saved;
    }

    public void delete(Long id) {
        motorcycleRepository.deleteById(id);
    }
}
