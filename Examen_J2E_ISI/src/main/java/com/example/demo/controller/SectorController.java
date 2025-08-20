package com.example.demo.controller;

import com.example.demo.dto.SectorDTO;
import com.example.demo.dto.ClassDTO;
import com.example.demo.model.Sector;
import com.example.demo.model.ClassEntity;
import com.example.demo.service.SectorService;
import com.example.demo.service.ClassService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sectors")
public class SectorController {
    private final SectorService service;
    private final ClassService classService;

    public SectorController(SectorService service, ClassService classService) {
        this.service = service;
        this.classService = classService;
    }

    @PostMapping
    public SectorDTO create(@RequestBody SectorDTO dto) {
        return toDTO(service.create(dto.name()));
    }

    @GetMapping
    public List<SectorDTO> all() {
        return service.all().stream().map(this::toDTO).toList();
    }

    @GetMapping("/{id}")
    public SectorDTO get(@PathVariable Long id) {
        return toDTO(service.get(id));
    }

    @GetMapping("/{id}/classes")
    public List<ClassDTO> classes(@PathVariable Long id) {
        return classService.bySector(id).stream().map(this::toDTO).toList();
    }

    private SectorDTO toDTO(Sector s) { return new SectorDTO(s.getId(), s.getName()); }
    private ClassDTO toDTO(ClassEntity c) { return new ClassDTO(c.getId(), c.getClassName(), c.getDescription(), c.getSector().getId()); }
}
