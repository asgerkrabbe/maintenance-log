package com.example.moto.controller;

import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.Motorcycle;
import com.example.moto.service.MaintenanceTaskService;
import com.example.moto.service.MotorcycleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class DashboardController {
    private final MotorcycleService motorcycleService;
    private final MaintenanceTaskService taskService;

    public DashboardController(MotorcycleService motorcycleService, MaintenanceTaskService taskService) {
        this.motorcycleService = motorcycleService;
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        List<Motorcycle> motorcycles = motorcycleService.findAll();
        model.addAttribute("motorcycles", motorcycles.stream().map(moto -> new DashboardItem(moto,
                nextTask(moto.getId()), overdueCount(moto.getId()))).toList());
        return "dashboard";
    }

    private MaintenanceTaskInstance nextTask(Long motorcycleId) {
        return taskService.findUpcomingForMotorcycle(motorcycleId).stream()
                .sorted(Comparator.comparing(MaintenanceTaskInstance::getStatus)
                        .thenComparing(MaintenanceTaskInstance::getNextDueDate, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(MaintenanceTaskInstance::getNextDueOdometerKm, Comparator.nullsLast(Comparator.naturalOrder())))
                .findFirst().orElse(null);
    }

    private long overdueCount(Long motorcycleId) {
        return taskService.findUpcomingForMotorcycle(motorcycleId).stream()
                .filter(t -> t.getStatus() != null && t.getStatus().name().equals("OVERDUE"))
                .count();
    }

    public record DashboardItem(Motorcycle motorcycle, MaintenanceTaskInstance nextTask, long overdueCount) {
    }
}
