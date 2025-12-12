package com.example.moto.service;

import com.example.moto.dto.MotorcycleDto;
import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MaintenanceTaskInstanceRepository;
import com.example.moto.repository.MotorcycleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MotorcycleService {
    private final MotorcycleRepository motorcycleRepository;
    private final MaintenanceTaskInstanceRepository taskInstanceRepository;
    private final StatusService statusService;

    public MotorcycleService(MotorcycleRepository motorcycleRepository,
                             MaintenanceTaskInstanceRepository taskInstanceRepository,
                             StatusService statusService) {
        this.motorcycleRepository = motorcycleRepository;
        this.taskInstanceRepository = taskInstanceRepository;
        this.statusService = statusService;
    }

    public List<Motorcycle> findAll() {
        return motorcycleRepository.findAll();
    }

    public Motorcycle findById(Long id) {
        return motorcycleRepository.findById(id).orElseThrow();
    }

    public Motorcycle create(MotorcycleDto dto) {
        Motorcycle m = new Motorcycle();
        applyDto(dto, m);
        return motorcycleRepository.save(m);
    }

    @Transactional
    public Motorcycle update(Long id, MotorcycleDto dto) {
        Motorcycle m = findById(id);
        applyDto(dto, m);
        Motorcycle saved = motorcycleRepository.save(m);
        refreshStatuses(saved);
        return saved;
    }

    public void delete(Long id) {
        motorcycleRepository.deleteById(id);
    }

    private void applyDto(MotorcycleDto dto, Motorcycle m) {
        m.setNickname(dto.getNickname());
        m.setMake(dto.getMake());
        m.setModel(dto.getModel());
        m.setYear(dto.getYear());
        m.setEngineSizeCc(dto.getEngineSizeCc());
        m.setVin(dto.getVin());
        m.setLicensePlate(dto.getLicensePlate());
        m.setPurchaseDate(dto.getPurchaseDate());
        m.setCurrentOdometerKm(dto.getCurrentOdometerKm());
        m.setNotes(dto.getNotes());
    }

    private void refreshStatuses(Motorcycle motorcycle) {
        List<MaintenanceTaskInstance> instances = taskInstanceRepository.findByMotorcycle(motorcycle);
        instances.forEach(instance -> {
            instance.setStatus(statusService.computeStatus(instance));
        });
        taskInstanceRepository.saveAll(instances);
    }
}
