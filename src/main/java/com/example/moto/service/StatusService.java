package com.example.moto.service;

import com.example.moto.entity.MaintenanceStatus;
import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.Motorcycle;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StatusService {
    public MaintenanceStatus computeStatus(MaintenanceTaskInstance instance) {
        Motorcycle moto = instance.getMotorcycle();
        LocalDate today = LocalDate.now();
        boolean overdue = false;
        if (instance.getNextDueDate() != null && today.isAfter(instance.getNextDueDate())) {
            overdue = true;
        }
        if (instance.getNextDueOdometerKm() != null && moto.getCurrentOdometerKm() != null
                && moto.getCurrentOdometerKm() > instance.getNextDueOdometerKm()) {
            overdue = true;
        }
        if (overdue) {
            return MaintenanceStatus.OVERDUE;
        }
        boolean dueSoon = false;
        if (instance.getNextDueDate() != null && !today.isAfter(instance.getNextDueDate())) {
            if (!today.plusDays(30).isBefore(instance.getNextDueDate())) {
                dueSoon = true;
            }
        }
        if (instance.getNextDueOdometerKm() != null && moto.getCurrentOdometerKm() != null) {
            if (moto.getCurrentOdometerKm() + 500 >= instance.getNextDueOdometerKm()) {
                dueSoon = true;
            }
        }
        return dueSoon ? MaintenanceStatus.DUE_SOON : MaintenanceStatus.OK;
    }
}
