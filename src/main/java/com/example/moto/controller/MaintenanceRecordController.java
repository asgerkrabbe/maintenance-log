package com.example.moto.controller;

import com.example.moto.dto.MaintenanceRecordDto;
import com.example.moto.service.MaintenanceRecordService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/records")
public class MaintenanceRecordController {
    private final MaintenanceRecordService recordService;

    public MaintenanceRecordController(MaintenanceRecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("recordDto") MaintenanceRecordDto dto, BindingResult result) {
        if (!result.hasErrors()) {
            recordService.create(dto);
        }
        return "redirect:/motorcycles/" + dto.getMotorcycleId();
    }
}
