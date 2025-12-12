package com.example.moto.controller;

import com.example.moto.dto.ExportData;
import com.example.moto.service.ImportExportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/import-export")
public class ImportExportController {
    private final ImportExportService importExportService;
    private final ObjectMapper objectMapper;

    public ImportExportController(ImportExportService importExportService, ObjectMapper objectMapper) {
        this.importExportService = importExportService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String page(Model model) {
        return "import-export";
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() throws IOException {
        ExportData data = importExportService.exportAll();
        byte[] payload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(data);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload);
    }

    @PostMapping("/import")
    public String importData(@RequestParam("file") MultipartFile file) throws IOException {
        ExportData data = objectMapper.readValue(file.getBytes(), ExportData.class);
        importExportService.importAll(data);
        return "redirect:/import-export?success";
    }
}
