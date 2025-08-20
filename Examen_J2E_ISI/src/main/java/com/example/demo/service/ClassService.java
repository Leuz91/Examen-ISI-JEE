package com.example.demo.service;

import com.example.demo.model.ClassEntity;
import com.example.demo.model.Sector;
import com.example.demo.repository.ClassRepository;
import com.example.demo.repository.SectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {
    private final ClassRepository repo;
    private final SectorRepository sectorRepo;

    public ClassService(ClassRepository repo, SectorRepository sectorRepo) {
        this.repo = repo;
        this.sectorRepo = sectorRepo;
    }

    public ClassEntity create(String className, String description, Long sectorId) {
        Sector sector = sectorRepo.findById(sectorId).orElseThrow();
        ClassEntity c = new ClassEntity();
        c.setClassName(className);
        c.setDescription(description);
        c.setSector(sector);
        return repo.save(c);
    }

    public List<ClassEntity> bySector(Long sectorId) {
        if (sectorId == null) return repo.findAll();
        return repo.findBySectorId(sectorId);
    }
}
