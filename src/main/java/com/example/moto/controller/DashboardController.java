package com.example.moto.controller;

import com.example.moto.service.MaintenanceTaskInstanceService;
import com.example.moto.service.MotorcycleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final MotorcycleService motorcycleService;
    private final MaintenanceTaskInstanceService instanceService;

    public DashboardController(MotorcycleService motorcycleService, MaintenanceTaskInstanceService instanceService) {
        this.motorcycleService = motorcycleService;
        this.instanceService = instanceService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("motorcycles", motorcycleService.findAll());
        model.addAttribute("instanceService", instanceService);
        return "dashboard";
    }
}
