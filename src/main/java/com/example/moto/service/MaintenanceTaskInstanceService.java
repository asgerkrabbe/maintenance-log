package com.example.moto.service;

import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.MaintenanceTaskTemplate;
import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MaintenanceTaskInstanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceTaskInstanceService {
    private final MaintenanceTaskInstanceRepository repository;
    private final TaskStatusService statusService;

    public MaintenanceTaskInstanceService(MaintenanceTaskInstanceRepository repository, TaskStatusService statusService) {
        this.repository = repository;
        this.statusService = statusService;
    }

    public List<MaintenanceTaskInstance> findAll() {
        return repository.findAll();
    }

    public Optional<MaintenanceTaskInstance> findById(Long id) { return repository.findById(id); }

    @Transactional
    public MaintenanceTaskInstance attachTemplate(Motorcycle motorcycle, MaintenanceTaskTemplate template) {
        MaintenanceTaskInstance instance = new MaintenanceTaskInstance();
        instance.setMotorcycle(motorcycle);
        instance.setTemplate(template);
        if (template.getIntervalKm() != null && motorcycle.getCurrentOdometerKm() != null) {
            instance.setNextDueOdometerKm(motorcycle.getCurrentOdometerKm() + template.getIntervalKm());
        }
        if (template.getIntervalMonths() != null) {
            instance.setNextDueDate(LocalDate.now().plusMonths(template.getIntervalMonths()));
        }
        instance.setStatus(statusService.calculateStatus(motorcycle, instance));
        return repository.save(instance);
    }

    @Transactional
    public MaintenanceTaskInstance save(MaintenanceTaskInstance instance) {
        instance.setStatus(statusService.calculateStatus(instance.getMotorcycle(), instance));
        return repository.save(instance);
    }

    public List<MaintenanceTaskInstance> findByMotorcycle(Motorcycle moto) {
        return repository.findByMotorcycle(moto);
    }

    public Optional<MaintenanceTaskInstance> findUpcomingForMoto(Motorcycle moto) {
        return repository.findByMotorcycle(moto).stream()
                .min(Comparator.comparing(MaintenanceTaskInstance::getNextDueDate,
                        Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(MaintenanceTaskInstance::getNextDueOdometerKm,
                                Comparator.nullsLast(Comparator.naturalOrder())));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
