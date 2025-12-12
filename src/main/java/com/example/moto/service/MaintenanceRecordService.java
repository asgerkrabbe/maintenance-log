package com.example.moto.service;

import com.example.moto.dto.MaintenanceRecordDto;
import com.example.moto.entity.MaintenanceRecord;
import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.MaintenanceTaskTemplate;
import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MaintenanceRecordRepository;
import com.example.moto.repository.MaintenanceTaskInstanceRepository;
import com.example.moto.repository.MotorcycleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MaintenanceRecordService {
    private final MaintenanceRecordRepository recordRepository;
    private final MotorcycleRepository motorcycleRepository;
    private final MaintenanceTaskInstanceRepository instanceRepository;
    private final MaintenanceTaskService maintenanceTaskService;

    public MaintenanceRecordService(MaintenanceRecordRepository recordRepository,
                                    MotorcycleRepository motorcycleRepository,
                                    MaintenanceTaskInstanceRepository instanceRepository,
                                    MaintenanceTaskService maintenanceTaskService) {
        this.recordRepository = recordRepository;
        this.motorcycleRepository = motorcycleRepository;
        this.instanceRepository = instanceRepository;
        this.maintenanceTaskService = maintenanceTaskService;
    }

    public List<MaintenanceRecord> findByMotorcycle(Long motorcycleId, String category) {
        if (category != null && !category.isBlank()) {
            return recordRepository.findByMotorcycleIdAndCategoryOrderByDateDesc(motorcycleId, category);
        }
        return recordRepository.findByMotorcycleIdOrderByDateDesc(motorcycleId);
    }

    @Transactional
    public MaintenanceRecord save(MaintenanceRecordDto dto) {
        Motorcycle motorcycle = motorcycleRepository.findById(dto.getMotorcycleId())
                .orElseThrow(() -> new IllegalArgumentException("Motorcycle not found"));
        MaintenanceRecord record = new MaintenanceRecord();
        record.setMotorcycle(motorcycle);
        record.setDate(dto.getDate());
        record.setOdometerKm(dto.getOdometerKm());
        record.setCategory(dto.getCategory());
        record.setTitle(dto.getTitle());
        record.setDescription(dto.getDescription());
        record.setPartsUsed(dto.getPartsUsed());
        record.setCostParts(dto.getCostParts());
        record.setCostLabor(dto.getCostLabor());
        record.setWorkshop(dto.getWorkshop());
        MaintenanceRecord saved = recordRepository.save(record);

        if (dto.getTemplateInstanceId() != null) {
            MaintenanceTaskInstance instance = instanceRepository.findById(dto.getTemplateInstanceId())
                    .orElseThrow(() -> new IllegalArgumentException("Task instance not found"));
            MaintenanceTaskTemplate template = instance.getTemplate();
            if (template.getIntervalMonths() != null && dto.getDate() != null) {
                instance.setNextDueDate(dto.getDate().plusMonths(template.getIntervalMonths()));
            }
            if (template.getIntervalKm() != null && dto.getOdometerKm() != null) {
                instance.setNextDueOdometerKm(dto.getOdometerKm() + template.getIntervalKm());
            }
            maintenanceTaskService.updateStatus(instance);
        }
        maintenanceTaskService.refreshStatusesForMotorcycle(motorcycle);
        return saved;
    }
}
