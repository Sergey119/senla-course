package com.example.task.controller;

import com.example.task.dto.CustomerDto;
import com.example.task.exception.ServiceException;
import com.example.task.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() throws ServiceException {
        List<CustomerDto> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable("id") Integer id) throws ServiceException {
        CustomerDto customer = customerService.findById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/sorted/name")
    public ResponseEntity<List<CustomerDto>> getCustomersSortedByName() throws ServiceException {
        List<CustomerDto> customers = customerService.findAllSortedByName();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getCustomersCount() throws ServiceException {
        long count = customerService.countAll();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkCustomerExists(@RequestParam("name") String name) throws ServiceException {
        boolean exists = customerService.existsByName(name);
        return ResponseEntity.ok(exists);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) throws ServiceException {
        CustomerDto created = customerService.create(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(
            @PathVariable("id") Integer id,
            @RequestBody CustomerDto customerDto) throws ServiceException {
        CustomerDto updated = customerService.update(id, customerDto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<Void> updateCustomerName(
            @PathVariable("id") Integer id,
            @RequestParam("newName") String newName) throws ServiceException {
        customerService.updateName(id, newName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Integer id) throws ServiceException {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}