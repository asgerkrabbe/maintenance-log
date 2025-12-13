package com.example.moto.controller;

import com.example.moto.entity.TaskStatus;
import com.example.moto.service.MaintenanceTaskInstanceService;
import com.example.moto.service.MotorcycleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
public class TaskController {
    private final MaintenanceTaskInstanceService instanceService;
    private final MotorcycleService motorcycleService;

    public TaskController(MaintenanceTaskInstanceService instanceService, MotorcycleService motorcycleService) {
        this.instanceService = instanceService;
        this.motorcycleService = motorcycleService;
    }

    @GetMapping("/tasks")
    public String tasks(@RequestParam(required = false) TaskStatus status,
                        @RequestParam(required = false) Long motorcycleId,
                        Model model) {
        var tasks = instanceService.findAll();
        if (status != null) {
            tasks = tasks.stream().filter(t -> t.getStatus() == status).toList();
        }
        if (motorcycleId != null) {
            tasks = tasks.stream().filter(t -> t.getMotorcycle().getId().equals(motorcycleId)).toList();
        }
        tasks = tasks.stream().sorted(Comparator.comparing((var t) -> t.getStatus().ordinal())
                .thenComparing(t -> t.getNextDueDate(), Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
        model.addAttribute("tasks", tasks);
        model.addAttribute("motorcycles", motorcycleService.findAll());
        return "tasks/list";
    }
}
