package com.example.moto.service;

import com.example.moto.dto.AttachTemplateDto;
import com.example.moto.entity.MaintenanceStatus;
import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.MaintenanceTaskTemplate;
import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MaintenanceTaskInstanceRepository;
import com.example.moto.repository.MaintenanceTaskTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class TaskInstanceService {
    private final MaintenanceTaskInstanceRepository repository;
    private final MaintenanceTaskTemplateRepository templateRepository;
    private final StatusService statusService;

    public TaskInstanceService(MaintenanceTaskInstanceRepository repository,
                               MaintenanceTaskTemplateRepository templateRepository,
                               StatusService statusService) {
        this.repository = repository;
        this.templateRepository = templateRepository;
        this.statusService = statusService;
    }

    public List<MaintenanceTaskInstance> findAll() {
        return repository.findAll().stream()
                .sorted((a, b) -> {
                    int statusOrderA = order(a.getStatus());
                    int statusOrderB = order(b.getStatus());
                    if (statusOrderA != statusOrderB) return Integer.compare(statusOrderA, statusOrderB);
                    int dateCompare = Comparator.nullsLast(LocalDate::compareTo)
                            .compare(a.getNextDueDate(), b.getNextDueDate());
                    if (dateCompare != 0) return dateCompare;
                    return Comparator.nullsLast(Integer::compareTo)
                            .compare(a.getNextDueOdometerKm(), b.getNextDueOdometerKm());
                })
                .toList();
    }

    public List<MaintenanceTaskInstance> findByMotorcycle(Motorcycle motorcycle) {
        return repository.findByMotorcycleOrderByStatusAsc(motorcycle);
    }

    private int order(MaintenanceStatus status) {
        return switch (status) {
            case OVERDUE -> 0;
            case DUE_SOON -> 1;
            default -> 2;
        };
    }

    @Transactional
    public MaintenanceTaskInstance attachTemplate(Motorcycle motorcycle, AttachTemplateDto dto) {
        MaintenanceTaskTemplate template = templateRepository.findById(dto.getTemplateId()).orElseThrow();
        MaintenanceTaskInstance instance = new MaintenanceTaskInstance();
        instance.setMotorcycle(motorcycle);
        instance.setTemplate(template);
        if (template.getIntervalKm() != null && motorcycle.getCurrentOdometerKm() != null) {
            instance.setNextDueOdometerKm(motorcycle.getCurrentOdometerKm() + template.getIntervalKm());
        }
        if (template.getIntervalMonths() != null) {
            instance.setNextDueDate(LocalDate.now().plusMonths(template.getIntervalMonths()));
        }
        instance.setStatus(statusService.computeStatus(instance));
        return repository.save(instance);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public MaintenanceTaskInstance save(MaintenanceTaskInstance instance) {
        instance.setStatus(statusService.computeStatus(instance));
        return repository.save(instance);
    }
}
