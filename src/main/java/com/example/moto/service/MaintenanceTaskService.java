package com.example.moto.service;

import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.MaintenanceTaskTemplate;
import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MaintenanceTaskInstanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MaintenanceTaskService {
    private final MaintenanceTaskInstanceRepository taskInstanceRepository;

    public MaintenanceTaskService(MaintenanceTaskInstanceRepository taskInstanceRepository) {
        this.taskInstanceRepository = taskInstanceRepository;
    }

    public MaintenanceTaskInstance attachTemplateToMotorcycle(MaintenanceTaskTemplate template, Motorcycle motorcycle) {
        MaintenanceTaskInstance instance = new MaintenanceTaskInstance();
        instance.setTemplate(template);
        instance.setMotorcycle(motorcycle);
        instance.setCreatedAt(LocalDateTime.now());
        if (template.getIntervalKm() != null && motorcycle.getCurrentOdometerKm() != null) {
            instance.setNextDueOdometerKm(motorcycle.getCurrentOdometerKm() + template.getIntervalKm());
        }
        if (template.getIntervalMonths() != null) {
            instance.setNextDueDate(LocalDate.now().plusMonths(template.getIntervalMonths()));
        }
        updateStatus(instance);
        return taskInstanceRepository.save(instance);
    }

    public void updateStatus(MaintenanceTaskInstance instance) {
        Motorcycle moto = instance.getMotorcycle();
        LocalDate today = LocalDate.now();
        boolean overdue = false;
        boolean dueSoon = false;
        if (instance.getNextDueDate() != null) {
            overdue = today.isAfter(instance.getNextDueDate());
            if (!overdue) {
                LocalDate soonDate = today.plusDays(30);
                dueSoon = !today.isAfter(instance.getNextDueDate()) && !soonDate.isBefore(instance.getNextDueDate());
            }
        }
        if (instance.getNextDueOdometerKm() != null && moto.getCurrentOdometerKm() != null) {
            overdue = overdue || moto.getCurrentOdometerKm() > instance.getNextDueOdometerKm();
            if (!overdue) {
                int diff = instance.getNextDueOdometerKm() - moto.getCurrentOdometerKm();
                if (diff <= 500) {
                    dueSoon = true;
                }
            }
        }
        if (overdue) {
            instance.setStatus(MaintenanceTaskInstance.Status.OVERDUE);
        } else if (dueSoon) {
            instance.setStatus(MaintenanceTaskInstance.Status.DUE_SOON);
        } else {
            instance.setStatus(MaintenanceTaskInstance.Status.OK);
        }
    }

    public void recomputeStatusesForMotorcycle(Motorcycle motorcycle) {
        List<MaintenanceTaskInstance> instances = taskInstanceRepository.findByMotorcycle(motorcycle);
        instances.forEach(instance -> {
            updateStatus(instance);
            taskInstanceRepository.save(instance);
        });
    }

    public List<MaintenanceTaskInstance> findAllSorted() {
        List<MaintenanceTaskInstance> all = taskInstanceRepository.findAll();
        all.sort(Comparator
                .comparing(this::statusWeight)
                .thenComparing(i -> Optional.ofNullable(i.getNextDueDate()).orElse(LocalDate.MAX))
                .thenComparing(i -> Optional.ofNullable(i.getNextDueOdometerKm()).orElse(Integer.MAX_VALUE)));
        return all;
    }

    private int statusWeight(MaintenanceTaskInstance instance) {
        return switch (instance.getStatus()) {
            case OVERDUE -> 0;
            case DUE_SOON -> 1;
            default -> 2;
        };
    }
}
