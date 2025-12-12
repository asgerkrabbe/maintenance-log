package com.example.moto.service;

import com.example.moto.dto.ExportData;
import com.example.moto.entity.MaintenanceRecord;
import com.example.moto.entity.MaintenanceStatus;
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

    public ExportData exportAll() {
        ExportData data = new ExportData();
        data.setMotorcycles(motorcycleRepository.findAll().stream().map(this::toExport).toList());
        data.setTemplates(templateRepository.findAll().stream().map(this::toExport).toList());
        data.setTaskInstances(instanceRepository.findAll().stream().map(this::toExport).toList());
        data.setRecords(recordRepository.findAll().stream().map(this::toExport).toList());
        return data;
    }

    @Transactional
    public void importAll(ExportData data) {
        recordRepository.deleteAll();
        instanceRepository.deleteAll();
        templateRepository.deleteAll();
        motorcycleRepository.deleteAll();

        Map<Long, Motorcycle> motoMap = new HashMap<>();
        if (data.getMotorcycles() != null) {
            for (ExportData.MotorcycleExport m : data.getMotorcycles()) {
                Motorcycle moto = new Motorcycle();
                moto.setId(m.getId());
                moto.setNickname(m.getNickname());
                moto.setMake(m.getMake());
                moto.setModel(m.getModel());
                moto.setYear(m.getYear());
                moto.setEngineSizeCc(m.getEngineSizeCc());
                moto.setVin(m.getVin());
                moto.setLicensePlate(m.getLicensePlate());
                moto.setPurchaseDate(m.getPurchaseDate());
                moto.setCurrentOdometerKm(m.getCurrentOdometerKm());
                moto.setNotes(m.getNotes());
                motoMap.put(m.getId(), motorcycleRepository.save(moto));
            }
        }

        Map<Long, MaintenanceTaskTemplate> templateMap = new HashMap<>();
        if (data.getTemplates() != null) {
            for (ExportData.MaintenanceTaskTemplateExport t : data.getTemplates()) {
                MaintenanceTaskTemplate template = new MaintenanceTaskTemplate();
                template.setId(t.getId());
                template.setName(t.getName());
                template.setDescription(t.getDescription());
                template.setIntervalKm(t.getIntervalKm());
                template.setIntervalMonths(t.getIntervalMonths());
                template.setDefaultCategory(t.getDefaultCategory());
                templateMap.put(t.getId(), templateRepository.save(template));
            }
        }

        Map<Long, MaintenanceTaskInstance> instanceMap = new HashMap<>();
        if (data.getTaskInstances() != null) {
            for (ExportData.MaintenanceTaskInstanceExport inst : data.getTaskInstances()) {
                MaintenanceTaskInstance instance = new MaintenanceTaskInstance();
                instance.setId(inst.getId());
                instance.setMotorcycle(motoMap.get(inst.getMotorcycleId()));
                instance.setTemplate(templateMap.get(inst.getTemplateId()));
                instance.setCreatedAt(inst.getCreatedAt());
                instance.setNextDueDate(inst.getNextDueDate());
                instance.setNextDueOdometerKm(inst.getNextDueOdometerKm());
                instance.setStatus(inst.getStatus() != null ? inst.getStatus() : MaintenanceStatus.OK);
                instanceMap.put(inst.getId(), instanceRepository.save(instance));
            }
        }

        if (data.getRecords() != null) {
            for (ExportData.MaintenanceRecordExport rec : data.getRecords()) {
                MaintenanceRecord record = new MaintenanceRecord();
                record.setId(rec.getId());
                record.setMotorcycle(motoMap.get(rec.getMotorcycleId()));
                record.setDate(rec.getDate());
                record.setOdometerKm(rec.getOdometerKm());
                record.setCategory(rec.getCategory());
                record.setTitle(rec.getTitle());
                record.setDescription(rec.getDescription());
                record.setPartsUsed(rec.getPartsUsed());
                record.setCostParts(rec.getCostParts());
                record.setCostLabor(rec.getCostLabor());
                record.setWorkshop(rec.getWorkshop());
                record.setCreatedAt(rec.getCreatedAt());
                recordRepository.save(record);
            }
        }
    }

    private ExportData.MotorcycleExport toExport(Motorcycle moto) {
        ExportData.MotorcycleExport exp = new ExportData.MotorcycleExport();
        exp.setId(moto.getId());
        exp.setNickname(moto.getNickname());
        exp.setMake(moto.getMake());
        exp.setModel(moto.getModel());
        exp.setYear(moto.getYear());
        exp.setEngineSizeCc(moto.getEngineSizeCc());
        exp.setVin(moto.getVin());
        exp.setLicensePlate(moto.getLicensePlate());
        exp.setPurchaseDate(moto.getPurchaseDate());
        exp.setCurrentOdometerKm(moto.getCurrentOdometerKm());
        exp.setNotes(moto.getNotes());
        return exp;
    }

    private ExportData.MaintenanceTaskTemplateExport toExport(MaintenanceTaskTemplate template) {
        ExportData.MaintenanceTaskTemplateExport exp = new ExportData.MaintenanceTaskTemplateExport();
        exp.setId(template.getId());
        exp.setName(template.getName());
        exp.setDescription(template.getDescription());
        exp.setIntervalKm(template.getIntervalKm());
        exp.setIntervalMonths(template.getIntervalMonths());
        exp.setDefaultCategory(template.getDefaultCategory());
        return exp;
    }

    private ExportData.MaintenanceTaskInstanceExport toExport(MaintenanceTaskInstance instance) {
        ExportData.MaintenanceTaskInstanceExport exp = new ExportData.MaintenanceTaskInstanceExport();
        exp.setId(instance.getId());
        exp.setMotorcycleId(instance.getMotorcycle().getId());
        exp.setTemplateId(instance.getTemplate().getId());
        exp.setCreatedAt(instance.getCreatedAt());
        exp.setNextDueDate(instance.getNextDueDate());
        exp.setNextDueOdometerKm(instance.getNextDueOdometerKm());
        exp.setStatus(instance.getStatus());
        return exp;
    }

    private ExportData.MaintenanceRecordExport toExport(MaintenanceRecord record) {
        ExportData.MaintenanceRecordExport exp = new ExportData.MaintenanceRecordExport();
        exp.setId(record.getId());
        exp.setMotorcycleId(record.getMotorcycle().getId());
        exp.setDate(record.getDate());
        exp.setOdometerKm(record.getOdometerKm());
        exp.setCategory(record.getCategory());
        exp.setTitle(record.getTitle());
        exp.setDescription(record.getDescription());
        exp.setPartsUsed(record.getPartsUsed());
        exp.setCostParts(record.getCostParts());
        exp.setCostLabor(record.getCostLabor());
        exp.setWorkshop(record.getWorkshop());
        exp.setCreatedAt(record.getCreatedAt());
        return exp;
    }
}
