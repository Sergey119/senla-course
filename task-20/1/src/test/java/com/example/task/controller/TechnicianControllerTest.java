package com.example.task.controller;

import com.example.task.dto.TechnicianDto;
import com.example.task.exception.ServiceException;
import com.example.task.service.TechnicianService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TechnicianControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TechnicianService technicianService;

    @InjectMocks
    private TechnicianController technicianController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(technicianController).build();
    }

    @Test
    public void getAllTechnicians_ShouldReturnAllTechnicians() throws Exception {
        List<TechnicianDto> comparedTechniciansList = List.of(
                new TechnicianDto(1, "Tech A", "Mechanics", true),
                new TechnicianDto(2, "Tech B", "Electronics", false)
        );

        when(technicianService.findAll()).thenReturn(comparedTechniciansList);

        MvcResult mvcResult = mockMvc.perform(get("/api/technicians"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<TechnicianDto> receivedTechnicians = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(comparedTechniciansList, receivedTechnicians);
    }

    @Test
    public void getAllTechnicians_ShouldReturnEmptyList() throws Exception {
        List<TechnicianDto> comparedTechniciansList = Collections.emptyList();
        when(technicianService.findAll()).thenReturn(comparedTechniciansList);

        mockMvc.perform(get("/api/technicians"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getTechnicianById_ShouldReturnTechnician() throws Exception {
        int id = 1;
        TechnicianDto comparedTechnicianDto = new TechnicianDto(
                id, "Tech A", "Mechanics", true
        );

        when(technicianService.findById(id)).thenReturn(comparedTechnicianDto);

        MvcResult mvcResult = mockMvc.perform(get("/api/technicians/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        TechnicianDto receivedTechnician = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), TechnicianDto.class);

        assertEquals(comparedTechnicianDto, receivedTechnician);
    }

    @Test
    public void getTechnicianById_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        when(technicianService.findById(nonExistingId)).thenThrow(
                new ServiceException("Technician not found with id: " + nonExistingId));

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> technicianController.getTechnicianById(nonExistingId)
        );

        assertEquals("Technician not found with id: " +
                nonExistingId, thrownException.getMessage());
        verify(technicianService, times(1)).findById(nonExistingId);
    }


    @Test
    public void getAvailableTechnicians_ShouldReturnAvailableTechnicians() throws Exception {
        List<TechnicianDto> comparedTechniciansList = List.of(
                new TechnicianDto(1, "Tech A", "Mechanics", true),
                new TechnicianDto(3, "Tech C", "Painting", true)
        );

        when(technicianService.findAvailableTechnicians()).thenReturn(comparedTechniciansList);

        MvcResult mvcResult = mockMvc.perform(get("/api/technicians/available"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<TechnicianDto> receivedTechnicians = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(comparedTechniciansList, receivedTechnicians);
    }

    @Test
    public void getAvailableTechnicians_ShouldReturnEmptyList() throws Exception {
        List<TechnicianDto> comparedTechniciansList = Collections.emptyList();
        when(technicianService.findAvailableTechnicians()).thenReturn(comparedTechniciansList);

        mockMvc.perform(get("/api/technicians/available"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getTechniciansSortedByName_ShouldReturnSortedTechnicians() throws Exception {
        List<TechnicianDto> comparedTechniciansList = List.of(
                new TechnicianDto(2, "Alice", "Mechanics", true),
                new TechnicianDto(1, "Bob", "Electronics", false)
        );

        when(technicianService.findAllSortedByName()).thenReturn(comparedTechniciansList);

        MvcResult mvcResult = mockMvc.perform(get("/api/technicians/sorted/name"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<TechnicianDto> receivedTechnicians = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(comparedTechniciansList, receivedTechnicians);
    }

    @Test
    public void getTechniciansSortedByAvailability_ShouldReturnSortedTechnicians() throws Exception {
        List<TechnicianDto> comparedTechniciansList = List.of(
                new TechnicianDto(1, "Tech A", "Mechanics", true),
                new TechnicianDto(2, "Tech B", "Electronics", false)
        );

        when(technicianService.findAllSortedByAvailability()).thenReturn(comparedTechniciansList);

        MvcResult mvcResult = mockMvc.perform(get("/api/technicians/sorted/availability"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<TechnicianDto> receivedTechnicians = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(comparedTechniciansList, receivedTechnicians);
    }

    @Test
    public void createTechnician_ShouldReturnCreatedTechnician() throws Exception {
        TechnicianDto sentTechnicianDto = new TechnicianDto();
        sentTechnicianDto.setName("New Tech");
        sentTechnicianDto.setSpecialization("Welding");
        sentTechnicianDto.setIsAvailable(true);

        TechnicianDto comparedTechnicianDto = new TechnicianDto(
                10, "New Tech", "Welding", true
        );

        when(technicianService.create(any(TechnicianDto.class)))
                .thenReturn(comparedTechnicianDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/technicians")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sentTechnicianDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        TechnicianDto receivedDto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), TechnicianDto.class);

        assertEquals(comparedTechnicianDto, receivedDto);
    }

    @Test
    public void updateTechnician_ShouldReturnUpdatedTechnician() throws Exception {
        int id = 10;
        TechnicianDto sentTechnicianDto = new TechnicianDto();
        sentTechnicianDto.setName("Updated Tech");
        sentTechnicianDto.setSpecialization("Advanced Welding");
        sentTechnicianDto.setIsAvailable(false);

        TechnicianDto comparedTechnicianDto = new TechnicianDto(
                id, "Updated Tech", "Advanced Welding", false
        );

        when(technicianService.update(id, sentTechnicianDto))
                .thenReturn(comparedTechnicianDto);

        MvcResult mvcResult = mockMvc.perform(put("/api/technicians/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sentTechnicianDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        TechnicianDto receivedDto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), TechnicianDto.class);

        assertEquals(comparedTechnicianDto, receivedDto);
    }

    @Test
    public void updateTechnician_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        TechnicianDto sentTechnicianDto = new TechnicianDto();
        sentTechnicianDto.setName("Updated Tech");
        sentTechnicianDto.setSpecialization("Advanced Welding");
        sentTechnicianDto.setIsAvailable(false);

        when(technicianService.update(nonExistingId, sentTechnicianDto))
                .thenThrow(new ServiceException("Technician not found with id: " + nonExistingId));

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> technicianController.updateTechnician(nonExistingId, sentTechnicianDto)
        );

        assertEquals("Technician not found with id: "
                + nonExistingId, thrownException.getMessage());
        verify(technicianService, times(1))
                .update(nonExistingId, sentTechnicianDto);
    }

    @Test
    public void updateAvailability_ShouldReturnOk() throws Exception {
        int id = 1;
        boolean newAvailability = false;

        mockMvc.perform(patch("/api/technicians/{id}/availability", id)
                        .param("available", Boolean.toString(newAvailability)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateAvailability_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        Boolean newAvailability = false;

        doThrow(new ServiceException("Technician not found with id: " + nonExistingId))
                .when(technicianService).updateAvailability(nonExistingId, newAvailability);

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> technicianController.updateAvailability(nonExistingId, newAvailability)
        );

        assertEquals("Technician not found with id: "
                + nonExistingId, thrownException.getMessage());
        verify(technicianService, times(1))
                .updateAvailability(nonExistingId, newAvailability);
    }

    @Test
    public void updateSpecialization_ShouldReturnOk() throws Exception {
        int id = 1;
        String newSpec = "Advanced Mechanics";

        mockMvc.perform(patch("/api/technicians/{id}/specialization", id)
                        .param("spec", newSpec))
                .andExpect(status().isOk());
    }

    @Test
    public void updateSpecialization_WithNonExistingId_ShouldThrowServiceException(){
        int nonExistingId = 999;
        String newSpec = "Advanced Mechanics";

        doThrow(new ServiceException("Technician not found with id: " + nonExistingId))
                .when(technicianService).updateSpecialization(nonExistingId, newSpec);

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> technicianController.updateSpecialization(nonExistingId, newSpec)
        );

        assertEquals("Technician not found with id: "
                + nonExistingId, thrownException.getMessage());
        verify(technicianService, times(1))
                .updateSpecialization(nonExistingId, newSpec);
    }

    @Test
    public void deleteTechnician_ShouldReturnNoContent() throws Exception {
        int id = 1;

        doNothing().when(technicianService).delete(id);

        mockMvc.perform(delete("/api/technicians/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteTechnician_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;

        doThrow(new ServiceException("Technician not found with id: " + nonExistingId))
                .when(technicianService).delete(nonExistingId);

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> technicianController.deleteTechnician(nonExistingId)
        );

        assertEquals("Technician not found with id: "
                + nonExistingId, thrownException.getMessage());
        verify(technicianService, times(1)).delete(nonExistingId);
    }

}