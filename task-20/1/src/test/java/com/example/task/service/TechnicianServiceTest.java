package com.example.task.service;

import com.example.task.dao.TechnicianDao;
import com.example.task.dto.TechnicianDto;
import com.example.task.exception.ServiceException;
import com.example.task.model.Technician;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TechnicianServiceTest {

    @Mock
    private TechnicianDao technicianDao;

    @InjectMocks
    private TechnicianService technicianService;

    @Test
    public void findAll_ShouldReturnAllTechnicians() {
        Technician techA = new Technician(1, "Tech A", "Mechanics", true);
        Technician techB = new Technician(2, "Tech B", "Electronics", false);
        List<Technician> daoTechnicians = List.of(techA, techB);

        TechnicianDto comparedTechADto = new TechnicianDto(1, "Tech A", "Mechanics", true);
        TechnicianDto comparedTechBDto = new TechnicianDto(2, "Tech B", "Electronics", false);
        List<TechnicianDto> comparedTechniciansList = List.of(comparedTechADto, comparedTechBDto);

        when(technicianDao.findAll()).thenReturn(daoTechnicians);

        List<TechnicianDto> actualDtos = technicianService.findAll();

        assertEquals(comparedTechniciansList, actualDtos);
    }

    @Test
    public void findAll_ShouldReturnEmptyList() {
        List<Technician> daoTechnicians = Collections.emptyList();
        List<TechnicianDto> comparedTechniciansList = Collections.emptyList();

        when(technicianDao.findAll()).thenReturn(daoTechnicians);

        List<TechnicianDto> actualDtos = technicianService.findAll();

        assertEquals(comparedTechniciansList, actualDtos);
    }

    @Test
    public void findById_ShouldReturnTechnician() throws ServiceException {
        int id = 1;
        Technician daoTechnician = new Technician(id, "Tech A", "Mechanics", true);
        TechnicianDto comparedTechnicianDto = new TechnicianDto(id, "Tech A", "Mechanics", true);

        when(technicianDao.findById(id)).thenReturn(daoTechnician);

        TechnicianDto actualDto = technicianService.findById(id);

        assertEquals(comparedTechnicianDto, actualDto);
    }

    @Test
    public void findById_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        when(technicianDao.findById(nonExistingId)).thenReturn(null);

        assertThrows(ServiceException.class, () -> technicianService.findById(nonExistingId));
    }

    @Test
    public void create_WithValidData_ShouldReturnCreatedTechnician() {
        TechnicianDto inputDto = new TechnicianDto();
        inputDto.setName("New Tech");
        inputDto.setSpecialization("Welding");
        inputDto.setIsAvailable(true);

        Technician savedTechnician = new Technician(10, "New Tech", "Welding", true);
        TechnicianDto comparedTechnicianDto = new TechnicianDto(10, "New Tech", "Welding", true);

        when(technicianDao.save(any(Technician.class))).thenReturn(10);
        when(technicianDao.findById(10)).thenReturn(savedTechnician);

        TechnicianDto actualDto = technicianService.create(inputDto);

        assertEquals(comparedTechnicianDto, actualDto);
        verify(technicianDao, times(1)).save(any(Technician.class));
    }

    @Test
    public void update_WithValidData_ShouldReturnUpdatedTechnician() throws ServiceException {
        int id = 10;
        TechnicianDto inputDto = new TechnicianDto();
        inputDto.setName("Updated Tech");
        inputDto.setSpecialization("Advanced Welding");
        inputDto.setIsAvailable(false);

        Technician existingTechnician = new Technician(id, "Old Name", "Old Spec", true);

        when(technicianDao.findById(id)).thenReturn(existingTechnician);

        TechnicianDto actualDto = technicianService.update(id, inputDto);

        assertEquals(id, actualDto.getId());
        assertEquals("Updated Tech", actualDto.getName());
        assertEquals("Advanced Welding", actualDto.getSpecialization());
        assertFalse(actualDto.getIsAvailable());
        verify(technicianDao, times(1)).update(existingTechnician);
    }

    @Test
    public void update_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        TechnicianDto inputDto = new TechnicianDto();
        inputDto.setName("Updated Tech");

        when(technicianDao.findById(nonExistingId)).thenReturn(null);

        assertThrows(ServiceException.class,
                () -> technicianService.update(nonExistingId, inputDto)
        );
    }

    @Test
    public void updateAvailability_WithExistingId_ShouldUpdateSuccessfully() throws ServiceException {
        int id = 1;
        boolean newAvailability = false;
        Technician existingTechnician = new Technician(id, "Tech A", "Mechanics", true);

        when(technicianDao.findById(id)).thenReturn(existingTechnician);

        technicianService.updateAvailability(id, newAvailability);

        verify(technicianDao, times(1))
                .updateAvailability(id, newAvailability);
    }

    @Test
    public void updateAvailability_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        boolean newAvailability = false;

        when(technicianDao.findById(nonExistingId)).thenReturn(null);

        assertThrows(ServiceException.class,
                () -> technicianService.updateAvailability(nonExistingId, newAvailability)
        );
    }

    @Test
    public void updateSpecialization_WithExistingId_ShouldUpdateSuccessfully() throws ServiceException {
        int id = 1;
        String newSpec = "Advanced Mechanics";
        Technician existingTechnician = new Technician(id, "Tech A", "Mechanics", true);

        when(technicianDao.findById(id)).thenReturn(existingTechnician);

        technicianService.updateSpecialization(id, newSpec);

        verify(technicianDao, times(1)).updateSpecialization(id, newSpec);
    }

    @Test
    public void updateSpecialization_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        String newSpec = "Advanced Mechanics";

        when(technicianDao.findById(nonExistingId)).thenReturn(null);

        assertThrows(ServiceException.class,
                () -> technicianService.updateSpecialization(nonExistingId, newSpec)
        );
    }

    @Test
    public void delete_WithExistingId_ShouldDeleteSuccessfully() throws ServiceException {
        int id = 1;

        when(technicianDao.existsById(id)).thenReturn(true);

        technicianService.delete(id);

        verify(technicianDao, times(1)).delete(id);
    }

    @Test
    public void delete_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;

        when(technicianDao.existsById(nonExistingId)).thenReturn(false);

        assertThrows(ServiceException.class, () -> technicianService.delete(nonExistingId));
    }
}