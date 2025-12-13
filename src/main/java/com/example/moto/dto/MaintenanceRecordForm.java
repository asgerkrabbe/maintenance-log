package com.example.moto.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MaintenanceRecordForm {
    private Long motorcycleId;

    @NotNull
    private LocalDate date;

    @Min(0)
    private Integer odometerKm;

    private String category;

    @NotBlank
    private String title;

    private String description;

    private String partsUsed;

    private BigDecimal costParts;

    private BigDecimal costLabor;

    private String workshop;

    private Long templateId;

    public Long getMotorcycleId() { return motorcycleId; }
    public void setMotorcycleId(Long motorcycleId) { this.motorcycleId = motorcycleId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Integer getOdometerKm() { return odometerKm; }
    public void setOdometerKm(Integer odometerKm) { this.odometerKm = odometerKm; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPartsUsed() { return partsUsed; }
    public void setPartsUsed(String partsUsed) { this.partsUsed = partsUsed; }
    public BigDecimal getCostParts() { return costParts; }
    public void setCostParts(BigDecimal costParts) { this.costParts = costParts; }
    public BigDecimal getCostLabor() { return costLabor; }
    public void setCostLabor(BigDecimal costLabor) { this.costLabor = costLabor; }
    public String getWorkshop() { return workshop; }
    public void setWorkshop(String workshop) { this.workshop = workshop; }
    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }
}
