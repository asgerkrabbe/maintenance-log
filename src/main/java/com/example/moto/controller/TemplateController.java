package com.example.moto.controller;

import com.example.moto.dto.TemplateForm;
import com.example.moto.entity.MaintenanceTaskTemplate;
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
        model.addAttribute("template", new TemplateForm());
        return "template-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("template") TemplateForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "template-form";
        }
        templateService.save(mapToEntity(form));
        return "redirect:/templates";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        MaintenanceTaskTemplate template = templateService.findById(id).orElseThrow();
        model.addAttribute("template", mapToForm(template));
        return "template-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("template") TemplateForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "template-form";
        }
        MaintenanceTaskTemplate template = mapToEntity(form);
        template.setId(id);
        templateService.save(template);
        return "redirect:/templates";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        templateService.delete(id);
        return "redirect:/templates";
    }

    private MaintenanceTaskTemplate mapToEntity(TemplateForm form) {
        MaintenanceTaskTemplate template = new MaintenanceTaskTemplate();
        template.setId(form.getId());
        template.setName(form.getName());
        template.setDescription(form.getDescription());
        template.setIntervalKm(form.getIntervalKm());
        template.setIntervalMonths(form.getIntervalMonths());
        template.setDefaultCategory(form.getDefaultCategory());
        return template;
    }

    private TemplateForm mapToForm(MaintenanceTaskTemplate template) {
        TemplateForm form = new TemplateForm();
        form.setId(template.getId());
        form.setName(template.getName());
        form.setDescription(template.getDescription());
        form.setIntervalKm(template.getIntervalKm());
        form.setIntervalMonths(template.getIntervalMonths());
        form.setDefaultCategory(template.getDefaultCategory());
        return form;
    }
}
