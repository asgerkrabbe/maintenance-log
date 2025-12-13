package com.example.moto.service;

import com.example.moto.dto.MaintenanceTaskTemplateDto;
import com.example.moto.entity.MaintenanceTaskTemplate;
import com.example.moto.repository.MaintenanceTaskTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {
    private final MaintenanceTaskTemplateRepository repository;

    public TemplateService(MaintenanceTaskTemplateRepository repository) {
        this.repository = repository;
    }

    public List<MaintenanceTaskTemplate> findAll() {
        return repository.findAll();
    }

    public MaintenanceTaskTemplate findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public MaintenanceTaskTemplate save(MaintenanceTaskTemplateDto dto) {
        MaintenanceTaskTemplate template = new MaintenanceTaskTemplate();
        apply(dto, template);
        return repository.save(template);
    }

    public MaintenanceTaskTemplate update(Long id, MaintenanceTaskTemplateDto dto) {
        MaintenanceTaskTemplate template = findById(id);
        apply(dto, template);
        return repository.save(template);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private void apply(MaintenanceTaskTemplateDto dto, MaintenanceTaskTemplate template) {
        template.setName(dto.getName());
        template.setDescription(dto.getDescription());
        template.setIntervalKm(dto.getIntervalKm());
        template.setIntervalMonths(dto.getIntervalMonths());
        template.setDefaultCategory(dto.getDefaultCategory());
    }
}
