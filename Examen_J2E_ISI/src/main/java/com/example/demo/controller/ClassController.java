package com.example.demo.controller;

import com.example.demo.dto.ClassDTO;
import com.example.demo.model.ClassEntity;
import com.example.demo.service.ClassService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassController {
    private final ClassService service;

    public ClassController(ClassService service) {
        this.service = service;
    }

    @PostMapping
    public ClassDTO create(@RequestBody ClassDTO dto) {
        return toDTO(service.create(dto.className(), dto.description(), dto.sectorId()));
    }

    @GetMapping
    public List<ClassDTO> all() {
        return service.bySector(null).stream().map(this::toDTO).toList();
    }

    private ClassDTO toDTO(ClassEntity c) {
        return new ClassDTO(c.getId(), c.getClassName(), c.getDescription(), c.getSector().getId());
    }
}
