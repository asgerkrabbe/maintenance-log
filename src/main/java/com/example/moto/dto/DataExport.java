package com.example.moto.dto;

import com.example.moto.entity.MaintenanceRecord;
import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.MaintenanceTaskTemplate;
import com.example.moto.entity.Motorcycle;

import java.util.List;

public class DataExport {
    private List<Motorcycle> motorcycles;
    private List<MaintenanceTaskTemplate> templates;
    private List<MaintenanceTaskInstance> taskInstances;
    private List<MaintenanceRecord> records;

    public List<Motorcycle> getMotorcycles() {
        return motorcycles;
    }

    public void setMotorcycles(List<Motorcycle> motorcycles) {
        this.motorcycles = motorcycles;
    }

    public List<MaintenanceTaskTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<MaintenanceTaskTemplate> templates) {
        this.templates = templates;
    }

    public List<MaintenanceTaskInstance> getTaskInstances() {
        return taskInstances;
    }

    public void setTaskInstances(List<MaintenanceTaskInstance> taskInstances) {
        this.taskInstances = taskInstances;
    }

    public List<MaintenanceRecord> getRecords() {
        return records;
    }

    public void setRecords(List<MaintenanceRecord> records) {
        this.records = records;
    }
}
