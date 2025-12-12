package com.example.moto.service;

import com.example.moto.entity.MaintenanceRecord;
import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.MaintenanceTaskTemplate;
import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MaintenanceRecordRepository;
import com.example.moto.repository.MaintenanceTaskInstanceRepository;
import com.example.moto.repository.MaintenanceTaskTemplateRepository;
import com.example.moto.repository.MotorcycleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImportExportService {
    private final MotorcycleRepository motorcycleRepository;
    private final MaintenanceTaskTemplateRepository templateRepository;
    private final MaintenanceTaskInstanceRepository instanceRepository;
    private final MaintenanceRecordRepository recordRepository;

    public ImportExportService(MotorcycleRepository motorcycleRepository,
                               MaintenanceTaskTemplateRepository templateRepository,
                               MaintenanceTaskInstanceRepository instanceRepository,
                               MaintenanceRecordRepository recordRepository) {
        this.motorcycleRepository = motorcycleRepository;
        this.templateRepository = templateRepository;
        this.instanceRepository = instanceRepository;
        this.recordRepository = recordRepository;
    }

    public Map<String, Object> exportAll() {
        Map<String, Object> data = new HashMap<>();
        data.put("motorcycles", motorcycleRepository.findAll());
        data.put("templates", templateRepository.findAll());
        data.put("taskInstances", instanceRepository.findAll());
        data.put("records", recordRepository.findAll());
        return data;
    }

    @Transactional
    public void importData(Map<String, List<?>> payload) {
        instanceRepository.deleteAll();
        recordRepository.deleteAll();
        templateRepository.deleteAll();
        motorcycleRepository.deleteAll();

        List<?> motorcycles = payload.getOrDefault("motorcycles", List.of());
        motorcycles.forEach(item -> motorcycleRepository.save((Motorcycle) item));

        List<?> templates = payload.getOrDefault("templates", List.of());
        templates.forEach(item -> templateRepository.save((MaintenanceTaskTemplate) item));

        List<?> instances = payload.getOrDefault("taskInstances", List.of());
        instances.forEach(item -> instanceRepository.save((MaintenanceTaskInstance) item));

        List<?> records = payload.getOrDefault("records", List.of());
        records.forEach(item -> recordRepository.save((MaintenanceRecord) item));
    }
}
