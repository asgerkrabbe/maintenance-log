package com.example.moto.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class TemplateForm {
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @Min(0)
    private Integer intervalKm;

    @Min(0)
    private Integer intervalMonths;

    private String defaultCategory;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getIntervalKm() { return intervalKm; }
    public void setIntervalKm(Integer intervalKm) { this.intervalKm = intervalKm; }
    public Integer getIntervalMonths() { return intervalMonths; }
    public void setIntervalMonths(Integer intervalMonths) { this.intervalMonths = intervalMonths; }
    public String getDefaultCategory() { return defaultCategory; }
    public void setDefaultCategory(String defaultCategory) { this.defaultCategory = defaultCategory; }
}
