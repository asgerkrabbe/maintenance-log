package com.example.moto.controller;

import com.example.moto.service.ImportExportService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class ImportExportController {
    private final ImportExportService importExportService;

    public ImportExportController(ImportExportService importExportService) {
        this.importExportService = importExportService;
    }

    @GetMapping("/import-export")
    public String page(Model model) {
        return "import-export";
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> exportJson() throws IOException {
        var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        String json = mapper.writeValueAsString(importExportService.exportAll());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=export.json")
                .body(json);
    }

    @PostMapping("/import")
    public String importJson(@RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            var map = mapper.readValue(new String(file.getBytes(), StandardCharsets.UTF_8), java.util.Map.class);
            importExportService.importData((java.util.Map<String, java.util.List<?>>) map);
        }
        return "redirect:/import-export";
    }
}
