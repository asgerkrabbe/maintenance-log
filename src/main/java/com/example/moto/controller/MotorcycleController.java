package com.example.moto.controller;

import com.example.moto.dto.AttachTemplateDto;
import com.example.moto.dto.MaintenanceRecordDto;
import com.example.moto.dto.MotorcycleDto;
import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.Motorcycle;
import com.example.moto.service.MaintenanceRecordService;
import com.example.moto.service.MotorcycleService;
import com.example.moto.service.TaskInstanceService;
import com.example.moto.service.TemplateService;
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
    private final TaskInstanceService taskInstanceService;
    private final TemplateService templateService;
    private final MaintenanceRecordService recordService;

    public MotorcycleController(MotorcycleService motorcycleService, TaskInstanceService taskInstanceService,
                                TemplateService templateService, MaintenanceRecordService recordService) {
        this.motorcycleService = motorcycleService;
        this.taskInstanceService = taskInstanceService;
        this.templateService = templateService;
        this.recordService = recordService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("motorcycles", motorcycleService.findAll());
        return "motorcycles";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("motorcycle", new MotorcycleDto());
        return "motorcycle-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("motorcycle") MotorcycleDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "motorcycle-form";
        }
        motorcycleService.create(dto);
        return "redirect:/motorcycles";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, @RequestParam(required = false) String category, Model model) {
        Motorcycle motorcycle = motorcycleService.findById(id);
        List<MaintenanceTaskInstance> tasks = taskInstanceService.findByMotorcycle(motorcycle);
        model.addAttribute("motorcycle", motorcycle);
        model.addAttribute("tasks", tasks);
        model.addAttribute("records", recordService.findByMotorcycle(motorcycle, category));
        model.addAttribute("categoryFilter", category);
        model.addAttribute("templates", templateService.findAll());
        model.addAttribute("attachDto", new AttachTemplateDto());
        model.addAttribute("recordDto", createRecordDto(motorcycle));
        return "motorcycle-detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Motorcycle motorcycle = motorcycleService.findById(id);
        MotorcycleDto dto = new MotorcycleDto();
        dto.setNickname(motorcycle.getNickname());
        dto.setMake(motorcycle.getMake());
        dto.setModel(motorcycle.getModel());
        dto.setYear(motorcycle.getYear());
        dto.setEngineSizeCc(motorcycle.getEngineSizeCc());
        dto.setVin(motorcycle.getVin());
        dto.setLicensePlate(motorcycle.getLicensePlate());
        dto.setPurchaseDate(motorcycle.getPurchaseDate());
        dto.setCurrentOdometerKm(motorcycle.getCurrentOdometerKm());
        dto.setNotes(motorcycle.getNotes());
        model.addAttribute("motorcycle", dto);
        return "motorcycle-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("motorcycle") MotorcycleDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "motorcycle-form";
        }
        motorcycleService.update(id, dto);
        return "redirect:/motorcycles/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        motorcycleService.delete(id);
        return "redirect:/motorcycles";
    }

    @PostMapping("/{id}/attach-template")
    public String attachTemplate(@PathVariable Long id, @Valid @ModelAttribute("attachDto") AttachTemplateDto dto, BindingResult result) {
        if (!result.hasErrors()) {
            Motorcycle motorcycle = motorcycleService.findById(id);
            taskInstanceService.attachTemplate(motorcycle, dto);
        }
        return "redirect:/motorcycles/" + id;
    }

    private MaintenanceRecordDto createRecordDto(Motorcycle motorcycle) {
        MaintenanceRecordDto dto = new MaintenanceRecordDto();
        dto.setMotorcycleId(motorcycle.getId());
        dto.setOdometerKm(motorcycle.getCurrentOdometerKm());
        return dto;
    }
}
