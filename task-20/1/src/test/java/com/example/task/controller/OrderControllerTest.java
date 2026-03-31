package com.example.task.controller;

import com.example.task.dto.OrderDto;
import com.example.task.exception.ServiceException;
import com.example.task.model.OrderStatus;
import com.example.task.service.OrderService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void createOrder_ShouldReturnCreatedOrder() throws Exception {
        LocalDateTime fixedStart = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusDays(1);
        LocalDateTime fixedEnd = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusDays(2);
        LocalDateTime fixedLoad = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusDays(1).plusHours(12);

        OrderDto sentOrderDto = new OrderDto();
        sentOrderDto.setServiceAdvisorId(1);
        sentOrderDto.setCustomerId(2);
        sentOrderDto.setCarPlaceId(3);
        sentOrderDto.setStartDate(fixedStart);
        sentOrderDto.setEndDate(fixedEnd);
        sentOrderDto.setLoadingDate(fixedLoad);

        OrderDto comparedOrderDto = new OrderDto();
        comparedOrderDto.setId(10);
        comparedOrderDto.setServiceAdvisorId(1);
        comparedOrderDto.setCustomerId(2);
        comparedOrderDto.setCarPlaceId(3);
        comparedOrderDto.setStartDate(fixedStart);
        comparedOrderDto.setEndDate(fixedEnd);
        comparedOrderDto.setLoadingDate(fixedLoad);
        comparedOrderDto.setStatus(OrderStatus.PENDING);

        when(orderService.createOrder(any(OrderDto.class))).thenReturn(comparedOrderDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sentOrderDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        OrderDto receivedDto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), OrderDto.class);

        assertEquals(comparedOrderDto.getId(), receivedDto.getId());
        assertEquals(comparedOrderDto.getServiceAdvisorId(), receivedDto.getServiceAdvisorId());
        assertEquals(comparedOrderDto.getCustomerId(), receivedDto.getCustomerId());
        assertEquals(comparedOrderDto.getCarPlaceId(), receivedDto.getCarPlaceId());
        assertEquals(comparedOrderDto.getStatus(), receivedDto.getStatus());

        assertEquals(comparedOrderDto.getStartDate().truncatedTo(ChronoUnit.SECONDS),
                receivedDto.getStartDate().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(comparedOrderDto.getEndDate().truncatedTo(ChronoUnit.SECONDS),
                receivedDto.getEndDate().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(comparedOrderDto.getLoadingDate().truncatedTo(ChronoUnit.SECONDS),
                receivedDto.getLoadingDate().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    public void createOrder_WithInvalidData_ShouldThrowServiceException() {
        OrderDto sentOrderDto = new OrderDto();

        when(orderService.createOrder(any(OrderDto.class))).thenThrow(
                new ServiceException("Service advisor not found with id: null")
        );

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> orderController.createOrder(sentOrderDto)
        );

        assertEquals("Service advisor not found with id: null",
                thrownException.getMessage());
        verify(orderService, times(1))
                .createOrder(any(OrderDto.class));
    }

    @Test
    public void getAllOrders_ShouldReturnAllOrders() throws Exception {
        List<OrderDto> comparedOrderDtoList = Arrays.asList(
                new OrderDto(1, null, "Advisor A",
                        10, "Customer X", 100, 20,
                        null, null, null, null),
                new OrderDto(2, null, "Advisor B",
                        11, "Customer Y", 101, 25,
                        null, null, null, null)
        );

        when(orderService.findAll()).thenReturn(comparedOrderDtoList);

        MvcResult mvcResult = mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<OrderDto> receivedOrders = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, OrderDto.class)
        );

        assertEquals(comparedOrderDtoList, receivedOrders);
    }

    @Test
    public void getAllOrders_ShouldReturnEmptyList() throws Exception {
        List<OrderDto> comparedOrderDtoList = Collections.emptyList();
        when(orderService.findAll()).thenReturn(comparedOrderDtoList);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getOrderById_ShouldReturnOrder() throws Exception {
        int id = 1;
        OrderDto comparedOrderDto = new OrderDto();
        comparedOrderDto.setId(id);
        comparedOrderDto.setServiceAdvisorId(1);
        comparedOrderDto.setCustomerId(2);
        comparedOrderDto.setStatus(OrderStatus.PENDING);

        when(orderService.findById(id)).thenReturn(comparedOrderDto);

        MvcResult mvcResult = mockMvc.perform(get("/api/orders/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        OrderDto receivedOrder = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), OrderDto.class);

        assertEquals(comparedOrderDto, receivedOrder);
    }

    @Test
    public void getOrderById_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        when(orderService.findById(nonExistingId)).thenThrow(
                new ServiceException("Order not found with id: " + nonExistingId)
        );

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> orderController.getOrderById(nonExistingId)
        );

        assertEquals("Order not found with id: " +
                nonExistingId, thrownException.getMessage());
        verify(orderService, times(1))
                .findById(nonExistingId);
    }

    @Test
    public void getOrdersByStatus_ShouldReturnOrders() throws Exception {
        String statusStr = "PENDING";
        OrderStatus statusEnum = OrderStatus.PENDING;
        List<OrderDto> comparedOrderDtoList = List.of(
                new OrderDto(1, null, "Advisor A",
                        10, "Customer X", 100, 20,
                        null, null, null, null)
        );

        when(orderService.findByStatus(statusEnum)).thenReturn(comparedOrderDtoList);

        MvcResult mvcResult = mockMvc.perform(get("/api/orders/status/{status}", statusStr))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<OrderDto> receivedOrders = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(comparedOrderDtoList, receivedOrders);
    }

    @Test
    public void getOrdersByStatus_WithInvalidStatus_ShouldThrowServiceException() {
        String invalidStatus = "INVALID_STATUS";
        ServiceException thrownServiceException = assertThrows(ServiceException.class,
                () -> orderController.getOrdersByStatus(invalidStatus)
        );
        assertEquals("Invalid status value. Allowed values: " +
                        "PENDING, IN_PROGRESS, COMPLETED, CANCELLED",
                thrownServiceException.getMessage());
        verify(orderService, times(0))
                .findByStatus(any(OrderStatus.class));
    }

    @Test
    public void searchOrders_ShouldReturnOrders() throws Exception {
        String startDateStr = "2023-01-01T10:00:00";
        String endDateStr = "2023-01-02T10:00:00";
        String status = "PENDING";

        LocalDateTime startDateParsed = LocalDateTime.parse(startDateStr);
        LocalDateTime endDateParsed = LocalDateTime.parse(endDateStr);

        OrderDto order1 = new OrderDto(1, null, "Advisor A",
                10, "Customer X", 100, 20,
                null, null, null, null);
        List<OrderDto> comparedOrderDtoList = List.of(order1);

        when(orderService.findOrdersByDateRange(
                startDateParsed, endDateParsed, OrderStatus.PENDING)
        ).thenReturn(comparedOrderDtoList);

        MvcResult mvcResult = mockMvc.perform(get("/api/orders/search")
                        .param("startDate", startDateStr)
                        .param("endDate", endDateStr)
                        .param("status", status))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<OrderDto> receivedOrders = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>(){}
        );

        assertEquals(comparedOrderDtoList, receivedOrders);
    }

    @Test
    public void searchOrders_WithInvalidDate_ShouldThrowServiceException() {
        String invalidDate = "not-a-date";
        String endDate = "2023-01-02T10:00:00";
        String status = "PENDING";

        assertThrows(ServiceException.class,
                () -> orderController.searchOrders(invalidDate, endDate, status)
        );

        verify(orderService, times(0)).findOrdersByDateRange(
                any(LocalDateTime.class), any(LocalDateTime.class), any(OrderStatus.class)
        );
    }
    
    @Test
    public void updateOrderStatus_ShouldReturnOk() throws Exception {
        int id = 1;
        String newStatus = "COMPLETED";

        OrderDto existingOrder = new OrderDto();
        existingOrder.setId(id);
        existingOrder.setStatus(OrderStatus.PENDING);
        when(orderService.findById(id)).thenReturn(existingOrder);

        mockMvc.perform(patch("/api/orders/{id}/status", id)
                        .param("status", newStatus))
                .andExpect(status().isOk());
    }

    @Test
    public void updateOrderStatus_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        String newStatus = "COMPLETED";
        OrderStatus statusEnum = OrderStatus.COMPLETED;

        when(orderService.findById(nonExistingId)).thenThrow(
                new ServiceException("Order not found with id: " + nonExistingId)
        );
        
        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> orderController.updateOrderStatus(nonExistingId, newStatus)
        );

        assertEquals("Order not found with id: " +
                nonExistingId, thrownException.getMessage());
        verify(orderService, times(1))
                .findById(nonExistingId);
        verify(orderService, times(0))
                .updateOrderStatus(nonExistingId, statusEnum);
    }
    
    @Test
    public void deleteOrder_ShouldReturnOk() throws Exception {
        int id = 1;
        doNothing().when(orderService).deleteOrder(id);

        mockMvc.perform(delete("/api/orders/{id}", id))
                .andExpect(status().isOk());

        verify(orderService, times(1)).deleteOrder(id);
    }
    
    @Test
    public void deleteOrder_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;

        doThrow(new ServiceException("Order not found with id: " + nonExistingId))
                .when(orderService).deleteOrder(nonExistingId);

        ServiceException thrownException = assertThrows(ServiceException.class,
                () -> orderController.deleteOrder(nonExistingId)
        );

        assertEquals("Order not found with id: " +
                nonExistingId, thrownException.getMessage());
        verify(orderService, times(1))
                .deleteOrder(nonExistingId);
    }
}