package com.example.task.controller;

import com.example.task.dto.CarPlaceDto;
import com.example.task.exception.ServiceException;
import com.example.task.service.CarPlaceService;
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
public class CarPlaceControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CarPlaceService carPlaceService;
    @InjectMocks
    private CarPlaceController carPlaceController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(carPlaceController).build();
    }

    @Test
    public void getAllCarPlaces_ShouldReturnAllCarPlaces() throws Exception {
        List<CarPlaceDto> comparedCarPlaceDtoList = List.of(
                new CarPlaceDto(1,25,true,false),
                new CarPlaceDto(2,22,false,false),
                new CarPlaceDto(3,28,true,false)
        );

        List<CarPlaceDto> testCarPlaceDtoList = List.of(
                new CarPlaceDto(1,25,true,false),
                new CarPlaceDto(2,22,false,false),
                new CarPlaceDto(3,28,true,false)
        );

        when(carPlaceService.findAll()).thenReturn(testCarPlaceDtoList);
        MvcResult mvcResult = mockMvc.perform(get("/api/car-places"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(
                        new MediaType(MediaType.APPLICATION_JSON))
                ).andReturn();

        List<CarPlaceDto> receivedCarPlace = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){}
        );

        assertEquals(receivedCarPlace, comparedCarPlaceDtoList);
    }

    @Test
    public void getAllCarPlaces_ShouldReturnEmptyList() throws Exception {
        List<CarPlaceDto> testCarPlaceDtoList = Collections.emptyList();
        when(carPlaceService.findAll()).thenReturn(testCarPlaceDtoList);
        mockMvc.perform(get("/api/car-places"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(
                        new MediaType(MediaType.APPLICATION_JSON))
                );
    }


    @Test
    public void getCarPlaceById_ShouldReturnCarPlace() throws Exception {
        int id = 1;
        CarPlaceDto comparedCarPlaceDto = new CarPlaceDto(
                id, 25, true, false
        );

        CarPlaceDto testCarPlaceDto = new CarPlaceDto(
                id, 25, true, false
        );

        when(carPlaceService.findById(id)).thenReturn(testCarPlaceDto);

        MvcResult mvcResult = mockMvc.perform(get("/api/car-places/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CarPlaceDto receivedCarPlace = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){}
        );

        assertEquals(receivedCarPlace, comparedCarPlaceDto);
    }

    
    @Test
    public void getCarPlaceById_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        when(carPlaceService.findById(nonExistingId)).thenThrow(
                new ServiceException("Car place not found with id: " + nonExistingId)
        );

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> carPlaceController.getCarPlaceById(nonExistingId)
        );

        assertEquals("Car place not found with id: " +
                nonExistingId, thrownException.getMessage());

        verify(carPlaceService, times(1)).findById(nonExistingId);
    }

    @Test
    public void getAvailableCarPlaces_ShouldReturnAvailableCarPlaces() throws Exception {
        List<CarPlaceDto> comparedAvailableCarPlaceDtoList = List.of(
                new CarPlaceDto(1, 25, true, false),
                new CarPlaceDto(3, 28, true, false)
        );

        List<CarPlaceDto> testAvailableCarPlaceDtoList = List.of(
                new CarPlaceDto(1, 25, true, false),
                new CarPlaceDto(3, 28, true, false)
        );

        when(carPlaceService.findAvailableCarPlaces()).thenReturn(testAvailableCarPlaceDtoList);

        MvcResult mvcResult = mockMvc.perform(get("/api/car-places/available"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<CarPlaceDto> receivedPlaces = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){}
        );

        assertEquals(comparedAvailableCarPlaceDtoList, receivedPlaces);
    }

    @Test
    public void getAvailableCarPlaces_ShouldReturnEmptyList() throws Exception {
        List<CarPlaceDto> testCarPlaceDtoList = Collections.emptyList();
        when(carPlaceService.findAvailableCarPlaces()).thenReturn(testCarPlaceDtoList);
        mockMvc.perform(get("/api/car-places/available"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getAvailableCarPlacesCount_ShouldReturnCount() throws Exception {
        long comparedCount = 5;
        long testCount = 5;
        when(carPlaceService.countAvailableCarPlaces()).thenReturn(testCount);

        MvcResult mvcResult = mockMvc.perform(get("/api/car-places/available/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        long receivedCount = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(comparedCount, receivedCount);
    }

    @Test
    public void getCarPlacesByCarLift_ShouldReturnPlacesWithLift() throws Exception {
        boolean carLiftFactor = true;
        List<CarPlaceDto> comparedCarPlaceDtoList = List.of(
                new CarPlaceDto(1, 25, true, false),
                new CarPlaceDto(2, 22, true, true)
        );

        List<CarPlaceDto> testCarPlaces = List.of(
                new CarPlaceDto(1, 25, true, false),
                new CarPlaceDto(2, 22, true, true)
        );

        when(carPlaceService.findByCarLift(carLiftFactor)).thenReturn(testCarPlaces);

        MvcResult mvcResult = mockMvc.perform(get("/api/car-places/carlift")
                        .param("hasLift", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<CarPlaceDto> receivedPlaces = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){}
        );

        assertEquals(comparedCarPlaceDtoList, receivedPlaces);
    }

    @Test
    public void getCarPlacesByCarLift_ShouldReturnEmptyListWhenNoMatches() throws Exception {
        boolean carLiftFactor = true;

        List<CarPlaceDto> comparedCarPlaceDtoList = Collections.emptyList();
        List<CarPlaceDto> testCarPlaces = Collections.emptyList();

        when(carPlaceService.findByCarLift(carLiftFactor)).thenReturn(testCarPlaces);

        MvcResult mvcResult = mockMvc.perform(get("/api/car-places/carlift")
                        .param("hasLift", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<CarPlaceDto> receivedCarPlaces = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){}
        );

        assertEquals(comparedCarPlaceDtoList, receivedCarPlaces);
    }

    @Test
    public void createCarPlace_ShouldReturnCreatedCarPlace() throws Exception {
        int generatedId = 10;

        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(15);
        sentCarPlaceDto.setCarLift(false);
        sentCarPlaceDto.setIsOccupied(false);

        CarPlaceDto comparedCarPlaceDto = new CarPlaceDto(
                generatedId, 15, false, false
        );

        CarPlaceDto testCarPlaceDto = new CarPlaceDto(
                generatedId, 15, false, false
        );

        when(carPlaceService.create(sentCarPlaceDto)).thenReturn(testCarPlaceDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/car-places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sentCarPlaceDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CarPlaceDto receivedDto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){}
        );

        assertEquals(comparedCarPlaceDto, receivedDto);
    }

    
    @Test
    public void createCarPlace_WithInvalidSquare_ShouldThrowServiceException() {
        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(-5);
        sentCarPlaceDto.setCarLift(false);
        sentCarPlaceDto.setIsOccupied(false);

        when(carPlaceService.create(sentCarPlaceDto))
                .thenThrow(new ServiceException("Square must be positive"));

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> carPlaceController.createCarPlace(sentCarPlaceDto)
        );

        assertEquals("Square must be positive", thrownException.getMessage());
        verify(carPlaceService, times(1)).create(any(CarPlaceDto.class));
    }


    @Test
    public void updateCarPlace_ShouldReturnUpdatedCarPlace() throws Exception {
        int id = 10;
        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(25);
        sentCarPlaceDto.setCarLift(false);
        sentCarPlaceDto.setIsOccupied(false);

        CarPlaceDto comparedCarPlaceDto = new CarPlaceDto(
                id, 25, false, false
        );

        CarPlaceDto testCarPlaceDto = new CarPlaceDto(
                id, 25, false, false
        );

        when(carPlaceService.update(id, sentCarPlaceDto))
                .thenReturn(testCarPlaceDto);

        MvcResult mvcResult = mockMvc.perform(put("/api/car-places/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sentCarPlaceDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CarPlaceDto receivedDto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){}
        );

        assertEquals(comparedCarPlaceDto, receivedDto);
    }

    
    @Test
    public void updateCarPlace_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;

        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(25);
        sentCarPlaceDto.setCarLift(false);
        sentCarPlaceDto.setIsOccupied(false);

        when(carPlaceService.update(nonExistingId, sentCarPlaceDto)).thenThrow(
                new ServiceException("Car place not found with id: " + nonExistingId)
        );

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> carPlaceController.updateCarPlace(nonExistingId, sentCarPlaceDto)
        );

        assertEquals("Car place not found with id: " +
                nonExistingId, thrownException.getMessage());
        verify(carPlaceService, times(1))
                .update(nonExistingId, sentCarPlaceDto);
    }


    @Test
    public void updateOccupation_ShouldReturnOk() throws Exception {
        int id = 1;
        mockMvc.perform(patch("/api/car-places/{id}/occupation", id)
                        .param("occupied", "true"))
                .andExpect(status().isOk());
    }

    
    @Test
    public void updateOccupation_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        boolean occupied = true;

        doThrow(new ServiceException("Car place not found with id: " + nonExistingId))
                .when(carPlaceService).updateOccupation(nonExistingId, occupied);

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> carPlaceController.updateOccupation(nonExistingId, occupied)
        );

        assertEquals("Car place not found with id: " + nonExistingId, thrownException.getMessage());
        verify(carPlaceService, times(1)).updateOccupation(nonExistingId, occupied);
    }

    @Test
    public void deleteCarPlace_ShouldReturnNoContent() throws Exception {
        int id = 1;

        mockMvc.perform(delete("/api/car-places/{id}", id))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void deleteCarPlace_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;

        doThrow(new ServiceException("Car place not found with id: " + nonExistingId))
                .when(carPlaceService).delete(nonExistingId);

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> carPlaceController.deleteCarPlace(nonExistingId)
        );

        assertEquals("Car place not found with id: " + nonExistingId, thrownException.getMessage());
        verify(carPlaceService, times(1)).delete(nonExistingId);
    }
}