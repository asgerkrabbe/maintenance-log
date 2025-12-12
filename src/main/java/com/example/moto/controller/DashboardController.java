package com.example.moto.controller;

import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MaintenanceTaskInstanceRepository;
import com.example.moto.repository.MotorcycleRepository;
import com.example.moto.service.MaintenanceTaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    private final MotorcycleRepository motorcycleRepository;
    private final MaintenanceTaskInstanceRepository taskInstanceRepository;
    private final MaintenanceTaskService maintenanceTaskService;

    public DashboardController(MotorcycleRepository motorcycleRepository,
                               MaintenanceTaskInstanceRepository taskInstanceRepository,
                               MaintenanceTaskService maintenanceTaskService) {
        this.motorcycleRepository = motorcycleRepository;
        this.taskInstanceRepository = taskInstanceRepository;
        this.maintenanceTaskService = maintenanceTaskService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        List<Motorcycle> motorcycles = motorcycleRepository.findAll();
        Map<Long, Long> overdueCount = taskInstanceRepository.findAll().stream()
                .collect(Collectors.groupingBy(t -> t.getMotorcycle().getId(), Collectors.filtering(t -> t.getStatus() == MaintenanceTaskInstance.Status.OVERDUE, Collectors.counting())));
        Map<Long, MaintenanceTaskInstance> nextTask = taskInstanceRepository.findAll().stream()
                .collect(Collectors.groupingBy(t -> t.getMotorcycle().getId(),
                        Collectors.collectingAndThen(Collectors.minBy(Comparator.comparing(MaintenanceTaskInstance::getNextDueDate, Comparator.nullsLast(Comparator.naturalOrder()))
                                .thenComparing(MaintenanceTaskInstance::getNextDueOdometerKm, Comparator.nullsLast(Comparator.naturalOrder()))), opt -> opt.orElse(null))));
        model.addAttribute("motorcycles", motorcycles);
        model.addAttribute("overdueCount", overdueCount);
        model.addAttribute("nextTask", nextTask);
        return "dashboard";
    }

    @GetMapping("/upcoming")
    public String upcoming(Model model) {
        List<MaintenanceTaskInstance> tasks = maintenanceTaskService.findAllSorted();
        model.addAttribute("tasks", tasks);
        return "upcoming";
    }
}
