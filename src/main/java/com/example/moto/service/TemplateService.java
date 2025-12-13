package com.example.moto.service;

import com.example.moto.dto.TemplateDto;
import com.example.moto.entity.MaintenanceTaskTemplate;
import com.example.moto.repository.MaintenanceTaskTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemplateService {
    private final MaintenanceTaskTemplateRepository repository;

    public TemplateService(MaintenanceTaskTemplateRepository repository) {
        this.repository = repository;
    }

    public List<MaintenanceTaskTemplate> findAll() {
        return repository.findAll();
    }

    public Optional<MaintenanceTaskTemplate> findById(Long id) {
        return repository.findById(id);
    }

    public MaintenanceTaskTemplate saveFromDto(TemplateDto dto) {
        MaintenanceTaskTemplate template = dto.getId() != null ? repository.findById(dto.getId()).orElse(new MaintenanceTaskTemplate()) : new MaintenanceTaskTemplate();
        template.setName(dto.getName());
        template.setDescription(dto.getDescription());
        template.setIntervalKm(dto.getIntervalKm());
        template.setIntervalMonths(dto.getIntervalMonths());
        template.setDefaultCategory(dto.getDefaultCategory());
        return repository.save(template);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
