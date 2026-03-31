package com.example.task.service;

import com.example.task.dao.ServiceAdvisorDao;
import com.example.task.dto.ServiceAdvisorDto;
import com.example.task.exception.ServiceException;
import com.example.task.model.ServiceAdvisor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceAdvisorServiceTest {

    @Mock
    private ServiceAdvisorDao serviceAdvisorDao;

    @InjectMocks
    private ServiceAdvisorService serviceAdvisorService;

    @Test
    public void findAll_ShouldReturnAllAdvisors() {
        List<ServiceAdvisor> daoAdvisors = List.of(
                new ServiceAdvisor(1, "Alice Johnson"),
                new ServiceAdvisor(2, "Bob Smith")
        );
        List<ServiceAdvisorDto> comparedAdvisorsList = Arrays.asList(
                new ServiceAdvisorDto(1, "Alice Johnson"),
                new ServiceAdvisorDto(2, "Bob Smith")
        );

        when(serviceAdvisorDao.findAll()).thenReturn(daoAdvisors);

        List<ServiceAdvisorDto> actualDtos = serviceAdvisorService.findAll();

        comparedAdvisorsList.sort(Comparator.comparing(ServiceAdvisorDto::getId));
        actualDtos.sort(Comparator.comparing(ServiceAdvisorDto::getId));

        assertEquals(comparedAdvisorsList, actualDtos);
    }

    @Test
    public void findAll_ShouldReturnEmptyList() {
        List<ServiceAdvisor> daoAdvisors = Collections.emptyList();
        List<ServiceAdvisorDto> comparedAdvisorsList = Collections.emptyList();

        when(serviceAdvisorDao.findAll()).thenReturn(daoAdvisors);

        List<ServiceAdvisorDto> actualDtos = serviceAdvisorService.findAll();

        assertEquals(comparedAdvisorsList, actualDtos);
    }

    @Test
    public void findById_ShouldReturnAdvisor() throws ServiceException {
        int id = 1;
        ServiceAdvisor daoAdvisor = new ServiceAdvisor(id, "Alice Johnson");
        ServiceAdvisorDto comparedAdvisorDto = new ServiceAdvisorDto(id, "Alice Johnson");

        when(serviceAdvisorDao.findById(id)).thenReturn(daoAdvisor);

        ServiceAdvisorDto actualDto = serviceAdvisorService.findById(id);

        assertEquals(comparedAdvisorDto, actualDto);
    }

    @Test
    public void findById_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        when(serviceAdvisorDao.findById(nonExistingId)).thenReturn(null);

        assertThrows(ServiceException.class, () -> serviceAdvisorService.findById(nonExistingId));
    }

    @Test
    public void create_WithValidData_ShouldReturnCreatedAdvisor() throws ServiceException {
        ServiceAdvisorDto inputDto = new ServiceAdvisorDto();
        inputDto.setName("New Advisor");

        ServiceAdvisor savedAdvisor = new ServiceAdvisor(10, "New Advisor");
        ServiceAdvisorDto comparedAdvisorDto = new ServiceAdvisorDto(10, "New Advisor");

        when(serviceAdvisorDao.existsByName("New Advisor")).thenReturn(false);
        when(serviceAdvisorDao.save(any(ServiceAdvisor.class))).thenReturn(10);
        when(serviceAdvisorDao.findById(10)).thenReturn(savedAdvisor);

        ServiceAdvisorDto actualDto = serviceAdvisorService.create(inputDto);

        assertEquals(comparedAdvisorDto, actualDto);
        verify(serviceAdvisorDao, times(1)).save(any(ServiceAdvisor.class));
    }

    @Test
    public void create_WithNullName_ShouldThrowServiceException() {
        ServiceAdvisorDto inputDto = new ServiceAdvisorDto();
        inputDto.setName(null);

        assertThrows(ServiceException.class, () -> serviceAdvisorService.create(inputDto));
    }

    @Test
    public void create_WithEmptyName_ShouldThrowServiceException() {
        ServiceAdvisorDto inputDto = new ServiceAdvisorDto();
        inputDto.setName("");

        assertThrows(ServiceException.class, () -> serviceAdvisorService.create(inputDto));
    }

    @Test
    public void create_WithExistingName_ShouldThrowServiceException() {
        ServiceAdvisorDto inputDto = new ServiceAdvisorDto();
        inputDto.setName("Existing Name");

        when(serviceAdvisorDao.existsByName("Existing Name")).thenReturn(true);

        assertThrows(ServiceException.class, () -> serviceAdvisorService.create(inputDto));
    }

    @Test
    public void update_WithValidData_ShouldReturnUpdatedAdvisor() throws ServiceException {
        int id = 1;
        ServiceAdvisorDto inputDto = new ServiceAdvisorDto();
        inputDto.setName("Updated Name");

        ServiceAdvisor existingAdvisor = new ServiceAdvisor(id, "Old Name");

        ServiceAdvisorDto comparedAdvisorDto = new ServiceAdvisorDto(id, "Updated Name");

        when(serviceAdvisorDao.findById(id)).thenReturn(existingAdvisor);
        when(serviceAdvisorDao.existsByName("Updated Name")).thenReturn(false);

        ServiceAdvisorDto actualDto = serviceAdvisorService.update(id, inputDto);

        assertEquals(comparedAdvisorDto, actualDto);
        assertEquals("Updated Name", existingAdvisor.getName());
        verify(serviceAdvisorDao, times(1)).update(existingAdvisor);
    }

    @Test
    public void update_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        ServiceAdvisorDto inputDto = new ServiceAdvisorDto();
        inputDto.setName("Updated Name");

        when(serviceAdvisorDao.findById(nonExistingId)).thenReturn(null);

        assertThrows(ServiceException.class,
                () -> serviceAdvisorService.update(nonExistingId, inputDto)
        );
    }

    @Test
    public void update_WithNewNameThatAlreadyExists_ShouldThrowServiceException() throws ServiceException {
        int id = 1;
        ServiceAdvisorDto inputDto = new ServiceAdvisorDto();
        inputDto.setName("Existing Name");

        ServiceAdvisor existingAdvisor = new ServiceAdvisor(id, "Old Name");

        when(serviceAdvisorDao.findById(id)).thenReturn(existingAdvisor);
        when(serviceAdvisorDao.existsByName("Existing Name")).thenReturn(true);

        assertThrows(ServiceException.class, () -> serviceAdvisorService.update(id, inputDto));
    }

    @Test
    public void updateName_WithValidData_ShouldUpdateSuccessfully() throws ServiceException {
        int id = 1;
        String newName = "New Name";
        ServiceAdvisor existingAdvisor = new ServiceAdvisor(id, "Old Name");

        when(serviceAdvisorDao.findById(id)).thenReturn(existingAdvisor);
        when(serviceAdvisorDao.existsByName(newName)).thenReturn(false);

        serviceAdvisorService.updateName(id, newName);

        verify(serviceAdvisorDao, times(1)).updateName(id, newName);
    }

    @Test
    public void updateName_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        String newName = "New Name";

        when(serviceAdvisorDao.findById(nonExistingId)).thenReturn(null);

        assertThrows(ServiceException.class,
                () -> serviceAdvisorService.updateName(nonExistingId, newName)
        );
    }

    @Test
    public void updateName_WithNullName_ShouldThrowServiceException() throws ServiceException {
        int id = 1;
        String newName = null;
        ServiceAdvisor existingAdvisor = new ServiceAdvisor(id, "Old Name");

        when(serviceAdvisorDao.findById(id)).thenReturn(existingAdvisor);

        assertThrows(ServiceException.class,
                () -> serviceAdvisorService.updateName(id, newName)
        );
    }

    @Test
    public void updateName_WithEmptyName_ShouldThrowServiceException() throws ServiceException {
        int id = 1;
        String newName = "";
        ServiceAdvisor existingAdvisor = new ServiceAdvisor(id, "Old Name");

        when(serviceAdvisorDao.findById(id)).thenReturn(existingAdvisor);

        assertThrows(ServiceException.class,
                () -> serviceAdvisorService.updateName(id, newName)
        );
    }

    @Test
    public void updateName_WithExistingName_ShouldThrowServiceException() throws ServiceException {
        int id = 1;
        String newName = "Existing Name";
        ServiceAdvisor existingAdvisor = new ServiceAdvisor(id, "Old Name");

        when(serviceAdvisorDao.findById(id)).thenReturn(existingAdvisor);
        when(serviceAdvisorDao.existsByName(newName)).thenReturn(true);

        assertThrows(ServiceException.class,
                () -> serviceAdvisorService.updateName(id, newName)
        );
    }

    @Test
    public void delete_WithExistingId_ShouldDeleteSuccessfully() throws ServiceException {
        int id = 1;

        when(serviceAdvisorDao.existsById(id)).thenReturn(true);

        serviceAdvisorService.delete(id);

        verify(serviceAdvisorDao, times(1)).delete(id);
    }

    @Test
    public void delete_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;

        when(serviceAdvisorDao.existsById(nonExistingId)).thenReturn(false);

        assertThrows(ServiceException.class, () -> serviceAdvisorService.delete(nonExistingId));
    }
}