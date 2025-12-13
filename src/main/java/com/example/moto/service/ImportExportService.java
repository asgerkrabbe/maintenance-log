package com.example.moto.service;

import com.example.moto.entity.MaintenanceRecord;
import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.MaintenanceTaskTemplate;
import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MaintenanceRecordRepository;
import com.example.moto.repository.MaintenanceTaskInstanceRepository;
import com.example.moto.repository.MaintenanceTaskTemplateRepository;
import com.example.moto.repository.MotorcycleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class ImportExportService {
    private final MotorcycleRepository motorcycleRepository;
    private final MaintenanceTaskTemplateRepository templateRepository;
    private final MaintenanceTaskInstanceRepository taskInstanceRepository;
    private final MaintenanceRecordRepository recordRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ImportExportService(MotorcycleRepository motorcycleRepository,
                               MaintenanceTaskTemplateRepository templateRepository,
                               MaintenanceTaskInstanceRepository taskInstanceRepository,
                               MaintenanceRecordRepository recordRepository) {
        this.motorcycleRepository = motorcycleRepository;
        this.templateRepository = templateRepository;
        this.taskInstanceRepository = taskInstanceRepository;
        this.recordRepository = recordRepository;
    }

    public byte[] exportData() throws IOException {
        DataBundle bundle = new DataBundle();
        bundle.motorcycles = motorcycleRepository.findAll();
        bundle.templates = templateRepository.findAll();
        bundle.instances = taskInstanceRepository.findAll();
        bundle.records = recordRepository.findAll();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(bundle);
    }

    @Transactional
    public void importData(byte[] data) throws IOException {
        DataBundle bundle = objectMapper.readValue(data, DataBundle.class);
        recordRepository.deleteAll();
        taskInstanceRepository.deleteAll();
        templateRepository.deleteAll();
        motorcycleRepository.deleteAll();

        List<Motorcycle> savedMotos = motorcycleRepository.saveAll(bundle.motorcycles);
        List<MaintenanceTaskTemplate> savedTemplates = templateRepository.saveAll(bundle.templates);

        taskInstanceRepository.saveAll(bundle.instances);
        recordRepository.saveAll(bundle.records);
    }

    public static class DataBundle {
        public List<Motorcycle> motorcycles;
        public List<MaintenanceTaskTemplate> templates;
        public List<MaintenanceTaskInstance> instances;
        public List<MaintenanceRecord> records;
    }
}
