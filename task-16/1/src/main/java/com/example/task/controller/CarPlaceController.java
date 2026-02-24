package com.example.task.controller;

import com.example.task.dto.CarPlaceDto;
import com.example.task.exception.ServiceException;
import com.example.task.service.CarPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car-places")
public class CarPlaceController {

    private final CarPlaceService carPlaceService;

    @Autowired
    public CarPlaceController(CarPlaceService carPlaceService) {
        this.carPlaceService = carPlaceService;
    }

    @GetMapping
    public ResponseEntity<List<CarPlaceDto>> getAllCarPlaces() {
        List<CarPlaceDto> carPlaces = carPlaceService.findAll();
        return ResponseEntity.ok(carPlaces);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarPlaceDto> getCarPlaceById(@PathVariable("id") Integer id) throws ServiceException {
        CarPlaceDto carPlace = carPlaceService.findById(id);
        return ResponseEntity.ok(carPlace);
    }

    @GetMapping("/available")
    public ResponseEntity<List<CarPlaceDto>> getAvailableCarPlaces() {
        List<CarPlaceDto> carPlaces = carPlaceService.findAvailableCarPlaces();
        return ResponseEntity.ok(carPlaces);
    }

    @GetMapping("/available/count")
    public ResponseEntity<Long> getAvailableCarPlacesCount() {
        long count = carPlaceService.countAvailableCarPlaces();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/carlift")
    public ResponseEntity<List<CarPlaceDto>> getCarPlacesByCarLift(
            @RequestParam("hasLift") boolean hasLift) {
        List<CarPlaceDto> carPlaces = carPlaceService.findByCarLift(hasLift);
        return ResponseEntity.ok(carPlaces);
    }

    @PostMapping
    public ResponseEntity<CarPlaceDto> createCarPlace(@RequestBody CarPlaceDto carPlaceDto) throws ServiceException {
        CarPlaceDto created = carPlaceService.create(carPlaceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarPlaceDto> updateCarPlace(
            @PathVariable("id") Integer id,
            @RequestBody CarPlaceDto carPlaceDto) throws ServiceException {
        CarPlaceDto updated = carPlaceService.update(id, carPlaceDto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/occupation")
    public ResponseEntity<Void> updateOccupation(
            @PathVariable("id") Integer id,
            @RequestParam("occupied") Boolean occupied) throws ServiceException {
        carPlaceService.updateOccupation(id, occupied);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarPlace(@PathVariable("id") Integer id) throws ServiceException {
        carPlaceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}