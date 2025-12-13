package com.example.moto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class MaintenanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Motorcycle motorcycle;

    @NotNull
    private LocalDate date;
    @NotNull
    private Integer odometerKm;
    private String category;
    @NotBlank
    private String title;
    @Lob
    private String description;
    @Lob
    private String partsUsed;
    private BigDecimal costParts;
    private BigDecimal costLabor;
    private String workshop;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    private MaintenanceTaskTemplate template;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Motorcycle getMotorcycle() {
        return motorcycle;
    }

    public void setMotorcycle(Motorcycle motorcycle) {
        this.motorcycle = motorcycle;
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

    public MaintenanceTaskTemplate getTemplate() {
        return template;
    }

    public void setTemplate(MaintenanceTaskTemplate template) {
        this.template = template;
    }
}
