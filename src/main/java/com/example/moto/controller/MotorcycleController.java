package com.example.moto.controller;

import com.example.moto.dto.AttachTemplateDto;
import com.example.moto.dto.MaintenanceRecordDto;
import com.example.moto.dto.MotorcycleDto;
import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.Motorcycle;
import com.example.moto.service.MaintenanceRecordService;
import com.example.moto.service.MaintenanceTaskService;
import com.example.moto.service.MaintenanceTemplateService;
import com.example.moto.service.MotorcycleService;
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
    private final MaintenanceTaskService taskService;
    private final MaintenanceTemplateService templateService;
    private final MaintenanceRecordService recordService;

    public MotorcycleController(MotorcycleService motorcycleService, MaintenanceTaskService taskService,
                                MaintenanceTemplateService templateService, MaintenanceRecordService recordService) {
        this.motorcycleService = motorcycleService;
        this.taskService = taskService;
        this.templateService = templateService;
        this.recordService = recordService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("motorcycles", motorcycleService.findAll());
        return "motorcycles/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("motorcycleDto", new MotorcycleDto());
        return "motorcycles/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("motorcycleDto") MotorcycleDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "motorcycles/form";
        }
        Motorcycle saved = motorcycleService.save(dto);
        return "redirect:/motorcycles/" + saved.getId();
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Motorcycle motorcycle = motorcycleService.findById(id).orElseThrow();
        model.addAttribute("motorcycleDto", toDto(motorcycle));
        return "motorcycles/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("motorcycleDto") MotorcycleDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "motorcycles/form";
        }
        dto.setId(id);
        motorcycleService.save(dto);
        return "redirect:/motorcycles/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        motorcycleService.delete(id);
        return "redirect:/motorcycles";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, @RequestParam(value = "category", required = false) String category,
                         Model model) {
        Motorcycle motorcycle = motorcycleService.findById(id).orElseThrow();
        List<MaintenanceTaskInstance> tasks = taskService.findUpcomingForMotorcycle(id);
        model.addAttribute("motorcycle", motorcycle);
        model.addAttribute("tasks", tasks);
        model.addAttribute("records", recordService.findByMotorcycle(id, category));
        model.addAttribute("category", category);
        model.addAttribute("recordDto", defaultRecordDto(id));
        model.addAttribute("attachDto", new AttachTemplateDto());
        model.addAttribute("templates", templateService.findAll());
        model.addAttribute("instances", tasks);
        return "motorcycles/detail";
    }

    @PostMapping("/{id}/attach-template")
    public String attachTemplate(@PathVariable Long id, @Valid @ModelAttribute("attachDto") AttachTemplateDto dto,
                                 BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/motorcycles/" + id;
        }
        taskService.attachTemplate(id, dto);
        return "redirect:/motorcycles/" + id;
    }

    @PostMapping("/{id}/records")
    public String addRecord(@PathVariable Long id, @Valid @ModelAttribute("recordDto") MaintenanceRecordDto dto,
                            BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/motorcycles/" + id + "?error";
        }
        dto.setMotorcycleId(id);
        recordService.save(dto);
        return "redirect:/motorcycles/" + id;
    }

    private MotorcycleDto toDto(Motorcycle moto) {
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
        return dto;
    }

    private MaintenanceRecordDto defaultRecordDto(Long motorcycleId) {
        MaintenanceRecordDto dto = new MaintenanceRecordDto();
        dto.setMotorcycleId(motorcycleId);
        dto.setCategory("general");
        return dto;
    }
}
