package com.example.task.service;

import com.example.task.dao.TechnicianDao;
import com.example.task.dto.TechnicianDto;
import com.example.task.exception.ServiceException;
import com.example.task.model.Technician;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnicianService {

    private final TechnicianDao technicianDao;

    @Autowired
    public TechnicianService(TechnicianDao technicianDao) {
        this.technicianDao = technicianDao;
    }

    @Transactional(readOnly = true)
    public List<TechnicianDto> findAll() {
        return technicianDao.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TechnicianDto findById(Integer id) throws ServiceException {
        Technician technician = technicianDao.findById(id);
        if (technician == null) {
            throw new ServiceException("Technician not found with id: " + id);
        }
        return convertToDto(technician);
    }

    @Transactional(readOnly = true)
    public List<TechnicianDto> findAvailableTechnicians() {
        return technicianDao.findAvailableTechnicians().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TechnicianDto> findAllSortedByName() {
        return technicianDao.findAllSortedByName().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TechnicianDto> findAllSortedByAvailability() {
        return technicianDao.findAllSortedByAvailability().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TechnicianDto create(TechnicianDto technicianDto) {
        Technician technician = new Technician(
                technicianDto.getName(),
                technicianDto.getSpecialization(),
                technicianDto.getIsAvailable() != null ? technicianDto.getIsAvailable() : true
        );

        Integer id = technicianDao.save(technician);
        return convertToDto(technicianDao.findById(id));
    }

    @Transactional
    public TechnicianDto update(Integer id, TechnicianDto technicianDto) throws ServiceException {
        Technician technician = technicianDao.findById(id);
        if (technician == null) {
            throw new ServiceException("Technician not found with id: " + id);
        }

        if (technicianDto.getName() != null) {
            technician.setName(technicianDto.getName());
        }
        if (technicianDto.getSpecialization() != null) {
            technician.setSpecialization(technicianDto.getSpecialization());
        }
        if (technicianDto.getIsAvailable() != null) {
            technician.setIsAvailable(technicianDto.getIsAvailable());
        }

        technicianDao.update(technician);
        return convertToDto(technician);
    }

    @Transactional
    public void updateAvailability(Integer id, Boolean isAvailable) throws ServiceException {
        Technician technician = technicianDao.findById(id);
        if (technician == null) {
            throw new ServiceException("Technician not found with id: " + id);
        }
        technicianDao.updateAvailability(id, isAvailable);
    }

    @Transactional
    public void updateSpecialization(Integer id, String specialization) throws ServiceException {
        Technician technician = technicianDao.findById(id);
        if (technician == null) {
            throw new ServiceException("Technician not found with id: " + id);
        }
        technicianDao.updateSpecialization(id, specialization);
    }

    @Transactional
    public void delete(Integer id) throws ServiceException {
        if (!technicianDao.existsById(id)) {
            throw new ServiceException("Technician not found with id: " + id);
        }
        technicianDao.delete(id);
    }

    private TechnicianDto convertToDto(Technician technician) {
        if (technician == null) return null;

        return new TechnicianDto(
                technician.getId(),
                technician.getName(),
                technician.getSpecialization(),
                technician.getIsAvailable()
        );
    }
}