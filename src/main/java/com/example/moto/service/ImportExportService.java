package com.example.moto.service;

import com.example.moto.dto.DataExport;
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

    public DataExport exportAll() {
        DataExport data = new DataExport();
        data.setMotorcycles(motorcycleRepository.findAll());
        data.setTemplates(templateRepository.findAll());
        data.setTaskInstances(instanceRepository.findAll());
        data.setRecords(recordRepository.findAll());
        return data;
    }

    @Transactional
    public void importAll(DataExport data) {
        instanceRepository.deleteAll();
        recordRepository.deleteAll();
        templateRepository.deleteAll();
        motorcycleRepository.deleteAll();

        if (data.getMotorcycles() != null) {
            for (Motorcycle m : data.getMotorcycles()) {
                motorcycleRepository.save(m);
            }
        }
        if (data.getTemplates() != null) {
            for (MaintenanceTaskTemplate t : data.getTemplates()) {
                templateRepository.save(t);
            }
        }
        if (data.getTaskInstances() != null) {
            for (MaintenanceTaskInstance i : data.getTaskInstances()) {
                instanceRepository.save(i);
            }
        }
        if (data.getRecords() != null) {
            for (MaintenanceRecord r : data.getRecords()) {
                recordRepository.save(r);
            }
        }
    }
}
