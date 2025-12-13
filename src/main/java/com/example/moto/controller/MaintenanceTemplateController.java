package com.example.moto.controller;

import com.example.moto.dto.MaintenanceTaskTemplateDto;
import com.example.moto.entity.MaintenanceTaskTemplate;
import com.example.moto.service.MaintenanceTemplateService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/templates")
public class MaintenanceTemplateController {
    private final MaintenanceTemplateService templateService;

    public MaintenanceTemplateController(MaintenanceTemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("templates", templateService.findAll());
        return "templates/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("templateDto", new MaintenanceTaskTemplateDto());
        return "templates/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("templateDto") MaintenanceTaskTemplateDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "templates/form";
        }
        templateService.save(dto);
        return "redirect:/templates";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        MaintenanceTaskTemplate template = templateService.findById(id).orElseThrow();
        model.addAttribute("templateDto", toDto(template));
        return "templates/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("templateDto") MaintenanceTaskTemplateDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "templates/form";
        }
        dto.setId(id);
        templateService.save(dto);
        return "redirect:/templates";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        templateService.delete(id);
        return "redirect:/templates";
    }

    private MaintenanceTaskTemplateDto toDto(MaintenanceTaskTemplate template) {
        MaintenanceTaskTemplateDto dto = new MaintenanceTaskTemplateDto();
        dto.setId(template.getId());
        dto.setName(template.getName());
        dto.setDescription(template.getDescription());
        dto.setIntervalKm(template.getIntervalKm());
        dto.setIntervalMonths(template.getIntervalMonths());
        dto.setDefaultCategory(template.getDefaultCategory());
        return dto;
    }
}
