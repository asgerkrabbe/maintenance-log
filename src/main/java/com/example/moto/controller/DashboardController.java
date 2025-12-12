package com.example.moto.controller;

import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.Motorcycle;
import com.example.moto.service.TaskInstanceService;
import com.example.moto.service.MotorcycleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    private final MotorcycleService motorcycleService;
    private final TaskInstanceService taskInstanceService;

    public DashboardController(MotorcycleService motorcycleService, TaskInstanceService taskInstanceService) {
        this.motorcycleService = motorcycleService;
        this.taskInstanceService = taskInstanceService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        List<Motorcycle> motorcycles = motorcycleService.findAll();
        Map<Long, List<MaintenanceTaskInstance>> tasksByMoto = motorcycles.stream()
                .collect(Collectors.toMap(Motorcycle::getId, taskInstanceService::findByMotorcycle));

        model.addAttribute("motorcycles", motorcycles);
        model.addAttribute("tasksByMoto", tasksByMoto);
        model.addAttribute("nextByMoto", tasksByMoto.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream()
                        .sorted(Comparator.comparing(MaintenanceTaskInstance::getStatus)
                                .thenComparing(MaintenanceTaskInstance::getNextDueDate, Comparator.nullsLast(Comparator.naturalOrder()))
                                .thenComparing(MaintenanceTaskInstance::getNextDueOdometerKm, Comparator.nullsLast(Comparator.naturalOrder())))
                        .findFirst().orElse(null))));
        model.addAttribute("overdueCounts", tasksByMoto.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (int) e.getValue().stream()
                        .filter(t -> t.getStatus().name().equals("OVERDUE")).count())));
        return "dashboard";
    }
}
