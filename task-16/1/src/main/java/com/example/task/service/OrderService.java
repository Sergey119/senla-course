package com.example.task.service;

import com.example.task.dao.*;
import com.example.task.dto.*;
import com.example.task.exception.ServiceException;
import com.example.task.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderDao orderDao;
    private final CarPlaceDao carPlaceDao;
    private final CustomerDao customerDao;
    private final ServiceAdvisorDao serviceAdvisorDao;
    private final TechnicianDao technicianDao;

    @Autowired
    public OrderService(OrderDao orderDao,
                        CarPlaceDao carPlaceDao,
                        CustomerDao customerDao,
                        ServiceAdvisorDao serviceAdvisorDao,
                        TechnicianDao technicianDao) {
        this.orderDao = orderDao;
        this.carPlaceDao = carPlaceDao;
        this.customerDao = customerDao;
        this.serviceAdvisorDao = serviceAdvisorDao;
        this.technicianDao = technicianDao;
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAll() {
        return orderDao.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDto findById(Integer id) throws ServiceException {
        Order order = orderDao.findById(id);
        if (order == null) {
            throw new ServiceException("Order not found with id: " + id);
        }
        return convertToDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findByStatus(OrderStatus status) {
        return orderDao.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findCurrentlyRunningSortedByStartDate() {
        return orderDao.findCurrentlyRunningOrdersSortedByStartDate().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findOrdersByTechnicianId(Integer technicianId) {
        return orderDao.findOrdersByTechnicianId(technicianId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate, OrderStatus status) {
        return orderDao.findOrdersByDateRange(startDate, endDate, status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto createOrder(OrderDto orderDto) throws ServiceException {
        try {
            System.out.println(orderDto);

            List<Integer> technicianIds = orderDto.getTechnicianIds();

            ServiceAdvisor advisor = serviceAdvisorDao.findById(orderDto.getServiceAdvisorId());
            Customer customer = customerDao.findById(orderDto.getCustomerId());
            CarPlace carPlace = carPlaceDao.findById(orderDto.getCarPlaceId());

            if (advisor == null) {
                throw new ServiceException("Service advisor not found with id: " + orderDto.getServiceAdvisorId());
            }
            if (customer == null) {
                throw new ServiceException("Customer not found with id: " + orderDto.getCustomerId());
            }
            if (carPlace == null) {
                throw new ServiceException("Car place not found with id: " + orderDto.getCarPlaceId());
            }

            if (carPlace.getIsOccupied()) {
                throw new ServiceException("Car place #" + carPlace.getId() + " is already occupied");
            }

            List<Technician> technicians = loadTechnicians(technicianIds);

            Order order = buildOrder(orderDto, advisor, customer, carPlace, technicians);

            orderDao.saveOrderWithTechnicians(order, technicianIds);

            carPlaceDao.updateOccupation(carPlace.getId(), true);

            return convertToDto(order);
        } catch (Exception | ServiceException e) {
            throw e;
        }
    }

    @Transactional
    public OrderDto updateOrder(Integer id, OrderDto orderDto) throws ServiceException {
        Order existingOrder = orderDao.findById(id);
        if (existingOrder == null) {
            throw new ServiceException("Order not found with id: " + id);
        }

        if (orderDto.getStatus() != null) {
            existingOrder.setStatus(orderDto.getStatus());
        }
        if (orderDto.getCost() != null) {
            existingOrder.setCost(orderDto.getCost());
        }
        if (orderDto.getStartDate() != null) {
            existingOrder.setStartDate(orderDto.getStartDate());
        }
        if (orderDto.getEndDate() != null) {
            existingOrder.setEndDate(orderDto.getEndDate());
        }
        if (orderDto.getLoadingDate() != null) {
            existingOrder.setLoadingDate(orderDto.getLoadingDate());
        }

        orderDao.update(existingOrder);
        return convertToDto(existingOrder);
    }

    @Transactional
    public void updateOrderStatus(Integer id, OrderStatus status) throws ServiceException {
        Order order = orderDao.findById(id);
        if (order == null) {
            throw new ServiceException("Order not found with id: " + id);
        }

        OrderStatus oldStatus = order.getStatus();
        orderDao.updateOrderStatus(id, status);

        if (status == OrderStatus.COMPLETED || status == OrderStatus.CANCELLED) {
            if (order.getCarPlace() != null) {
                carPlaceDao.updateOccupation(order.getCarPlace().getId(), false);
            }
        }
    }

    @Transactional
    public void deleteOrder(Integer id) throws ServiceException {
        Order order = orderDao.findById(id);
        if (order == null) {
            throw new ServiceException("Order not found with id: " + id);
        }

        if (order.getCarPlace() != null) {
            carPlaceDao.updateOccupation(order.getCarPlace().getId(), false);
        }

        orderDao.deleteOrderWithRelations(id);
    }

    @Transactional
    public void addTechnicianToOrder(Integer orderId, Integer technicianId) throws ServiceException {
        Order order = orderDao.findById(orderId);
        if (order == null) {
            throw new ServiceException("Order not found with id: " + orderId);
        }

        Technician technician = technicianDao.findById(technicianId);
        if (technician == null) {
            throw new ServiceException("Technician not found with id: " + technicianId);
        }

        orderDao.addTechnicianToOrder(orderId, technicianId);
    }

    @Transactional
    public void removeTechnicianFromOrder(Integer orderId, Integer technicianId) {
        orderDao.removeTechnicianFromOrder(orderId, technicianId);
    }

    @Transactional
    public void shiftOrderTime(Integer orderId, int hoursToShift) throws ServiceException {
        Order order = orderDao.findById(orderId);
        if (order == null) {
            throw new ServiceException("Order not found with id: " + orderId);
        }
        orderDao.shiftOrderTime(orderId, hoursToShift);
    }

    private List<Technician> loadTechnicians(List<Integer> technicianIds) throws ServiceException {
        List<Technician> technicians = new ArrayList<>();
        for (Integer techId : technicianIds) {
            Technician tech = technicianDao.findById(techId);
            if (tech == null) {
                throw new ServiceException("Technician not found with id: " + techId);
            }
            if (!tech.getIsAvailable()) {
                throw new ServiceException("Technician " + tech.getName() + " is not available");
            }
            technicians.add(tech);
        }
        return technicians;
    }

    private Order buildOrder(OrderDto orderDto, ServiceAdvisor advisor,
                             Customer customer, CarPlace carPlace,
                             List<Technician> technicians) {
        Order order = new Order();
        order.setServiceAdvisor(advisor);
        order.setCustomer(customer);
        order.setCarPlace(carPlace);
        order.setTechnicians(technicians);
        order.setStatus(orderDto.getStatus() != null ? orderDto.getStatus() : OrderStatus.PENDING);
        order.setCost(orderDto.getCost() != null ? orderDto.getCost() : 0);
        order.setCreatedDate(LocalDateTime.now());
        order.setStartDate(orderDto.getStartDate());
        order.setLoadingDate(orderDto.getLoadingDate());
        order.setEndDate(orderDto.getEndDate());
        return order;
    }

    private OrderDto convertToDto(Order order) {
        if (order == null) return null;

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setCost(order.getCost());
        dto.setCreatedDate(order.getCreatedDate());
        dto.setStartDate(order.getStartDate());
        dto.setLoadingDate(order.getLoadingDate());
        dto.setEndDate(order.getEndDate());

        if (order.getServiceAdvisor() != null) {
            dto.setServiceAdvisorId(order.getServiceAdvisor().getId());
            dto.setServiceAdvisorName(order.getServiceAdvisor().getName());
        }

        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getId());
            dto.setCustomerName(order.getCustomer().getName());
        }

        if (order.getCarPlace() != null) {
            dto.setCarPlaceId(order.getCarPlace().getId());
            dto.setCarPlaceSquare(order.getCarPlace().getSquare());
            dto.setCarPlaceHasLift(order.getCarPlace().getCarLift());
        }

        if (order.getTechnicians() != null && !order.getTechnicians().isEmpty()) {
            List<Integer> techIds = new ArrayList<>();
            List<String> techNames = new ArrayList<>();

            for (Technician tech : order.getTechnicians()) {
                techIds.add(tech.getId());
                techNames.add(tech.getName());
            }

            dto.setTechnicianIds(techIds);
            dto.setTechnicianNames(techNames);
        }

        return dto;
    }
}