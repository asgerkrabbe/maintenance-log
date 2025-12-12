package com.example.moto.dto;

import jakarta.validation.constraints.NotNull;

public class AttachTemplateDto {
    @NotNull
    private Long templateId;

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
}
