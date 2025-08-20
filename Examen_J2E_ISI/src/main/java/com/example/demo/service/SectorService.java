package com.example.demo.service;

import com.example.demo.model.Sector;
import com.example.demo.repository.SectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorService {
    private final SectorRepository repo;

    public SectorService(SectorRepository repo) {
        this.repo = repo;
    }

    public Sector create(String name) {
        Sector s = new Sector();
        s.setName(name);
        return repo.save(s);
    }

    public List<Sector> all() {
        return repo.findAll();
    }

    public Sector get(Long id) {
        return repo.findById(id).orElseThrow();
    }
}
