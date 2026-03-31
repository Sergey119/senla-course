package com.example.task.service;

import com.example.task.dao.CustomerDao;
import com.example.task.dto.CustomerDto;
import com.example.task.exception.ServiceException;
import com.example.task.model.Customer;
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
public class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void findAll_ShouldReturnAllCustomers() {
        List<Customer> daoCustomers = List.of(
                new Customer(1, "John Doe"),
                new Customer(2, "Jane Smith")
        );
        List<CustomerDto> expectedDtos = Arrays.asList(
                new CustomerDto(1, "John Doe"),
                new CustomerDto(2, "Jane Smith")
        );

        when(customerDao.findAll()).thenReturn(daoCustomers);

        List<CustomerDto> actualDtos = customerService.findAll();

        expectedDtos.sort(Comparator.comparing(CustomerDto::getId));
        actualDtos.sort(Comparator.comparing(CustomerDto::getId));

        assertEquals(expectedDtos, actualDtos);
    }

    @Test
    public void findAll_ShouldReturnEmptyList() {
        List<Customer> daoCustomers = Collections.emptyList();
        List<CustomerDto> expectedDtos = Collections.emptyList();

        when(customerDao.findAll()).thenReturn(daoCustomers);

        List<CustomerDto> actualDtos = customerService.findAll();

        assertEquals(expectedDtos, actualDtos);
    }

    @Test
    public void findById_ShouldReturnCustomer() throws ServiceException {
        int id = 1;
        Customer daoCustomer = new Customer(id, "John Doe");
        CustomerDto expectedDto = new CustomerDto(id, "John Doe");

        when(customerDao.findById(id)).thenReturn(daoCustomer);

        CustomerDto actualDto = customerService.findById(id);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    public void findById_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        when(customerDao.findById(nonExistingId)).thenReturn(null);

        assertThrows(ServiceException.class, () -> customerService.findById(nonExistingId));
    }

    @Test
    public void findAllSortedByName_ShouldReturnSortedCustomers() {
        List<Customer> daoCustomers = List.of(
                new Customer(2, "Zoe"),
                new Customer(1, "Alice")
        );
        List<CustomerDto> expectedDtos = Arrays.asList(
                new CustomerDto(1, "Alice"),
                new CustomerDto(2, "Zoe")
        );

        when(customerDao.findAllSortedByName()).thenReturn(daoCustomers);

        List<CustomerDto> actualDtos = customerService.findAllSortedByName();

        expectedDtos.sort(Comparator.comparing(CustomerDto::getId));
        actualDtos.sort(Comparator.comparing(CustomerDto::getId));

        assertEquals(expectedDtos, actualDtos);
    }

    @Test
    public void countAll_ShouldReturnCount() {
        long expectedCount = 5;
        when(customerDao.countAll()).thenReturn(expectedCount);

        long actualCount = customerService.countAll();

        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void existsByName_WithName_ShouldReturnTrue() {
        String name = "John Doe";
        boolean expected = true;
        when(customerDao.existsByName(name)).thenReturn(expected);

        boolean actual = customerService.existsByName(name);

        assertEquals(expected, actual);
    }

    @Test
    public void existsByName_WithNonExistingName_ShouldReturnFalse() {
        String name = "Non Existing";
        boolean expected = false;
        when(customerDao.existsByName(name)).thenReturn(expected);

        boolean actual = customerService.existsByName(name);

        assertEquals(expected, actual);
    }

    @Test
    public void create_WithValidData_ShouldReturnCreatedCustomer() throws ServiceException {
        CustomerDto inputDto = new CustomerDto();
        inputDto.setName("New Customer");

        Customer savedCustomer = new Customer(1, "New Customer");
        CustomerDto expectedDto = new CustomerDto(1, "New Customer");

        when(customerDao.existsByName("New Customer")).thenReturn(false);
        when(customerDao.save(any(Customer.class))).thenReturn(1);
        when(customerDao.findById(1)).thenReturn(savedCustomer);

        CustomerDto actualDto = customerService.create(inputDto);

        assertEquals(expectedDto, actualDto);
        verify(customerDao, times(1)).save(any(Customer.class));
    }

    @Test
    public void create_WithNullName_ShouldThrowServiceException() {
        CustomerDto inputDto = new CustomerDto();
        inputDto.setName(null);

        assertThrows(ServiceException.class, () -> customerService.create(inputDto));
    }

    @Test
    public void create_WithEmptyName_ShouldThrowServiceException() {
        CustomerDto inputDto = new CustomerDto();
        inputDto.setName("");

        assertThrows(ServiceException.class, () -> customerService.create(inputDto));
    }

    @Test
    public void create_WithWhitespaceName_ShouldThrowServiceException() {
        CustomerDto inputDto = new CustomerDto();
        inputDto.setName("   ");

        assertThrows(ServiceException.class, () -> customerService.create(inputDto));
    }

    @Test
    public void create_WithExistingName_ShouldThrowServiceException() {
        CustomerDto inputDto = new CustomerDto();
        inputDto.setName("Existing Name");

        when(customerDao.existsByName("Existing Name")).thenReturn(true);

        assertThrows(ServiceException.class, () -> customerService.create(inputDto));
    }

    @Test
    public void update_WithValidData_ShouldReturnUpdatedCustomer() throws ServiceException {
        int id = 1;
        CustomerDto inputDto = new CustomerDto();
        inputDto.setName("Updated Name");

        Customer existingCustomer = new Customer(id, "Old Name");
        CustomerDto expectedDto = new CustomerDto(id, "Updated Name");

        when(customerDao.findById(id)).thenReturn(existingCustomer);
        when(customerDao.existsByName("Updated Name")).thenReturn(false);

        CustomerDto actualDto = customerService.update(id, inputDto);

        assertEquals(expectedDto, actualDto);
        assertEquals("Updated Name", existingCustomer.getName());
        verify(customerDao, times(1)).update(existingCustomer);
    }

    @Test
    public void update_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        CustomerDto inputDto = new CustomerDto();
        inputDto.setName("Updated Name");

        when(customerDao.findById(nonExistingId)).thenReturn(null);

        assertThrows(ServiceException.class,
                () -> customerService.update(nonExistingId, inputDto)
        );
    }

    @Test
    public void update_WithNewNameThatAlreadyExists_ShouldThrowServiceException() throws ServiceException {
        int id = 1;
        CustomerDto inputDto = new CustomerDto();
        inputDto.setName("Existing Name");

        Customer existingCustomer = new Customer(id, "Old Name");

        when(customerDao.findById(id)).thenReturn(existingCustomer);
        when(customerDao.existsByName("Existing Name")).thenReturn(true);

        assertThrows(ServiceException.class, () -> customerService.update(id, inputDto));
    }

    @Test
    public void updateName_WithValidData_ShouldUpdateSuccessfully() throws ServiceException {
        int id = 1;
        String newName = "New Name";
        Customer existingCustomer = new Customer(id, "Old Name");

        when(customerDao.findById(id)).thenReturn(existingCustomer);
        when(customerDao.existsByName(newName)).thenReturn(false);

        customerService.updateName(id, newName);

        verify(customerDao, times(1)).updateName(id, newName);
    }

    @Test
    public void updateName_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        String newName = "New Name";

        when(customerDao.findById(nonExistingId)).thenReturn(null);

        assertThrows(ServiceException.class,
                () -> customerService.updateName(nonExistingId, newName)
        );
    }

    @Test
    public void updateName_WithNullName_ShouldThrowServiceException() throws ServiceException {
        int id = 1;
        String newName = null;
        Customer existingCustomer = new Customer(id, "Old Name");

        when(customerDao.findById(id)).thenReturn(existingCustomer);

        assertThrows(ServiceException.class, () -> customerService.updateName(id, newName));
    }

    @Test
    public void updateName_WithEmptyName_ShouldThrowServiceException() throws ServiceException {
        int id = 1;
        String newName = "";
        Customer existingCustomer = new Customer(id, "Old Name");

        when(customerDao.findById(id)).thenReturn(existingCustomer);

        assertThrows(ServiceException.class, () -> customerService.updateName(id, newName));
    }

    @Test
    public void updateName_WithWhitespaceName_ShouldThrowServiceException() throws ServiceException {
        int id = 1;
        String newName = "   ";
        Customer existingCustomer = new Customer(id, "Old Name");

        when(customerDao.findById(id)).thenReturn(existingCustomer);

        assertThrows(ServiceException.class, () -> customerService.updateName(id, newName));
    }

    @Test
    public void updateName_WithExistingName_ShouldThrowServiceException() throws ServiceException {
        int id = 1;
        String newName = "Existing Name";
        Customer existingCustomer = new Customer(id, "Old Name");

        when(customerDao.findById(id)).thenReturn(existingCustomer);
        when(customerDao.existsByName(newName)).thenReturn(true);

        assertThrows(ServiceException.class, () -> customerService.updateName(id, newName));
    }

    @Test
    public void delete_WithExistingId_ShouldDeleteSuccessfully() throws ServiceException {
        int id = 1;

        when(customerDao.existsById(id)).thenReturn(true);

        customerService.delete(id);

        verify(customerDao, times(1)).delete(id);
    }

    @Test
    public void delete_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;

        when(customerDao.existsById(nonExistingId)).thenReturn(false);

        assertThrows(ServiceException.class, () -> customerService.delete(nonExistingId));
    }
}