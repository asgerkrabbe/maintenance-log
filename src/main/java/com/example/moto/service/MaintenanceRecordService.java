package com.example.moto.service;

import com.example.moto.dto.MaintenanceRecordDto;
import com.example.moto.entity.*;
import com.example.moto.repository.MaintenanceRecordRepository;
import com.example.moto.repository.MaintenanceTaskInstanceRepository;
import com.example.moto.repository.MaintenanceTaskTemplateRepository;
import com.example.moto.repository.MotorcycleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceRecordService {
    private final MaintenanceRecordRepository recordRepository;
    private final MotorcycleRepository motorcycleRepository;
    private final MaintenanceTaskTemplateRepository templateRepository;
    private final MaintenanceTaskInstanceRepository instanceRepository;
    private final TaskStatusService statusService;

    public MaintenanceRecordService(MaintenanceRecordRepository recordRepository,
                                    MotorcycleRepository motorcycleRepository,
                                    MaintenanceTaskTemplateRepository templateRepository,
                                    MaintenanceTaskInstanceRepository instanceRepository,
                                    TaskStatusService statusService) {
        this.recordRepository = recordRepository;
        this.motorcycleRepository = motorcycleRepository;
        this.templateRepository = templateRepository;
        this.instanceRepository = instanceRepository;
        this.statusService = statusService;
    }

    public List<MaintenanceRecord> findByMotorcycle(Motorcycle motorcycle) {
        return recordRepository.findByMotorcycleOrderByDateDesc(motorcycle);
    }

    public MaintenanceRecord findById(Long id) { return recordRepository.findById(id).orElseThrow(); }

    @Transactional
    public MaintenanceRecord saveFromDto(MaintenanceRecordDto dto) {
        Motorcycle moto = motorcycleRepository.findById(dto.getMotorcycleId()).orElseThrow();
        MaintenanceRecord record = dto.getId() != null ? recordRepository.findById(dto.getId()).orElse(new MaintenanceRecord()) : new MaintenanceRecord();
        record.setMotorcycle(moto);
        record.setDate(dto.getDate());
        record.setOdometerKm(dto.getOdometerKm());
        record.setCategory(dto.getCategory());
        record.setTitle(dto.getTitle());
        record.setDescription(dto.getDescription());
        record.setPartsUsed(dto.getPartsUsed());
        record.setCostParts(dto.getCostParts());
        record.setCostLabor(dto.getCostLabor());
        record.setWorkshop(dto.getWorkshop());

        if (dto.getTemplateId() != null) {
            templateRepository.findById(dto.getTemplateId()).ifPresent(record::setTemplate);
        } else {
            record.setTemplate(null);
        }
        MaintenanceRecord saved = recordRepository.save(record);
        updateTaskInstancesAfterRecord(moto, record);
        return saved;
    }

    private void updateTaskInstancesAfterRecord(Motorcycle moto, MaintenanceRecord record) {
        List<MaintenanceTaskInstance> instances = instanceRepository.findByMotorcycle(moto);
        LocalDate recordDate = record.getDate();
        for (MaintenanceTaskInstance instance : instances) {
            MaintenanceTaskTemplate template = instance.getTemplate();
            if (record.getTemplate() != null && record.getTemplate().getId().equals(template.getId())) {
                if (template.getIntervalMonths() != null) {
                    instance.setNextDueDate(recordDate.plusMonths(template.getIntervalMonths()));
                }
                if (template.getIntervalKm() != null && record.getOdometerKm() != null) {
                    instance.setNextDueOdometerKm(record.getOdometerKm() + template.getIntervalKm());
                }
                instance.setStatus(statusService.calculateStatus(moto, instance));
            }
        }
        instanceRepository.saveAll(instances);
    }
}
