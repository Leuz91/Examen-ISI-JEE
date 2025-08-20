package com.example.demo.repository;

import com.example.demo.model.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findBySectorId(Long sectorId);
}
