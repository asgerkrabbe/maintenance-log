package com.example.moto.controller;

import com.example.moto.dto.AttachTemplateDto;
import com.example.moto.dto.MotorcycleDto;
import com.example.moto.dto.MaintenanceRecordDto;
import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.Motorcycle;
import com.example.moto.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/motorcycles")
public class MotorcycleController {
    private final MotorcycleService motorcycleService;
    private final TemplateService templateService;
    private final MaintenanceTaskInstanceService instanceService;
    private final MaintenanceRecordService recordService;

    public MotorcycleController(MotorcycleService motorcycleService, TemplateService templateService,
                                MaintenanceTaskInstanceService instanceService, MaintenanceRecordService recordService) {
        this.motorcycleService = motorcycleService;
        this.templateService = templateService;
        this.instanceService = instanceService;
        this.recordService = recordService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("motorcycles", motorcycleService.findAll());
        return "motorcycles/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("motorcycle", new MotorcycleDto());
        return "motorcycles/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Motorcycle moto = motorcycleService.findById(id).orElseThrow();
        MotorcycleDto dto = new MotorcycleDto();
        dto.setId(moto.getId());
        dto.setNickname(moto.getNickname());
        dto.setMake(moto.getMake());
        dto.setModel(moto.getModel());
        dto.setYear(moto.getYear());
        dto.setEngineSizeCc(moto.getEngineSizeCc());
        dto.setVin(moto.getVin());
        dto.setLicensePlate(moto.getLicensePlate());
        dto.setPurchaseDate(moto.getPurchaseDate());
        dto.setCurrentOdometerKm(moto.getCurrentOdometerKm());
        dto.setNotes(moto.getNotes());
        model.addAttribute("motorcycle", dto);
        return "motorcycles/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("motorcycle") MotorcycleDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "motorcycles/form";
        }
        motorcycleService.saveFromDto(dto);
        return "redirect:/motorcycles";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, @RequestParam(required = false) String category, Model model) {
        Motorcycle moto = motorcycleService.findById(id).orElseThrow();
        List<MaintenanceTaskInstance> instances = instanceService.findByMotorcycle(moto);
        var records = recordService.findByMotorcycle(moto);
        if (category != null && !category.isEmpty()) {
            records = records.stream().filter(r -> category.equalsIgnoreCase(r.getCategory())).toList();
        }
        model.addAttribute("motorcycle", moto);
        model.addAttribute("instances", instances);
        model.addAttribute("records", records);
        model.addAttribute("templates", templateService.findAll());
        model.addAttribute("attach", new AttachTemplateDto());
        model.addAttribute("recordDto", new MaintenanceRecordDto());
        return "motorcycles/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        motorcycleService.delete(id);
        return "redirect:/motorcycles";
    }

    @PostMapping("/{id}/attach-template")
    public String attachTemplate(@PathVariable Long id, @Valid @ModelAttribute("attach") AttachTemplateDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/motorcycles/" + id;
        }
        Motorcycle moto = motorcycleService.findById(id).orElseThrow();
        templateService.findById(dto.getTemplateId()).ifPresent(template -> instanceService.attachTemplate(moto, template));
        return "redirect:/motorcycles/" + id;
    }
}
