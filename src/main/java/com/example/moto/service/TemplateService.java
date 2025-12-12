package com.example.moto.service;

import com.example.moto.entity.MaintenanceTaskTemplate;
import com.example.moto.repository.MaintenanceTaskTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TemplateService {
    private final MaintenanceTaskTemplateRepository templateRepository;

    public TemplateService(MaintenanceTaskTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public List<MaintenanceTaskTemplate> findAll() { return templateRepository.findAll(); }

    public Optional<MaintenanceTaskTemplate> findById(Long id) { return templateRepository.findById(id); }

    public MaintenanceTaskTemplate save(MaintenanceTaskTemplate template) { return templateRepository.save(template); }

    public void delete(Long id) { templateRepository.deleteById(id); }
}
