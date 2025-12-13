package com.example.moto.controller;

import com.example.moto.dto.MaintenanceRecordDto;
import com.example.moto.service.MaintenanceRecordService;
import com.example.moto.service.MotorcycleService;
import com.example.moto.service.TemplateService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/records")
public class MaintenanceRecordController {
    private final MaintenanceRecordService recordService;
    private final MotorcycleService motorcycleService;
    private final TemplateService templateService;

    public MaintenanceRecordController(MaintenanceRecordService recordService, MotorcycleService motorcycleService, TemplateService templateService) {
        this.recordService = recordService;
        this.motorcycleService = motorcycleService;
        this.templateService = templateService;
    }

    @GetMapping("/create")
    public String createForm(@RequestParam Long motorcycleId, Model model) {
        MaintenanceRecordDto dto = new MaintenanceRecordDto();
        dto.setMotorcycleId(motorcycleId);
        model.addAttribute("record", dto);
        model.addAttribute("templates", templateService.findAll());
        return "records/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("record") MaintenanceRecordDto dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("templates", templateService.findAll());
            return "records/form";
        }
        var record = recordService.saveFromDto(dto);
        return "redirect:/motorcycles/" + record.getMotorcycle().getId();
    }
}
