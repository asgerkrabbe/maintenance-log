package com.example.moto.controller;

import com.example.moto.service.MaintenanceTaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/upcoming")
public class TaskController {
    private final MaintenanceTaskService taskService;

    public TaskController(MaintenanceTaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String upcoming(Model model) {
        model.addAttribute("tasks", taskService.findAllOrdered());
        return "tasks/upcoming";
    }
}
