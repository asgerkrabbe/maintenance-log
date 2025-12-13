package com.example.moto.controller;

import com.example.moto.entity.MaintenanceStatus;
import com.example.moto.service.TaskInstanceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaskController {
    private final TaskInstanceService taskInstanceService;

    public TaskController(TaskInstanceService taskInstanceService) {
        this.taskInstanceService = taskInstanceService;
    }

    @GetMapping("/tasks")
    public String upcoming(@RequestParam(required = false) String status, Model model) {
        var tasks = taskInstanceService.findAll();
        if (status != null && !status.isBlank()) {
            try {
                var filter = MaintenanceStatus.valueOf(status);
                tasks = tasks.stream().filter(t -> t.getStatus() == filter).toList();
            } catch (IllegalArgumentException ignored) {
            }
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("filterStatus", status);
        return "tasks";
    }
}
