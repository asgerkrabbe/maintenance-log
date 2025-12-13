package com.example.moto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_task_instances")
public class MaintenanceTaskInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Motorcycle motorcycle;

    @ManyToOne(optional = false)
    private MaintenanceTaskTemplate template;

    private LocalDateTime createdAt;

    @Positive
    private Integer nextDueOdometerKm;

    private LocalDate nextDueDate;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status = MaintenanceStatus.OK;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

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

    public MaintenanceTaskTemplate getTemplate() {
        return template;
    }

    public void setTemplate(MaintenanceTaskTemplate template) {
        this.template = template;
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
