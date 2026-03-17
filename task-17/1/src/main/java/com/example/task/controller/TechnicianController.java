package com.example.task.controller;

import com.example.task.dto.TechnicianDto;
import com.example.task.exception.ServiceException;
import com.example.task.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technicians")
public class TechnicianController {

    private final TechnicianService technicianService;

    @Autowired
    public TechnicianController(TechnicianService technicianService) {
        this.technicianService = technicianService;
    }

    @GetMapping
    public ResponseEntity<List<TechnicianDto>> getAllTechnicians() {
        List<TechnicianDto> technicians = technicianService.findAll();
        return ResponseEntity.ok(technicians);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnicianDto> getTechnicianById(@PathVariable("id") Integer id) throws ServiceException {
        TechnicianDto technician = technicianService.findById(id);
        return ResponseEntity.ok(technician);
    }

    @GetMapping("/available")
    public ResponseEntity<List<TechnicianDto>> getAvailableTechnicians() {
        List<TechnicianDto> technicians = technicianService.findAvailableTechnicians();
        return ResponseEntity.ok(technicians);
    }

    @GetMapping("/sorted/name")
    public ResponseEntity<List<TechnicianDto>> getTechniciansSortedByName() {
        List<TechnicianDto> technicians = technicianService.findAllSortedByName();
        return ResponseEntity.ok(technicians);
    }

    @GetMapping("/sorted/availability")
    public ResponseEntity<List<TechnicianDto>> getTechniciansSortedByAvailability() {
        List<TechnicianDto> technicians = technicianService.findAllSortedByAvailability();
        return ResponseEntity.ok(technicians);
    }

    @PostMapping
    public ResponseEntity<TechnicianDto> createTechnician(@RequestBody TechnicianDto technicianDto) {
        TechnicianDto created = technicianService.create(technicianDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnicianDto> updateTechnician(
            @PathVariable("id") Integer id,
            @RequestBody TechnicianDto technicianDto) throws ServiceException {
        TechnicianDto updated = technicianService.update(id, technicianDto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Void> updateAvailability(
            @PathVariable("id") Integer id,
            @RequestParam("available") Boolean available) throws ServiceException {
        technicianService.updateAvailability(id, available);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/specialization")
    public ResponseEntity<Void> updateSpecialization(
            @PathVariable("id") Integer id,
            @RequestParam("spec") String specialization) throws ServiceException {
        technicianService.updateSpecialization(id, specialization);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnician(@PathVariable("id") Integer id) throws ServiceException {
        technicianService.delete(id);
        return ResponseEntity.noContent().build();
    }
}