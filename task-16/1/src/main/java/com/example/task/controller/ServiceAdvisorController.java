package com.example.task.controller;

import com.example.task.dto.ServiceAdvisorDto;
import com.example.task.exception.ServiceException;
import com.example.task.service.ServiceAdvisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-advisors")
public class ServiceAdvisorController {

    private final ServiceAdvisorService serviceAdvisorService;

    @Autowired
    public ServiceAdvisorController(ServiceAdvisorService serviceAdvisorService) {
        this.serviceAdvisorService = serviceAdvisorService;
    }

    @GetMapping
    public ResponseEntity<List<ServiceAdvisorDto>> getAllServiceAdvisors() {
        List<ServiceAdvisorDto> advisors = serviceAdvisorService.findAll();
        return ResponseEntity.ok(advisors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceAdvisorDto> getServiceAdvisorById(@PathVariable("id") Integer id) throws ServiceException {
        ServiceAdvisorDto advisor = serviceAdvisorService.findById(id);
        return ResponseEntity.ok(advisor);
    }

    @GetMapping("/sorted/name")
    public ResponseEntity<List<ServiceAdvisorDto>> getServiceAdvisorsSortedByName() {
        List<ServiceAdvisorDto> advisors = serviceAdvisorService.findAllSortedByName();
        return ResponseEntity.ok(advisors);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getServiceAdvisorsCount() {
        long count = serviceAdvisorService.countAll();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkServiceAdvisorExists(@RequestParam("name") String name) {
        boolean exists = serviceAdvisorService.existsByName(name);
        return ResponseEntity.ok(exists);
    }

    @PostMapping
    public ResponseEntity<ServiceAdvisorDto> createServiceAdvisor(@RequestBody ServiceAdvisorDto advisorDto) throws ServiceException {
        ServiceAdvisorDto created = serviceAdvisorService.create(advisorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceAdvisorDto> updateServiceAdvisor(
            @PathVariable("id") Integer id,
            @RequestBody ServiceAdvisorDto advisorDto) throws ServiceException {
        ServiceAdvisorDto updated = serviceAdvisorService.update(id, advisorDto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<Void> updateServiceAdvisorName(
            @PathVariable("id") Integer id,
            @RequestParam("newName") String newName) throws ServiceException {
        serviceAdvisorService.updateName(id, newName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceAdvisor(@PathVariable("id") Integer id) throws ServiceException {
        serviceAdvisorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}