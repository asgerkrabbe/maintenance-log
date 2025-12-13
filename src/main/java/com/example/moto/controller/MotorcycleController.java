package com.example.moto.controller;

import com.example.moto.dto.MaintenanceRecordForm;
import com.example.moto.dto.MotorcycleForm;
import com.example.moto.dto.TaskAttachForm;
import com.example.moto.entity.MaintenanceRecord;
import com.example.moto.entity.MaintenanceTaskTemplate;
import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MaintenanceTaskTemplateRepository;
import com.example.moto.service.MaintenanceRecordService;
import com.example.moto.service.MaintenanceTaskService;
import com.example.moto.service.MotorcycleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/motorcycles")
public class MotorcycleController {
    private final MotorcycleService motorcycleService;
    private final MaintenanceTaskService maintenanceTaskService;
    private final MaintenanceRecordService recordService;
    private final MaintenanceTaskTemplateRepository templateRepository;

    public MotorcycleController(MotorcycleService motorcycleService, MaintenanceTaskService maintenanceTaskService,
                                MaintenanceRecordService recordService, MaintenanceTaskTemplateRepository templateRepository) {
        this.motorcycleService = motorcycleService;
        this.maintenanceTaskService = maintenanceTaskService;
        this.recordService = recordService;
        this.templateRepository = templateRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("motorcycles", motorcycleService.findAll());
        return "motorcycles";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("motorcycle", new MotorcycleForm());
        return "motorcycle-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("motorcycle") MotorcycleForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "motorcycle-form";
        }
        motorcycleService.save(mapToEntity(form));
        return "redirect:/motorcycles";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Motorcycle moto = motorcycleService.findById(id).orElseThrow();
        List<MaintenanceRecord> records = recordService.listByMotorcycle(moto);
        model.addAttribute("motorcycle", moto);
        model.addAttribute("tasks", moto.getTaskInstances().stream().sorted(Comparator.comparing(t -> t.getStatus().name())).collect(Collectors.toList()));
        model.addAttribute("records", records);
        model.addAttribute("attachForm", new TaskAttachForm());
        model.addAttribute("templates", templateRepository.findAll());
        return "motorcycle-detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Motorcycle moto = motorcycleService.findById(id).orElseThrow();
        model.addAttribute("motorcycle", mapToForm(moto));
        return "motorcycle-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("motorcycle") MotorcycleForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "motorcycle-form";
        }
        Motorcycle moto = mapToEntity(form);
        moto.setId(id);
        motorcycleService.save(moto);
        return "redirect:/motorcycles/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        motorcycleService.delete(id);
        return "redirect:/motorcycles";
    }

    @PostMapping("/{id}/attach-template")
    public String attachTemplate(@PathVariable Long id, @Valid @ModelAttribute("attachForm") TaskAttachForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/motorcycles/" + id + "?error=template";
        }
        Motorcycle moto = motorcycleService.findById(id).orElseThrow();
        MaintenanceTaskTemplate template = templateRepository.findById(form.getTemplateId()).orElseThrow();
        maintenanceTaskService.attachTemplateToMotorcycle(template, moto);
        return "redirect:/motorcycles/" + id;
    }

    @GetMapping("/{id}/records/new")
    public String newRecord(@PathVariable Long id, Model model) {
        Motorcycle moto = motorcycleService.findById(id).orElseThrow();
        MaintenanceRecordForm form = new MaintenanceRecordForm();
        form.setMotorcycleId(id);
        form.setCategory(moto.getTaskInstances().stream().findFirst().map(t -> t.getTemplate().getDefaultCategory()).orElse("general"));
        model.addAttribute("record", form);
        model.addAttribute("templates", templateRepository.findAll());
        model.addAttribute("motorcycle", moto);
        return "record-form";
    }

    @PostMapping("/{id}/records")
    public String createRecord(@PathVariable Long id, @Valid @ModelAttribute("record") MaintenanceRecordForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "record-form";
        }
        Motorcycle moto = motorcycleService.findById(id).orElseThrow();
        MaintenanceRecord record = new MaintenanceRecord();
        record.setMotorcycle(moto);
        record.setDate(form.getDate());
        record.setOdometerKm(form.getOdometerKm());
        record.setCategory(form.getCategory());
        record.setTitle(form.getTitle());
        record.setDescription(form.getDescription());
        record.setPartsUsed(form.getPartsUsed());
        record.setCostLabor(form.getCostLabor());
        record.setCostParts(form.getCostParts());
        record.setWorkshop(form.getWorkshop());
        MaintenanceTaskTemplate template = null;
        if (form.getTemplateId() != null) {
            template = templateRepository.findById(form.getTemplateId()).orElse(null);
        }
        recordService.saveRecord(record, template);
        return "redirect:/motorcycles/" + id;
    }

    private Motorcycle mapToEntity(MotorcycleForm form) {
        Motorcycle moto = new Motorcycle();
        moto.setId(form.getId());
        moto.setNickname(form.getNickname());
        moto.setMake(form.getMake());
        moto.setModel(form.getModel());
        moto.setYear(form.getYear());
        moto.setEngineSizeCc(form.getEngineSizeCc());
        moto.setVin(form.getVin());
        moto.setLicensePlate(form.getLicensePlate());
        moto.setPurchaseDate(form.getPurchaseDate());
        moto.setCurrentOdometerKm(form.getCurrentOdometerKm());
        moto.setNotes(form.getNotes());
        return moto;
    }

    private MotorcycleForm mapToForm(Motorcycle moto) {
        MotorcycleForm form = new MotorcycleForm();
        form.setId(moto.getId());
        form.setNickname(moto.getNickname());
        form.setMake(moto.getMake());
        form.setModel(moto.getModel());
        form.setYear(moto.getYear());
        form.setEngineSizeCc(moto.getEngineSizeCc());
        form.setVin(moto.getVin());
        form.setLicensePlate(moto.getLicensePlate());
        form.setPurchaseDate(moto.getPurchaseDate());
        form.setCurrentOdometerKm(moto.getCurrentOdometerKm());
        form.setNotes(moto.getNotes());
        return form;
    }
}
