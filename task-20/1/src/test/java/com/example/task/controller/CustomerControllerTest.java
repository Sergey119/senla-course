package com.example.task.controller;

import com.example.task.dto.CustomerDto;
import com.example.task.exception.ServiceException;
import com.example.task.service.CustomerService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void getAllCustomers_ShouldReturnAllCustomers() throws Exception {
        List<CustomerDto> expectedCustomers = Arrays.asList(
                new CustomerDto(1, "John Doe"),
                new CustomerDto(2, "Jane Smith")
        );

        when(customerService.findAll()).thenReturn(expectedCustomers);

        MvcResult mvcResult = mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<CustomerDto> actualCustomers = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(expectedCustomers, actualCustomers);
    }

    @Test
    public void getAllCustomers_ShouldReturnEmptyList() throws Exception {
        List<CustomerDto> expectedCustomers = Collections.emptyList();
        when(customerService.findAll()).thenReturn(expectedCustomers);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getCustomerById_ShouldReturnCustomer() throws Exception {
        int id = 1;
        CustomerDto expectedCustomer = new CustomerDto(id, "John Doe");

        when(customerService.findById(id)).thenReturn(expectedCustomer);

        MvcResult mvcResult = mockMvc.perform(get("/api/customers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CustomerDto actualCustomer = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(expectedCustomer, actualCustomer);
    }

    @Test
    public void getCustomerById_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        when(customerService.findById(nonExistingId)).thenThrow(
                new ServiceException("Customer not found with id: " + nonExistingId)
        );

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> customerController.getCustomerById(nonExistingId)
        );

        assertEquals("Customer not found with id: " +
                nonExistingId, thrownException.getMessage()
        );
        verify(customerService, times(1))
                .findById(nonExistingId);
    }

    @Test
    public void getCustomersSortedByName_ShouldReturnCustomersSortedByName() throws Exception {
        List<CustomerDto> expectedCustomers = Arrays.asList(
                new CustomerDto(2, "Alice Johnson"),
                new CustomerDto(1, "Bob Smith")
        );

        when(customerService.findAllSortedByName()).thenReturn(expectedCustomers);

        MvcResult mvcResult = mockMvc.perform(get("/api/customers/sorted/name"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<CustomerDto> actualCustomers = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(expectedCustomers, actualCustomers);
    }

    @Test
    public void getCustomersSortedByName_WithNoCustomers_ShouldReturnEmptyList() throws Exception {
        List<CustomerDto> expectedCustomers = Collections.emptyList();
        when(customerService.findAllSortedByName()).thenReturn(expectedCustomers);

        mockMvc.perform(get("/api/customers/sorted/name"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getCustomersCount_ShouldReturnCount() throws Exception {
        long expectedCount = 3;
        when(customerService.countAll()).thenReturn(expectedCount);

        MvcResult mvcResult = mockMvc.perform(get("/api/customers/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        long actualCount = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void checkCustomerExists_WithName_ShouldReturnTrue() throws Exception {
        String name = "John Doe";
        boolean expectedExists = true;
        when(customerService.existsByName(name)).thenReturn(expectedExists);

        MvcResult mvcResult = mockMvc.perform(get("/api/customers/exists")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        boolean actualExists = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});
        assertEquals(expectedExists, actualExists);
    }

    @Test
    public void checkCustomerExists_WithNonExistingName_ShouldReturnFalse() throws Exception {
        String name = "Non Existing Name";
        boolean expectedExists = false;
        when(customerService.existsByName(name)).thenReturn(expectedExists);

        MvcResult mvcResult = mockMvc.perform(get("/api/customers/exists")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        boolean actualExists = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});
        assertEquals(expectedExists, actualExists);
    }

    @Test
    public void createCustomer_ShouldReturnCreatedCustomer() throws Exception {
        CustomerDto inputDto = new CustomerDto();
        inputDto.setName("New Customer");

        CustomerDto expectedDto = new CustomerDto(1, "New Customer");
        when(customerService.create(any(CustomerDto.class))).thenReturn(expectedDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CustomerDto actualDto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});
        assertEquals(expectedDto, actualDto);
    }

    @Test
    public void createCustomer_WithInvalidData_ShouldThrowServiceException() {
        CustomerDto inputDto = new CustomerDto();
        inputDto.setName("");

        when(customerService.create(any(CustomerDto.class))).thenThrow(
                new ServiceException("Customer name cannot be empty")
        );

        ServiceException thrownException = assertThrows(
                ServiceException.class,
                () -> customerController.createCustomer(inputDto)
        );

        assertEquals("Customer name cannot be empty",
                thrownException.getMessage());
        verify(customerService, times(1))
                .create(any(CustomerDto.class));
    }

    @Test
    public void updateCustomer_ShouldReturnUpdatedCustomer() throws Exception {
        int id = 1;
        CustomerDto inputDto = new CustomerDto();
        inputDto.setName("Updated Name");

        CustomerDto expectedDto = new CustomerDto(id, "Updated Name");
        when(customerService.update(id, inputDto)).thenReturn(expectedDto);

        MvcResult mvcResult = mockMvc.perform(put("/api/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        CustomerDto actualDto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});
        assertEquals(expectedDto, actualDto);
    }

    @Test
    public void updateCustomer_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;

        CustomerDto inputDto = new CustomerDto();
        inputDto.setName("Updated Name");

        when(customerService.update(nonExistingId, inputDto)).thenThrow(
                new ServiceException("Customer not found with id: " + nonExistingId)
        );

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> customerController.updateCustomer(nonExistingId, inputDto)
        );

        assertEquals("Customer not found with id: " +
                nonExistingId, thrownException.getMessage());
        verify(customerService, times(1))
                .update(nonExistingId, inputDto);
    }

    @Test
    public void updateCustomerName_ShouldReturnOk() throws Exception {
        int id = 1;
        String newName = "New Name";

        doNothing().when(customerService).updateName(id, newName);

        mockMvc.perform(patch("/api/customers/{id}/name", id)
                        .param("newName", newName))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCustomerName_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        String newName = "New Name";

        doThrow(new ServiceException("Customer not found with id: " + nonExistingId))
                .when(customerService).updateName(nonExistingId, newName);

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> customerController.updateCustomerName(nonExistingId, newName)
        );

        assertEquals("Customer not found with id: " +
                nonExistingId, thrownException.getMessage());
        verify(customerService, times(1))
                .updateName(nonExistingId, newName);
    }

    @Test
    public void deleteCustomer_ShouldReturnNoContent() throws Exception {
        int id = 1;

        doNothing().when(customerService).delete(id);

        mockMvc.perform(delete("/api/customers/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteCustomer_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;

        doThrow(new ServiceException("Customer not found with id: " + nonExistingId))
                .when(customerService).delete(nonExistingId);

        ServiceException thrownException = assertThrows(
                ServiceException.class,
                () -> customerController.deleteCustomer(nonExistingId)
        );

        assertEquals("Customer not found with id: " +
                nonExistingId, thrownException.getMessage());
        verify(customerService, times(1))
                .delete(nonExistingId);
    }
}