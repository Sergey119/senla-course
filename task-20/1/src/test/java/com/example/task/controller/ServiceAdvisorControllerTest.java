package com.example.task.controller;

import com.example.task.dto.ServiceAdvisorDto;
import com.example.task.exception.ServiceException;
import com.example.task.service.ServiceAdvisorService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ServiceAdvisorControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ServiceAdvisorService serviceAdvisorService;

    @InjectMocks
    private ServiceAdvisorController serviceAdvisorController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(serviceAdvisorController).build();
    }

    @Test
    public void getAllServiceAdvisors_ShouldReturnAllAdvisors() throws Exception {
        List<ServiceAdvisorDto> comparedAdvisorsList = Arrays.asList(
                new ServiceAdvisorDto(1, "Alice Johnson"),
                new ServiceAdvisorDto(2, "Bob Smith")
        );

        when(serviceAdvisorService.findAll()).thenReturn(comparedAdvisorsList);

        MvcResult mvcResult = mockMvc.perform(get("/api/service-advisors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<ServiceAdvisorDto> receivedAdvisors = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){}
        );

        assertEquals(comparedAdvisorsList, receivedAdvisors);
    }

    @Test
    public void getAllServiceAdvisors_ShouldReturnEmptyList() throws Exception {
        List<ServiceAdvisorDto> comparedAdvisorsList = Collections.emptyList();
        when(serviceAdvisorService.findAll()).thenReturn(comparedAdvisorsList);

        mockMvc.perform(get("/api/service-advisors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getServiceAdvisorById_ShouldReturnAdvisor() throws Exception {
        int id = 1;
        ServiceAdvisorDto comparedAdvisorDto = new ServiceAdvisorDto(id, "Alice Johnson");

        when(serviceAdvisorService.findById(id)).thenReturn(comparedAdvisorDto);

        MvcResult mvcResult = mockMvc.perform(get("/api/service-advisors/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ServiceAdvisorDto receivedAdvisor = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(comparedAdvisorDto, receivedAdvisor);
    }

    @Test
    public void getServiceAdvisorById_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        when(serviceAdvisorService.findById(nonExistingId)).thenThrow(
                new ServiceException("Service advisor not found with id: " + nonExistingId)
        );

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> serviceAdvisorController.getServiceAdvisorById(nonExistingId)
        );

        assertEquals("Service advisor not found with id: " +
                nonExistingId, thrownException.getMessage());
        verify(serviceAdvisorService, times(1))
                .findById(nonExistingId);
    }

    @Test
    public void getServiceAdvisorsSortedByName_ShouldReturnSortedAdvisors() throws Exception {
        List<ServiceAdvisorDto> comparedAdvisorsList = Arrays.asList(
                new ServiceAdvisorDto(2, "Alice"),
                new ServiceAdvisorDto(1, "Bob")
        );

        when(serviceAdvisorService.findAllSortedByName())
                .thenReturn(comparedAdvisorsList);

        MvcResult mvcResult = mockMvc.perform(get("/api/service-advisors/sorted/name"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<ServiceAdvisorDto> receivedAdvisors = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(comparedAdvisorsList, receivedAdvisors);
    }

    @Test
    public void getServiceAdvisorsSortedByName_WithNoAdvisors_ShouldReturnEmptyList() throws Exception {
        List<ServiceAdvisorDto> comparedAdvisorsList = Collections.emptyList();
        when(serviceAdvisorService.findAllSortedByName()).thenReturn(comparedAdvisorsList);

        mockMvc.perform(get("/api/service-advisors/sorted/name"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getServiceAdvisorsCount_ShouldReturnCount() throws Exception {
        long comparedCount = 5L;
        when(serviceAdvisorService.countAll()).thenReturn(comparedCount);

        MvcResult mvcResult = mockMvc.perform(get("/api/service-advisors/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        long receivedCount = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){}
        );

        assertEquals(comparedCount, receivedCount);
    }

    @Test
    public void checkServiceAdvisorExists_() throws Exception {
        String name = "Alice Johnson";
        boolean expectedExists = true;
        when(serviceAdvisorService.existsByName(name)).thenReturn(expectedExists);

        MvcResult mvcResult = mockMvc.perform(get("/api/service-advisors/exists")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        boolean actualExists = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(expectedExists, actualExists);
    }

    @Test
    public void checkServiceAdvisorExists_WithNonExistingName_ShouldReturnFalse() throws Exception {
        String name = "Non Existing Name";
        boolean expectedExists = false;
        when(serviceAdvisorService.existsByName(name)).thenReturn(expectedExists);

        MvcResult mvcResult = mockMvc.perform(get("/api/service-advisors/exists")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        boolean actualExists = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){}
        );
        assertEquals(expectedExists, actualExists);
    }

    @Test
    public void createServiceAdvisor_ShouldReturnCreatedAdvisor() throws Exception {
        ServiceAdvisorDto sentAdvisorDto = new ServiceAdvisorDto();
        sentAdvisorDto.setName("New Advisor");

        ServiceAdvisorDto comparedAdvisorDto = new ServiceAdvisorDto(1, "New Advisor");

        when(serviceAdvisorService.create(any(ServiceAdvisorDto.class)))
                .thenReturn(comparedAdvisorDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/service-advisors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sentAdvisorDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ServiceAdvisorDto receivedDto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(comparedAdvisorDto, receivedDto);
    }

    @Test
    public void createServiceAdvisor_WithInvalidData_ShouldThrowServiceException() {
        ServiceAdvisorDto sentAdvisorDto = new ServiceAdvisorDto();
        sentAdvisorDto.setName("");

        when(serviceAdvisorService.create(any(ServiceAdvisorDto.class))).thenThrow(
                new ServiceException("Service advisor name cannot be empty")
        );
        
        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> serviceAdvisorController.createServiceAdvisor(sentAdvisorDto)
        );

        assertEquals("Service advisor name cannot be empty",
                thrownException.getMessage());
        verify(serviceAdvisorService, times(1))
                .create(any(ServiceAdvisorDto.class));
    }
    
    @Test
    public void updateServiceAdvisor_ShouldReturnUpdatedAdvisor() throws Exception {
        int id = 1;
        ServiceAdvisorDto sentAdvisorDto = new ServiceAdvisorDto();
        sentAdvisorDto.setName("Updated Name");

        ServiceAdvisorDto comparedAdvisorDto = new ServiceAdvisorDto(id, "Updated Name");

        when(serviceAdvisorService.update(id, sentAdvisorDto))
                .thenReturn(comparedAdvisorDto);

        MvcResult mvcResult = mockMvc.perform(put("/api/service-advisors/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sentAdvisorDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ServiceAdvisorDto receivedDto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){}
        );

        assertEquals(comparedAdvisorDto, receivedDto);
    }

    @Test
    public void updateServiceAdvisor_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        ServiceAdvisorDto sentAdvisorDto = new ServiceAdvisorDto();
        sentAdvisorDto.setName("Updated Name");

        when(serviceAdvisorService.update(nonExistingId, sentAdvisorDto))
                .thenThrow(new ServiceException("Service advisor not found with id: " + nonExistingId));

        ServiceException thrownException = assertThrows(
                ServiceException.class,
                () -> serviceAdvisorController.updateServiceAdvisor(nonExistingId, sentAdvisorDto)
        );

        assertEquals("Service advisor not found with id: " +
                nonExistingId, thrownException.getMessage()
        );
        verify(serviceAdvisorService, times(1))
                .update(nonExistingId, sentAdvisorDto);
    }

    @Test
    public void updateServiceAdvisorName_ShouldReturnOk() throws Exception {
        int id = 1;
        String newName = "New Name";

        doNothing().when(serviceAdvisorService).updateName(id, newName);

        mockMvc.perform(patch("/api/service-advisors/{id}/name", id)
                        .param("newName", newName))
                .andExpect(status().isOk());
    }

    @Test
    public void updateServiceAdvisorName_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        String newName = "New Name";

        doThrow(new ServiceException("Service advisor not found with id: " + nonExistingId))
                .when(serviceAdvisorService).updateName(nonExistingId, newName);

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> serviceAdvisorController.updateServiceAdvisorName(nonExistingId, newName)
        );

        assertEquals("Service advisor not found with id: "
                + nonExistingId, thrownException.getMessage());
        verify(serviceAdvisorService, times(1))
                .updateName(nonExistingId, newName);
    }

    @Test
    public void deleteServiceAdvisor_ShouldReturnNoContent() throws Exception {
        int id = 1;

        doNothing().when(serviceAdvisorService).delete(id);

        mockMvc.perform(delete("/api/service-advisors/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteServiceAdvisor_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;

        doThrow(new ServiceException("Service advisor not found with id: " + nonExistingId))
                .when(serviceAdvisorService).delete(nonExistingId);

        ServiceException thrownException = assertThrows(
                ServiceException.class,
                () -> serviceAdvisorController.deleteServiceAdvisor(nonExistingId)
        );

        assertEquals("Service advisor not found with id: "
                + nonExistingId, thrownException.getMessage());
        verify(serviceAdvisorService, times(1))
                .delete(nonExistingId);
    }
}