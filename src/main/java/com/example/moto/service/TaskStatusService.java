package com.example.moto.service;

import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.Motorcycle;
import com.example.moto.entity.TaskStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TaskStatusService {

    public TaskStatus calculateStatus(Motorcycle motorcycle, MaintenanceTaskInstance instance) {
        LocalDate today = LocalDate.now();
        if (instance.getNextDueDate() != null && today.isAfter(instance.getNextDueDate())) {
            return TaskStatus.OVERDUE;
        }
        if (instance.getNextDueOdometerKm() != null && motorcycle.getCurrentOdometerKm() != null
                && motorcycle.getCurrentOdometerKm() > instance.getNextDueOdometerKm()) {
            return TaskStatus.OVERDUE;
        }

        if (instance.getNextDueDate() != null && (today.isAfter(instance.getNextDueDate().minusDays(30)) || today.isEqual(instance.getNextDueDate().minusDays(30)))) {
            return TaskStatus.DUE_SOON;
        }
        if (instance.getNextDueOdometerKm() != null && motorcycle.getCurrentOdometerKm() != null
                && motorcycle.getCurrentOdometerKm() >= instance.getNextDueOdometerKm() - 500) {
            return TaskStatus.DUE_SOON;
        }
        return TaskStatus.OK;
    }
}
