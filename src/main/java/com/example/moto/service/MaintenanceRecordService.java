package com.example.moto.service;

import com.example.moto.entity.MaintenanceRecord;
import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.MaintenanceTaskTemplate;
import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MaintenanceRecordRepository;
import com.example.moto.repository.MaintenanceTaskInstanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MaintenanceRecordService {
    private final MaintenanceRecordRepository recordRepository;
    private final MaintenanceTaskInstanceRepository taskInstanceRepository;
    private final MaintenanceTaskService maintenanceTaskService;

    public MaintenanceRecordService(MaintenanceRecordRepository recordRepository,
                                    MaintenanceTaskInstanceRepository taskInstanceRepository,
                                    MaintenanceTaskService maintenanceTaskService) {
        this.recordRepository = recordRepository;
        this.taskInstanceRepository = taskInstanceRepository;
        this.maintenanceTaskService = maintenanceTaskService;
    }

    public List<MaintenanceRecord> listByMotorcycle(Motorcycle motorcycle) {
        return recordRepository.findByMotorcycleOrderByDateDesc(motorcycle);
    }

    public MaintenanceRecord saveRecord(MaintenanceRecord record, MaintenanceTaskTemplate template) {
        record.setCreatedAt(LocalDateTime.now());
        record.setTemplate(template);
        MaintenanceRecord saved = recordRepository.save(record);
        if (template != null) {
            Optional<MaintenanceTaskInstance> instanceOpt = taskInstanceRepository.findByMotorcycle(record.getMotorcycle()).stream()
                    .filter(i -> i.getTemplate().getId().equals(template.getId()))
                    .findFirst();
            instanceOpt.ifPresent(instance -> updateInstanceFromRecord(instance, record));
        }
        return saved;
    }

    private void updateInstanceFromRecord(MaintenanceTaskInstance instance, MaintenanceRecord record) {
        MaintenanceTaskTemplate template = instance.getTemplate();
        if (template.getIntervalMonths() != null && record.getDate() != null) {
            instance.setNextDueDate(record.getDate().plusMonths(template.getIntervalMonths()));
        }
        if (template.getIntervalKm() != null && record.getOdometerKm() != null) {
            instance.setNextDueOdometerKm(record.getOdometerKm() + template.getIntervalKm());
        }
        maintenanceTaskService.updateStatus(instance);
        taskInstanceRepository.save(instance);
    }
}
