package com.example.moto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Motorcycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nickname;
    @NotBlank
    private String make;
    @NotBlank
    private String model;
    @NotNull
    private Integer year;
    private Integer engineSizeCc;
    private String vin;
    private String licensePlate;
    private LocalDate purchaseDate;
    @Min(0)
    private Integer currentOdometerKm = 0;
    @Lob
    private String notes;

    // getters and setters
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
