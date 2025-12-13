package com.example.moto.dto;

import com.example.moto.entity.MaintenanceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ExportData {
    private List<MotorcycleExport> motorcycles;
    private List<MaintenanceTaskTemplateExport> templates;
    private List<MaintenanceTaskInstanceExport> taskInstances;
    private List<MaintenanceRecordExport> records;

    public List<MotorcycleExport> getMotorcycles() {
        return motorcycles;
    }

    public void setMotorcycles(List<MotorcycleExport> motorcycles) {
        this.motorcycles = motorcycles;
    }

    public List<MaintenanceTaskTemplateExport> getTemplates() {
        return templates;
    }

    public void setTemplates(List<MaintenanceTaskTemplateExport> templates) {
        this.templates = templates;
    }

    public List<MaintenanceTaskInstanceExport> getTaskInstances() {
        return taskInstances;
    }

    public void setTaskInstances(List<MaintenanceTaskInstanceExport> taskInstances) {
        this.taskInstances = taskInstances;
    }

    public List<MaintenanceRecordExport> getRecords() {
        return records;
    }

    public void setRecords(List<MaintenanceRecordExport> records) {
        this.records = records;
    }

    public static class MotorcycleExport {
        private Long id;
        private String nickname;
        private String make;
        private String model;
        private Integer year;
        private Integer engineSizeCc;
        private String vin;
        private String licensePlate;
        private LocalDate purchaseDate;
        private Integer currentOdometerKm;
        private String notes;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public Integer getEngineSizeCc() {
            return engineSizeCc;
        }

        public void setEngineSizeCc(Integer engineSizeCc) {
            this.engineSizeCc = engineSizeCc;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public String getLicensePlate() {
            return licensePlate;
        }

        public void setLicensePlate(String licensePlate) {
            this.licensePlate = licensePlate;
        }

        public LocalDate getPurchaseDate() {
            return purchaseDate;
        }

        public void setPurchaseDate(LocalDate purchaseDate) {
            this.purchaseDate = purchaseDate;
        }

        public Integer getCurrentOdometerKm() {
            return currentOdometerKm;
        }

        public void setCurrentOdometerKm(Integer currentOdometerKm) {
            this.currentOdometerKm = currentOdometerKm;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    public static class MaintenanceTaskTemplateExport {
        private Long id;
        private String name;
        private String description;
        private Integer intervalKm;
        private Integer intervalMonths;
        private String defaultCategory;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getIntervalKm() {
            return intervalKm;
        }

        public void setIntervalKm(Integer intervalKm) {
            this.intervalKm = intervalKm;
        }

        public Integer getIntervalMonths() {
            return intervalMonths;
        }

        public void setIntervalMonths(Integer intervalMonths) {
            this.intervalMonths = intervalMonths;
        }

        public String getDefaultCategory() {
            return defaultCategory;
        }

        public void setDefaultCategory(String defaultCategory) {
            this.defaultCategory = defaultCategory;
        }
    }

    public static class MaintenanceTaskInstanceExport {
        private Long id;
        private Long motorcycleId;
        private Long templateId;
        private LocalDateTime createdAt;
        private Integer nextDueOdometerKm;
        private LocalDate nextDueDate;
        private MaintenanceStatus status;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getMotorcycleId() {
            return motorcycleId;
        }

        public void setMotorcycleId(Long motorcycleId) {
            this.motorcycleId = motorcycleId;
        }

        public Long getTemplateId() {
            return templateId;
        }

        public void setTemplateId(Long templateId) {
            this.templateId = templateId;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getNextDueOdometerKm() {
            return nextDueOdometerKm;
        }

        public void setNextDueOdometerKm(Integer nextDueOdometerKm) {
            this.nextDueOdometerKm = nextDueOdometerKm;
        }

        public LocalDate getNextDueDate() {
            return nextDueDate;
        }

        public void setNextDueDate(LocalDate nextDueDate) {
            this.nextDueDate = nextDueDate;
        }

        public MaintenanceStatus getStatus() {
            return status;
        }

        public void setStatus(MaintenanceStatus status) {
            this.status = status;
        }
    }

    public static class MaintenanceRecordExport {
        private Long id;
        private Long motorcycleId;
        private LocalDate date;
        private Integer odometerKm;
        private String category;
        private String title;
        private String description;
        private String partsUsed;
        private BigDecimal costParts;
        private BigDecimal costLabor;
        private String workshop;
        private LocalDateTime createdAt;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getMotorcycleId() {
            return motorcycleId;
        }

        public void setMotorcycleId(Long motorcycleId) {
            this.motorcycleId = motorcycleId;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public Integer getOdometerKm() {
            return odometerKm;
        }

        public void setOdometerKm(Integer odometerKm) {
            this.odometerKm = odometerKm;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPartsUsed() {
            return partsUsed;
        }

        public void setPartsUsed(String partsUsed) {
            this.partsUsed = partsUsed;
        }

        public BigDecimal getCostParts() {
            return costParts;
        }

        public void setCostParts(BigDecimal costParts) {
            this.costParts = costParts;
        }

        public BigDecimal getCostLabor() {
            return costLabor;
        }

        public void setCostLabor(BigDecimal costLabor) {
            this.costLabor = costLabor;
        }

        public String getWorkshop() {
            return workshop;
        }

        public void setWorkshop(String workshop) {
            this.workshop = workshop;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }
}
