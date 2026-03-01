package com.example.task.service;

import com.example.task.dao.CarPlaceDao;
import com.example.task.dto.CarPlaceDto;
import com.example.task.exception.ServiceException;
import com.example.task.model.CarPlace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarPlaceService {

    private final CarPlaceDao carPlaceDao;

    @Autowired
    public CarPlaceService(CarPlaceDao carPlaceDao) {
        this.carPlaceDao = carPlaceDao;
    }

    @Transactional(readOnly = true)
    public List<CarPlaceDto> findAll() {
        return carPlaceDao.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CarPlaceDto findById(Integer id) throws ServiceException {
        CarPlace carPlace = carPlaceDao.findById(id);
        if (carPlace == null) {
            throw new ServiceException("Car place not found with id: " + id);
        }
        return convertToDto(carPlace);
    }

    @Transactional(readOnly = true)
    public List<CarPlaceDto> findAvailableCarPlaces() {
        return carPlaceDao.findAvailableCarPlaces().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAvailableCarPlaces() {
        return carPlaceDao.countAvailableCarPlaces();
    }

    @Transactional(readOnly = true)
    public List<CarPlaceDto> findByCarLift(boolean hasCarLift) {
        return carPlaceDao.findByCarLift(hasCarLift).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CarPlaceDto create(CarPlaceDto carPlaceDto) throws ServiceException {
        if (carPlaceDto.getSquare() == null || carPlaceDto.getSquare() <= 0) {
            throw new ServiceException("Square must be positive");
        }

        CarPlace carPlace = new CarPlace(
                carPlaceDto.getSquare(),
                carPlaceDto.getCarLift() != null ? carPlaceDto.getCarLift() : false,
                carPlaceDto.getIsOccupied() != null ? carPlaceDto.getIsOccupied() : false
        );

        Integer id = carPlaceDao.save(carPlace);
        return findById(id);
    }

    @Transactional
    public CarPlaceDto update(Integer id, CarPlaceDto carPlaceDto) throws ServiceException {
        CarPlace carPlace = carPlaceDao.findById(id);
        if (carPlace == null) {
            throw new ServiceException("Car place not found with id: " + id);
        }

        if (carPlaceDto.getSquare() != null && carPlaceDto.getSquare() > 0) {
            carPlace.setSquare(carPlaceDto.getSquare());
        }
        if (carPlaceDto.getCarLift() != null) {
            carPlace.setCarLift(carPlaceDto.getCarLift());
        }
        if (carPlaceDto.getIsOccupied() != null) {
            carPlace.setIsOccupied(carPlaceDto.getIsOccupied());
        }

        carPlaceDao.update(carPlace);
        return convertToDto(carPlace);
    }

    @Transactional
    public void updateOccupation(Integer id, Boolean isOccupied) throws ServiceException {
        CarPlace carPlace = carPlaceDao.findById(id);
        if (carPlace == null) {
            throw new ServiceException("Car place not found with id: " + id);
        }
        carPlaceDao.updateOccupation(id, isOccupied);
    }

    @Transactional
    public void delete(Integer id) throws ServiceException {
        if (!carPlaceDao.existsById(id)) {
            throw new ServiceException("Car place not found with id: " + id);
        }
        carPlaceDao.delete(id);
    }

    private CarPlaceDto convertToDto(CarPlace carPlace) {
        if (carPlace == null) return null;
        return new CarPlaceDto(
                carPlace.getId(),
                carPlace.getSquare(),
                carPlace.getCarLift(),
                carPlace.getIsOccupied()
        );
    }
}