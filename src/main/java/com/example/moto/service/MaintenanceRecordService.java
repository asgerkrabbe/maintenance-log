package com.example.moto.service;

import com.example.moto.dto.MaintenanceRecordDto;
import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.MaintenanceTaskTemplate;
import com.example.moto.entity.Motorcycle;
import com.example.moto.entity.MaintenanceRecord;
import com.example.moto.repository.MaintenanceRecordRepository;
import com.example.moto.repository.MaintenanceTaskInstanceRepository;
import com.example.moto.repository.MaintenanceTaskTemplateRepository;
import com.example.moto.repository.MotorcycleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MaintenanceRecordService {
    private final MaintenanceRecordRepository recordRepository;
    private final MotorcycleRepository motorcycleRepository;
    private final MaintenanceTaskTemplateRepository templateRepository;
    private final MaintenanceTaskInstanceRepository instanceRepository;
    private final StatusService statusService;

    public MaintenanceRecordService(MaintenanceRecordRepository recordRepository,
                                    MotorcycleRepository motorcycleRepository,
                                    MaintenanceTaskTemplateRepository templateRepository,
                                    MaintenanceTaskInstanceRepository instanceRepository,
                                    StatusService statusService) {
        this.recordRepository = recordRepository;
        this.motorcycleRepository = motorcycleRepository;
        this.templateRepository = templateRepository;
        this.instanceRepository = instanceRepository;
        this.statusService = statusService;
    }

    public List<MaintenanceRecord> findByMotorcycle(Motorcycle motorcycle, String categoryFilter) {
        if (categoryFilter != null && !categoryFilter.isBlank()) {
            return recordRepository.findByMotorcycleAndCategoryIgnoreCaseContainingOrderByDateDesc(motorcycle, categoryFilter);
        }
        return recordRepository.findByMotorcycleOrderByDateDesc(motorcycle);
    }

    @Transactional
    public MaintenanceRecord create(MaintenanceRecordDto dto) {
        Motorcycle motorcycle = motorcycleRepository.findById(dto.getMotorcycleId()).orElseThrow();
        MaintenanceTaskTemplate template = dto.getTemplateId() != null ?
                templateRepository.findById(dto.getTemplateId()).orElse(null) : null;

        MaintenanceRecord record = new MaintenanceRecord();
        record.setMotorcycle(motorcycle);
        record.setTemplate(template);
        record.setDate(dto.getDate());
        record.setOdometerKm(dto.getOdometerKm());
        record.setCategory(dto.getCategory() != null ? dto.getCategory() : (template != null ? template.getDefaultCategory() : null));
        record.setTitle(dto.getTitle());
        record.setDescription(dto.getDescription());
        record.setPartsUsed(dto.getPartsUsed());
        record.setCostParts(dto.getCostParts());
        record.setCostLabor(dto.getCostLabor());
        record.setWorkshop(dto.getWorkshop());

        MaintenanceRecord saved = recordRepository.save(record);
        updateTaskInstanceAfterRecord(motorcycle, template, saved);
        return saved;
    }

    private void updateTaskInstanceAfterRecord(Motorcycle motorcycle, MaintenanceTaskTemplate template, MaintenanceRecord record) {
        if (template == null) {
            return;
        }
        instanceRepository.findByMotorcycle(motorcycle).stream()
                .filter(inst -> inst.getTemplate().getId().equals(template.getId()))
                .forEach(inst -> {
                    if (template.getIntervalMonths() != null) {
                        inst.setNextDueDate(record.getDate().plusMonths(template.getIntervalMonths()));
                    }
                    if (template.getIntervalKm() != null) {
                        inst.setNextDueOdometerKm(record.getOdometerKm() + template.getIntervalKm());
                    }
                    inst.setStatus(statusService.computeStatus(inst));
                    instanceRepository.save(inst);
                });
    }
}
