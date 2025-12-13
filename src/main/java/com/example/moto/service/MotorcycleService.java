package com.example.moto.service;

import com.example.moto.dto.MotorcycleDto;
import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MotorcycleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MotorcycleService {
    private final MotorcycleRepository motorcycleRepository;
    private final MaintenanceTaskService maintenanceTaskService;

    public MotorcycleService(MotorcycleRepository motorcycleRepository,
                             MaintenanceTaskService maintenanceTaskService) {
        this.motorcycleRepository = motorcycleRepository;
        this.maintenanceTaskService = maintenanceTaskService;
    }

    public List<Motorcycle> findAll() {
        return motorcycleRepository.findAll();
    }

    public Optional<Motorcycle> findById(Long id) {
        return motorcycleRepository.findById(id);
    }

    @Transactional
    public Motorcycle save(MotorcycleDto dto) {
        Motorcycle moto = dto.getId() != null ? motorcycleRepository.findById(dto.getId())
                .orElse(new Motorcycle()) : new Motorcycle();
        moto.setNickname(dto.getNickname());
        moto.setMake(dto.getMake());
        moto.setModel(dto.getModel());
        moto.setYear(dto.getYear());
        moto.setEngineSizeCc(dto.getEngineSizeCc());
        moto.setVin(dto.getVin());
        moto.setLicensePlate(dto.getLicensePlate());
        moto.setPurchaseDate(dto.getPurchaseDate());
        moto.setCurrentOdometerKm(dto.getCurrentOdometerKm());
        moto.setNotes(dto.getNotes());
        Motorcycle saved = motorcycleRepository.save(moto);
        maintenanceTaskService.refreshStatusesForMotorcycle(saved);
        return saved;
    }

    @Transactional
    public void delete(Long id) {
        motorcycleRepository.deleteById(id);
    }
}
