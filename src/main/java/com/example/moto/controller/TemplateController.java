package com.example.moto.controller;

import com.example.moto.dto.MaintenanceTaskTemplateDto;
import com.example.moto.service.TemplateService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/templates")
public class TemplateController {
    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("templates", templateService.findAll());
        return "templates";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("template", new MaintenanceTaskTemplateDto());
        return "template-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("template") MaintenanceTaskTemplateDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "template-form";
        }
        templateService.save(dto);
        return "redirect:/templates";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        var template = templateService.findById(id);
        MaintenanceTaskTemplateDto dto = new MaintenanceTaskTemplateDto();
        dto.setId(template.getId());
        dto.setName(template.getName());
        dto.setDescription(template.getDescription());
        dto.setIntervalKm(template.getIntervalKm());
        dto.setIntervalMonths(template.getIntervalMonths());
        dto.setDefaultCategory(template.getDefaultCategory());
        model.addAttribute("template", dto);
        return "template-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("template") MaintenanceTaskTemplateDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "template-form";
        }
        templateService.update(id, dto);
        return "redirect:/templates";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        templateService.delete(id);
        return "redirect:/templates";
    }
}
