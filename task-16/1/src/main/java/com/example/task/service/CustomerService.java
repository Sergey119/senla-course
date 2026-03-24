package com.example.task.service;

import com.example.task.dao.CustomerDao;
import com.example.task.dto.CustomerDto;
import com.example.task.exception.ServiceException;
import com.example.task.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    @Autowired
    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> findAll() {
        return customerDao.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerDto findById(Integer id) throws ServiceException {
        Customer customer = customerDao.findById(id);
        if (customer == null) {
            throw new ServiceException("Customer not found with id: " + id);
        }
        return convertToDto(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> findAllSortedByName() {
        return customerDao.findAllSortedByName().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return customerDao.countAll();
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return customerDao.existsByName(name);
    }

    @Transactional
    public CustomerDto create(CustomerDto customerDto) throws ServiceException {
        if (customerDto.getName() == null || customerDto.getName().trim().isEmpty()) {
            throw new ServiceException("Customer name cannot be empty");
        }

        if (customerDao.existsByName(customerDto.getName())) {
            throw new ServiceException("Customer with name '" + customerDto.getName() + "' already exists");
        }

        Customer customer = new Customer(customerDto.getName());
        Integer id = customerDao.save(customer);
        return findById(id);
    }

    @Transactional
    public CustomerDto update(Integer id, CustomerDto customerDto) throws ServiceException {
        Customer customer = customerDao.findById(id);
        if (customer == null) {
            throw new ServiceException("Customer not found with id: " + id);
        }

        if (customerDto.getName() != null && !customerDto.getName().trim().isEmpty()) {
            // Проверяем, не занято ли имя другим клиентом
            if (!customer.getName().equals(customerDto.getName()) &&
                    customerDao.existsByName(customerDto.getName())) {
                throw new ServiceException("Customer with name '" + customerDto.getName() + "' already exists");
            }
            customer.setName(customerDto.getName());
            customerDao.update(customer);
        }

        return convertToDto(customer);
    }

    @Transactional
    public void updateName(Integer id, String newName) throws ServiceException {
        Customer customer = customerDao.findById(id);
        if (customer == null) {
            throw new ServiceException("Customer not found with id: " + id);
        }

        if (newName == null || newName.trim().isEmpty()) {
            throw new ServiceException("Customer name cannot be empty");
        }

        if (!customer.getName().equals(newName) && customerDao.existsByName(newName)) {
            throw new ServiceException("Customer with name '" + newName + "' already exists");
        }

        customerDao.updateName(id, newName);
    }

    @Transactional
    public void delete(Integer id) throws ServiceException {
        if (!customerDao.existsById(id)) {
            throw new ServiceException("Customer not found with id: " + id);
        }
        customerDao.delete(id);
    }

    private CustomerDto convertToDto(Customer customer) {
        if (customer == null) return null;
        return new CustomerDto(customer.getId(), customer.getName());
    }
}