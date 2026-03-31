package com.example.task.service;

import com.example.task.dao.*;
import com.example.task.dto.OrderDto;
import com.example.task.exception.ServiceException;
import com.example.task.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private CarPlaceDao carPlaceDao;

    @Mock
    private CustomerDao customerDao;

    @Mock
    private ServiceAdvisorDao serviceAdvisorDao;

    @Mock
    private TechnicianDao technicianDao;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void findAll_ShouldReturnAllOrders() {
        ServiceAdvisor advisor1 = new ServiceAdvisor(10, "Advisor A");
        Customer customer1 = new Customer(20, "Customer X");
        CarPlace carPlace1 = new CarPlace(30, 20, true, false);
        Technician tech1 = new Technician(40, "Tech A", "Spec", true);

        Order order1 = new Order();
        order1.setId(1);
        order1.setServiceAdvisor(advisor1);
        order1.setCustomer(customer1);
        order1.setCarPlace(carPlace1);
        order1.setTechnicians(Collections.singletonList(tech1));

        ServiceAdvisor advisor2 = new ServiceAdvisor(11, "Advisor B");
        Customer customer2 = new Customer(21, "Customer Y");
        CarPlace carPlace2 = new CarPlace(31, 25, false, true);
        Technician tech2 = new Technician(41, "Tech B", "Spec2", false);

        Order order2 = new Order();
        order2.setId(2);
        order2.setServiceAdvisor(advisor2);
        order2.setCustomer(customer2);
        order2.setCarPlace(carPlace2);
        order2.setTechnicians(Collections.singletonList(tech2));

        List<Order> daoOrders = Arrays.asList(order1, order2);

        when(orderDao.findAll()).thenReturn(daoOrders);

        List<OrderDto> actualDtos = orderService.findAll();

        verify(orderDao, times(1)).findAll();
        assertNotNull(actualDtos);
        assertEquals(2, actualDtos.size());
    }


    @Test
    public void findById_ShouldReturnOrder() throws ServiceException {
        int id = 1;

        ServiceAdvisor advisor = new ServiceAdvisor(10, "Advisor A");
        Customer customer = new Customer(20, "Customer X");
        CarPlace carPlace = new CarPlace(30, 20, true, false);
        Technician tech = new Technician(40, "Tech A", "Spec", true);

        Order daoOrder = new Order();
        daoOrder.setId(id);
        daoOrder.setServiceAdvisor(advisor);
        daoOrder.setCustomer(customer);
        daoOrder.setCarPlace(carPlace);
        daoOrder.setTechnicians(Collections.singletonList(tech));

        when(orderDao.findById(id)).thenReturn(daoOrder);

        OrderDto actualDto = orderService.findById(id);

        assertEquals(id, actualDto.getId());
        assertEquals("Advisor A", actualDto.getServiceAdvisorName());
        assertEquals("Customer X", actualDto.getCustomerName());
        assertEquals(40, actualDto.getTechnicianIds().get(0));
    }

    @Test
    public void updateOrderStatus_WithExistingOrder_ShouldUpdateStatusAndFreePlace() throws ServiceException {
        int orderId = 1;
        OrderStatus newStatus = OrderStatus.COMPLETED;
        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setStatus(OrderStatus.IN_PROGRESS);
        CarPlace carPlace = new CarPlace(100, 20, true, true);
        existingOrder.setCarPlace(carPlace);

        when(orderDao.findById(orderId)).thenReturn(existingOrder);

        orderService.updateOrderStatus(orderId, newStatus);

        verify(orderDao, times(1))
                .updateOrderStatus(orderId, newStatus);
        verify(carPlaceDao, times(1))
                .updateOccupation(100, false);
    }

    @Test
    public void deleteOrder_WithExistingOrder_ShouldDeleteAndFreePlace() throws ServiceException {
        int orderId = 1;
        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        CarPlace carPlace = new CarPlace(100, 20, true, true);
        existingOrder.setCarPlace(carPlace);

        when(orderDao.findById(orderId)).thenReturn(existingOrder);

        orderService.deleteOrder(orderId);

        verify(orderDao, times(1))
                .deleteOrderWithRelations(orderId);
        verify(carPlaceDao, times(1))
                .updateOccupation(100, false);
    }

    @Test
    public void createOrder_WithValidData_ShouldReturnCreatedOrder() throws ServiceException {
        OrderDto inputDto = new OrderDto();
        inputDto.setServiceAdvisorId(1);
        inputDto.setCustomerId(2);
        inputDto.setCarPlaceId(3);
        inputDto.setTechnicianIds(Arrays.asList(4, 5));
        inputDto.setStartDate(LocalDateTime.now().plusDays(1));
        inputDto.setEndDate(LocalDateTime.now().plusDays(2));
        inputDto.setLoadingDate(LocalDateTime.now().plusDays(1).plusHours(12));

        ServiceAdvisor advisor = new ServiceAdvisor(1, "Advisor A");
        Customer customer = new Customer(2, "Customer X");
        CarPlace carPlace = new CarPlace(3, 20, true, false);
        Technician tech1 = new Technician(4, "Tech A", "Mechanics", true);
        Technician tech2 = new Technician(5, "Tech B", "Electronics", true);

        doAnswer(invocation -> {
            Order orderArg = invocation.getArgument(0);
            orderArg.setId(10);
            return null;
        }).when(orderDao).saveOrderWithTechnicians(any(Order.class), any(List.class));

        when(serviceAdvisorDao.findById(1)).thenReturn(advisor);
        when(customerDao.findById(2)).thenReturn(customer);
        when(carPlaceDao.findById(3)).thenReturn(carPlace);
        when(technicianDao.findById(4)).thenReturn(tech1);
        when(technicianDao.findById(5)).thenReturn(tech2);
        
        OrderDto resultDto = orderService.createOrder(inputDto);

        assertNotNull(resultDto);
        assertEquals(10, resultDto.getId());
        verify(carPlaceDao, times(1))
                .updateOccupation(3, true);
    }
    
    @Test
    public void findById_WithNonExistingId_ShouldThrowServiceException() {
        int nonExistingId = 999;
        when(orderDao.findById(nonExistingId)).thenReturn(null);
        assertThrows(ServiceException.class, () -> orderService.findById(nonExistingId));
    }

    @Test
    public void createOrder_WithOccupiedCarPlace_ShouldThrowServiceException() throws ServiceException {
        OrderDto inputDto = new OrderDto();
        inputDto.setServiceAdvisorId(1);
        inputDto.setCustomerId(2);
        inputDto.setCarPlaceId(3);

        ServiceAdvisor advisor = new ServiceAdvisor(1, "Advisor A");
        Customer customer = new Customer(2, "Customer X");
        CarPlace carPlace = new CarPlace(3, 20, true, true);

        when(serviceAdvisorDao.findById(1)).thenReturn(advisor);
        when(customerDao.findById(2)).thenReturn(customer);
        when(carPlaceDao.findById(3)).thenReturn(carPlace);

        assertThrows(ServiceException.class, () -> orderService.createOrder(inputDto));
    }

    @Test
    public void updateOrderStatus_WithNonExistingOrder_ShouldThrowServiceException() {
        int nonExistingId = 999;
        OrderStatus newStatus = OrderStatus.COMPLETED;

        when(orderDao.findById(nonExistingId)).thenReturn(null);

        assertThrows(ServiceException.class,
                () -> orderService.updateOrderStatus(nonExistingId, newStatus)
        );
    }

    @Test
    public void deleteOrder_WithNonExistingOrder_ShouldThrowServiceException() {
        int nonExistingId = 999;

        when(orderDao.findById(nonExistingId)).thenReturn(null);

        assertThrows(ServiceException.class, () -> orderService.deleteOrder(nonExistingId));
    }
}