package com.example.moto.service;

import com.example.moto.dto.AttachTemplateDto;
import com.example.moto.entity.MaintenanceStatus;
import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.MaintenanceTaskTemplate;
import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MaintenanceTaskInstanceRepository;
import com.example.moto.repository.MaintenanceTaskTemplateRepository;
import com.example.moto.repository.MotorcycleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceTaskService {
    private final MaintenanceTaskInstanceRepository instanceRepository;
    private final MaintenanceTaskTemplateRepository templateRepository;
    private final MotorcycleRepository motorcycleRepository;

    public MaintenanceTaskService(MaintenanceTaskInstanceRepository instanceRepository,
                                  MaintenanceTaskTemplateRepository templateRepository,
                                  MotorcycleRepository motorcycleRepository) {
        this.instanceRepository = instanceRepository;
        this.templateRepository = templateRepository;
        this.motorcycleRepository = motorcycleRepository;
    }

    @Transactional
    public MaintenanceTaskInstance attachTemplate(Long motorcycleId, AttachTemplateDto dto) {
        Motorcycle motorcycle = motorcycleRepository.findById(motorcycleId)
                .orElseThrow(() -> new IllegalArgumentException("Motorcycle not found"));
        MaintenanceTaskTemplate template = templateRepository.findById(dto.getTemplateId())
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));

        MaintenanceTaskInstance instance = new MaintenanceTaskInstance();
        instance.setMotorcycle(motorcycle);
        instance.setTemplate(template);
        if (template.getIntervalKm() != null) {
            instance.setNextDueOdometerKm(motorcycle.getCurrentOdometerKm() + template.getIntervalKm());
        }
        if (template.getIntervalMonths() != null) {
            instance.setNextDueDate(LocalDate.now().plusMonths(template.getIntervalMonths()));
        }
        instance.setStatus(MaintenanceStatus.OK);
        MaintenanceTaskInstance saved = instanceRepository.save(instance);
        updateStatus(saved);
        return saved;
    }

    public List<MaintenanceTaskInstance> findUpcomingForMotorcycle(Long motorcycleId) {
        return instanceRepository.findByMotorcycleIdOrderByStatusAscNextDueDateAsc(motorcycleId);
    }

    public List<MaintenanceTaskInstance> findAllOrdered() {
        return instanceRepository.findAll().stream()
                .sorted(Comparator.comparing(MaintenanceTaskInstance::getStatus)
                        .thenComparing(MaintenanceTaskInstance::getNextDueDate, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(MaintenanceTaskInstance::getNextDueOdometerKm, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    public Optional<MaintenanceTaskInstance> findById(Long id) {
        return instanceRepository.findById(id);
    }

    @Transactional
    public void updateStatus(MaintenanceTaskInstance instance) {
        Motorcycle moto = instance.getMotorcycle();
        LocalDate today = LocalDate.now();
        boolean overdue = false;
        if (instance.getNextDueDate() != null && today.isAfter(instance.getNextDueDate())) {
            overdue = true;
        }
        if (!overdue && instance.getNextDueOdometerKm() != null &&
                moto.getCurrentOdometerKm() != null && moto.getCurrentOdometerKm() > instance.getNextDueOdometerKm()) {
            overdue = true;
        }
        if (overdue) {
            instance.setStatus(MaintenanceStatus.OVERDUE);
        } else {
            boolean dueSoon = false;
            if (instance.getNextDueDate() != null) {
                long days = ChronoUnit.DAYS.between(today, instance.getNextDueDate());
                if (days <= 30 && days >= 0) {
                    dueSoon = true;
                }
            }
            if (instance.getNextDueOdometerKm() != null && moto.getCurrentOdometerKm() != null) {
                int diff = instance.getNextDueOdometerKm() - moto.getCurrentOdometerKm();
                if (diff <= 500 && diff >= 0) {
                    dueSoon = true;
                }
            }
            instance.setStatus(dueSoon ? MaintenanceStatus.DUE_SOON : MaintenanceStatus.OK);
        }
        instanceRepository.save(instance);
    }

    @Transactional
    public void refreshStatusesForMotorcycle(Motorcycle motorcycle) {
        List<MaintenanceTaskInstance> instances = instanceRepository.findByMotorcycleId(motorcycle.getId());
        instances.forEach(this::updateStatus);
    }

    @Transactional
    public void deleteInstance(Long id) {
        instanceRepository.deleteById(id);
    }
}
