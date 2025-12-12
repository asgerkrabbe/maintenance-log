package com.example.moto.controller;

import com.example.moto.dto.TemplateDto;
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
        return "templates/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("template", new TemplateDto());
        return "templates/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var template = templateService.findById(id).orElseThrow();
        TemplateDto dto = new TemplateDto();
        dto.setId(template.getId());
        dto.setName(template.getName());
        dto.setDescription(template.getDescription());
        dto.setIntervalKm(template.getIntervalKm());
        dto.setIntervalMonths(template.getIntervalMonths());
        dto.setDefaultCategory(template.getDefaultCategory());
        model.addAttribute("template", dto);
        return "templates/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("template") TemplateDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "templates/form";
        }
        templateService.saveFromDto(dto);
        return "redirect:/templates";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        templateService.delete(id);
        return "redirect:/templates";
    }
}
