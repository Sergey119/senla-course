package com.example.task.service;

import com.example.task.dao.CarPlaceDao;
import com.example.task.dto.CarPlaceDto;
import com.example.task.exception.ServiceException;
import com.example.task.model.CarPlace;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarPlaceServiceTest {

    @Mock
    private CarPlaceDao carPlaceDao;
    @InjectMocks
    private CarPlaceService carPlaceService;

    @Test
    public void findAll_ShouldReturnAllCarPlaces() {
        List<CarPlaceDto> comparedCarPlaceDtoList = Arrays.asList(
                new CarPlaceDto(1, 25, true, true),
                new CarPlaceDto(2, 22, false, true),
                new CarPlaceDto(3, 28, true, false)
        );

        List<CarPlace> testCarPlaceList = List.of(
                new CarPlace(1, 25,true,true),
                new CarPlace(2, 22,false,true),
                new CarPlace(3, 28,true,false)
        );
        
        when(carPlaceDao.findAll()).thenReturn(testCarPlaceList);

        List<CarPlaceDto> carPlaceDtoResult = carPlaceService.findAll();

        comparedCarPlaceDtoList.sort(Comparator.comparing(CarPlaceDto::getId));
        carPlaceDtoResult.sort(Comparator.comparing(CarPlaceDto::getId));

        assertEquals(comparedCarPlaceDtoList, carPlaceDtoResult);
    }

    @Test
    public void findAll_ShouldReturnEmptyList() {
        List<CarPlaceDto> comparedCarPlaceDtoList = Collections.emptyList();

        List<CarPlace> testCarPlaceList = Collections.emptyList();
        when(carPlaceDao.findAll()).thenReturn(testCarPlaceList);

        List<CarPlaceDto> carPlaceDtoResult = carPlaceService.findAll();

        assertEquals(comparedCarPlaceDtoList, carPlaceDtoResult);
    }

    @Test
    public void findById_ShouldReturnCarPlace() throws ServiceException {
        CarPlaceDto comparedCarPlaceDto = new CarPlaceDto(
                1, 25, true, false
        );

        CarPlace testCarPlace = new CarPlace(
                1, 25, true, false
        );
        when(carPlaceDao.findById(testCarPlace.getId())).thenReturn(testCarPlace);

        CarPlaceDto carPlaceDtoResult = carPlaceService.findById(1);

        assertEquals(comparedCarPlaceDto, carPlaceDtoResult);
    }

    @Test
    public void findById_WithNonExistingId_ShouldThrowServiceException() throws ServiceException {
        int nonExistingId = 999;
        when(carPlaceDao.findById(nonExistingId)).thenReturn(null);
        assertThrows(ServiceException.class, () -> carPlaceService.findById(nonExistingId));
    }

    @Test
    public void findAvailableCarPlaces_ShouldReturnAvailableCarPlaces() {
        List<CarPlaceDto> comparedAvailableCarPlaceDtoList = Arrays.asList(
                new CarPlaceDto(1, 25, true, false),
                new CarPlaceDto(2, 22, false, false),
                new CarPlaceDto(3, 28, true, false)
        );

        List<CarPlace> testAvailableCarPlaceList = List.of(
                new CarPlace(1, 25,true,false),
                new CarPlace(2, 22,false,false),
                new CarPlace(3, 28,true,false)
        );
        when(carPlaceDao.findAvailableCarPlaces()).thenReturn(testAvailableCarPlaceList);

        List<CarPlaceDto> carPlaceDtoResult = carPlaceService.findAvailableCarPlaces();

        comparedAvailableCarPlaceDtoList.sort(Comparator.comparing(CarPlaceDto::getId));
        carPlaceDtoResult.sort(Comparator.comparing(CarPlaceDto::getId));

        assertEquals(comparedAvailableCarPlaceDtoList, carPlaceDtoResult);
    }

    @Test
    public void findAvailableCarPlaces_ShouldReturnEmptyList() {
        List<CarPlaceDto> comparedAvailableCarPlaceDtoList = Collections.emptyList();

        List<CarPlace> testAvailableCarPlaceList = Collections.emptyList();
        when(carPlaceDao.findAvailableCarPlaces()).thenReturn(testAvailableCarPlaceList);

        List<CarPlaceDto> carPlaceDtoResult = carPlaceService.findAvailableCarPlaces();

        assertEquals(comparedAvailableCarPlaceDtoList, carPlaceDtoResult);
    }

    @Test
    public void countAvailableCarPlaces_ShouldReturnAvailableCarPlaces() {
        long comparedAvailableCarPlaces = 2;
        long testAvailableCarPlaces = 2;
        when(carPlaceDao.countAvailableCarPlaces()).thenReturn(testAvailableCarPlaces);

        assertEquals(comparedAvailableCarPlaces, carPlaceService.countAvailableCarPlaces());
    }

    @Test
    public void countAvailableCarPlaces_ShouldReturnZero() {
        long comparedAvailableCarPlaces = 0;
        long testAvailableCarPlaces = 0;
        when(carPlaceDao.countAvailableCarPlaces()).thenReturn(testAvailableCarPlaces);

        assertEquals(comparedAvailableCarPlaces, carPlaceService.countAvailableCarPlaces());
    }

    @Test
    public void findByCarLift_ShouldReturnCarPlacesWithCarLift() {
        boolean carLiftFactor = true;

        List<CarPlaceDto> comparedCarPlaceDtoList = Arrays.asList(
                new CarPlaceDto(1, 25, true, true),
                new CarPlaceDto(3, 28, true, false)
        );

        List<CarPlace> testCarPlaceList = List.of(
                new CarPlace(1, 25,true,true),
                new CarPlace(3, 28,true,false)
        );
        when(carPlaceDao.findByCarLift(carLiftFactor)).thenReturn(testCarPlaceList);

        List<CarPlaceDto> carPlaceDtoResult = carPlaceService.findByCarLift(carLiftFactor);

        assertEquals(comparedCarPlaceDtoList, carPlaceDtoResult);
    }

    @Test
    public void findByCarLift_ShouldReturnCarPlacesWithoutCarLift() {
        boolean carLiftFactor = false;

        List<CarPlaceDto> comparedCarPlaceDtoList = Arrays.asList(
                new CarPlaceDto(2, 22, carLiftFactor, true),
                new CarPlaceDto(4, 30, carLiftFactor, false)
        );

        List<CarPlace> testCarPlaceList = List.of(
                new CarPlace(2, 22, carLiftFactor, true),
                new CarPlace(4, 30, carLiftFactor, false)
        );
        when(carPlaceDao.findByCarLift(carLiftFactor)).thenReturn(testCarPlaceList);

        List<CarPlaceDto> carPlaceDtoResult = carPlaceService.findByCarLift(carLiftFactor);

        assertEquals(comparedCarPlaceDtoList, carPlaceDtoResult);
    }


    @Test
    void create_WithValidDto_ShouldReturnCreatedCarPlaceDto() throws ServiceException {
        int generatedId = 10;

        CarPlace testCarPlace = new CarPlace(
                generatedId, 15, false, false
        );

        CarPlaceDto comparedCarPlaceDto = new CarPlaceDto(
                generatedId, 15, false, false
        );

        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(15);
        sentCarPlaceDto.setCarLift(false);
        sentCarPlaceDto.setIsOccupied(false);

        when(carPlaceDao.save(any(CarPlace.class))).thenReturn(generatedId);
        when(carPlaceDao.findById(generatedId)).thenReturn(testCarPlace);

        CarPlaceDto result = carPlaceService.create(sentCarPlaceDto);

        assertEquals(comparedCarPlaceDto, result);
        verify(carPlaceDao, times(1)).save(any(CarPlace.class));
    }

    @Test
    void create_WithNullSquare_ShouldThrowServiceException() {
        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(null);
        assertThrows(ServiceException.class, () -> carPlaceService.create(sentCarPlaceDto));
    }

    @Test
    void create_WithZeroSquare_ShouldThrowServiceException() {
        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(0);
        assertThrows(ServiceException.class, () -> carPlaceService.create(sentCarPlaceDto));
    }

    @Test
    void create_WithNegativeSquare_ShouldThrowServiceException() {
        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(-5);
        assertThrows(ServiceException.class, () -> carPlaceService.create(sentCarPlaceDto));
    }

    @Test
    void create_WithNullCarLift_ShouldSetDefaultFalse() throws ServiceException {
        int generatedId = 10;

        CarPlace testCarPlace = new CarPlace(
                generatedId, 15, false, false
        );

        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(15);
        sentCarPlaceDto.setCarLift(null);
        sentCarPlaceDto.setIsOccupied(false);

        when(carPlaceDao.save(any(CarPlace.class))).thenReturn(generatedId);
        when(carPlaceDao.findById(generatedId)).thenReturn(testCarPlace);

        CarPlaceDto result = carPlaceService.create(sentCarPlaceDto);

        assertFalse(result.getCarLift());
    }

    @Test
    void create_WithNullIsOccupied_ShouldSetDefaultFalse() throws ServiceException {
        int generatedId = 10;

        CarPlace testCarPlace = new CarPlace(
                generatedId, 15, false, false
        );

        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(15);
        sentCarPlaceDto.setCarLift(false);
        sentCarPlaceDto.setIsOccupied(null);

        when(carPlaceDao.save(any(CarPlace.class))).thenReturn(generatedId);
        when(carPlaceDao.findById(generatedId)).thenReturn(testCarPlace);

        CarPlaceDto result = carPlaceService.create(sentCarPlaceDto);

        assertFalse(result.getIsOccupied());
    }

    @Test
    void update_WithValidIdAndDto_ShouldReturnUpdatedCarPlaceDto() throws ServiceException {
        int id = 10;

        CarPlaceDto comparedCarPlaceDto = new CarPlaceDto(
                id, 15, false, false
        );

        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(15);
        sentCarPlaceDto.setCarLift(false);
        sentCarPlaceDto.setIsOccupied(false);

        CarPlace testCarPlace = new CarPlace(id, 15, false, false);

        when(carPlaceDao.findById(id)).thenReturn(testCarPlace);

        CarPlaceDto result = carPlaceService.update(id, sentCarPlaceDto);

        assertEquals(comparedCarPlaceDto, result);
        verify(carPlaceDao, times(1)).update(testCarPlace);
    }

    @Test
    void update_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;

        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(15);
        sentCarPlaceDto.setCarLift(false);
        sentCarPlaceDto.setIsOccupied(false);

        when(carPlaceDao.findById(nonExistingId)).thenReturn(null);

        assertThrows(ServiceException.class, () -> carPlaceService.update(nonExistingId, sentCarPlaceDto));
    }

    @Test
    void update_WithPositiveSquare_ShouldUpdateSquare() throws ServiceException {
        int id = 10;

        CarPlaceDto comparedCarPlaceDto = new CarPlaceDto(
                id, 15, false, false
        );

        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(15);
        sentCarPlaceDto.setCarLift(null);
        sentCarPlaceDto.setIsOccupied(null);

        CarPlace testCarPlace = new CarPlace(
                id, 15, false, false
        );

        when(carPlaceDao.findById(id)).thenReturn(testCarPlace);

        CarPlaceDto result = carPlaceService.update(id, sentCarPlaceDto);

        assertEquals(comparedCarPlaceDto, result);
    }

    @Test
    void update_WithNullSquare_ShouldNotUpdateSquare() throws ServiceException {
        int id = 10;

        CarPlaceDto comparedCarPlaceDto = new CarPlaceDto(
                id, 15, false, false
        );

        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(null);
        sentCarPlaceDto.setCarLift(false);
        sentCarPlaceDto.setIsOccupied(false);

        CarPlace testCarPlace = new CarPlace(
                id, 15, true, true
        );

        when(carPlaceDao.findById(id)).thenReturn(testCarPlace);

        CarPlaceDto result = carPlaceService.update(id, sentCarPlaceDto);

        assertEquals(comparedCarPlaceDto, result);
    }

    @Test
    void update_WithZeroSquare_ShouldNotUpdateSquare() throws ServiceException {
        int id = 10;

        CarPlaceDto comparedCarPlaceDto = new CarPlaceDto(
                id, 15, false, false
        );

        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(0);
        sentCarPlaceDto.setCarLift(false);
        sentCarPlaceDto.setIsOccupied(false);

        CarPlace testCarPlace = new CarPlace(
                id, 15, true, true
        );

        when(carPlaceDao.findById(id)).thenReturn(testCarPlace);

        CarPlaceDto result = carPlaceService.update(id, sentCarPlaceDto);

        assertEquals(comparedCarPlaceDto, result);
    }

    @Test
    void update_WithNegativeSquare_ShouldNotUpdateSquare() throws ServiceException {
        int id = 10;

        CarPlaceDto comparedCarPlaceDto = new CarPlaceDto(
                id, 15, false, false
        );

        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(-5);
        sentCarPlaceDto.setCarLift(false);
        sentCarPlaceDto.setIsOccupied(false);

        CarPlace testCarPlace = new CarPlace(
                id, 15, true, true
        );

        when(carPlaceDao.findById(id)).thenReturn(testCarPlace);

        CarPlaceDto result = carPlaceService.update(id, sentCarPlaceDto);

        assertEquals(comparedCarPlaceDto, result);
    }

    @Test
    void update_WithSignificantCarLift_ShouldUpdateCarLift() throws ServiceException {
        int id = 10;

        CarPlaceDto comparedCarPlaceDto = new CarPlaceDto(
                id, 15, true, false
        );

        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(null);
        sentCarPlaceDto.setCarLift(true);
        sentCarPlaceDto.setIsOccupied(null);

        CarPlace testCarPlace = new CarPlace(
                id, 15, true, false
        );

        when(carPlaceDao.findById(id)).thenReturn(testCarPlace);

        CarPlaceDto result = carPlaceService.update(id, sentCarPlaceDto);

        assertEquals(comparedCarPlaceDto, result);
    }

    @Test
    void update_WithNullCarLift_ShouldNotUpdateCarLift() throws ServiceException {
        int id = 10;

        CarPlaceDto comparedCarPlaceDto = new CarPlaceDto(
                id, 15, true, false
        );

        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(15);
        sentCarPlaceDto.setCarLift(null);
        sentCarPlaceDto.setIsOccupied(false);

        CarPlace testCarPlace = new CarPlace(
                id, 15, true, false
        );

        when(carPlaceDao.findById(id)).thenReturn(testCarPlace);

        CarPlaceDto result = carPlaceService.update(id, sentCarPlaceDto);

        assertEquals(comparedCarPlaceDto, result);
    }

    @Test
    void update_WithSignificantIsOccupied_ShouldUpdateIsOccupied() throws ServiceException {
        int id = 10;

        CarPlaceDto comparedCarPlaceDto = new CarPlaceDto(
                id, 15, true, false
        );

        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(null);
        sentCarPlaceDto.setCarLift(null);
        sentCarPlaceDto.setIsOccupied(false);

        CarPlace testCarPlace = new CarPlace(
                id, 15, true, false
        );

        when(carPlaceDao.findById(id)).thenReturn(testCarPlace);

        CarPlaceDto result = carPlaceService.update(id, sentCarPlaceDto);

        assertEquals(comparedCarPlaceDto, result);
    }

    @Test
    void update_WithNullIsOccupied_ShouldNotUpdateIsOccupied() throws ServiceException {
        int id = 10;

        CarPlaceDto comparedCarPlaceDto = new CarPlaceDto(
                id, 15, true, false
        );

        CarPlaceDto sentCarPlaceDto = new CarPlaceDto();
        sentCarPlaceDto.setSquare(15);
        sentCarPlaceDto.setCarLift(true);
        sentCarPlaceDto.setIsOccupied(null);

        CarPlace testCarPlace = new CarPlace(
                id, 15, true, false
        );

        when(carPlaceDao.findById(id)).thenReturn(testCarPlace);

        CarPlaceDto result = carPlaceService.update(id, sentCarPlaceDto);

        assertEquals(comparedCarPlaceDto, result);
    }

    @Test
    void updateOccupation_WithExistingId_ShouldUpdateSuccessfully() throws ServiceException {
        int id = 10;
        boolean occupationFactor = true;

        CarPlace testCarPlace = new CarPlace(
                id, 15, true, false
        );

        when(carPlaceDao.findById(id)).thenReturn(testCarPlace);

        carPlaceService.updateOccupation(id, occupationFactor);

        verify(carPlaceDao, times(1))
                .updateOccupation(id, occupationFactor);
    }

    @Test
    void updateOccupation_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        boolean occupationFactor = true;

        when(carPlaceDao.findById(nonExistingId)).thenReturn(null);

        assertThrows(ServiceException.class,
                () -> carPlaceService.updateOccupation(nonExistingId, occupationFactor)
        );
    }

    @Test
    void updateOccupation_WithNullIsOccupied_ShouldThrowServiceException() throws ServiceException {
        int id = 1;
        Boolean occupationFactor = null;

        CarPlace testCarPlace = new CarPlace(
                id, 15, false, false
        );

        when(carPlaceDao.findById(id)).thenReturn(testCarPlace);

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> carPlaceService.updateOccupation(id, occupationFactor)
        );
        assertEquals("Occupation status cannot be null", thrownException.getMessage());

        verify(carPlaceDao, never()).updateOccupation(eq(id), anyBoolean());
    }

    @Test
    void delete_WithExistingId_ShouldDeleteSuccessfully() throws ServiceException {
        int id = 10;
        when(carPlaceDao.existsById(id)).thenReturn(true);

        carPlaceService.delete(id);

        verify(carPlaceDao, times(1)).delete(id);
    }

    @Test
    void delete_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        when(carPlaceDao.existsById(nonExistingId)).thenReturn(false);
        assertThrows(ServiceException.class, () -> carPlaceService.delete(nonExistingId));
    }
}
