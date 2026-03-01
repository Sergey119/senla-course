package com.example.task.service;

import com.example.task.dao.ServiceAdvisorDao;
import com.example.task.dto.ServiceAdvisorDto;
import com.example.task.exception.ServiceException;
import com.example.task.model.ServiceAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceAdvisorService {

    private final ServiceAdvisorDao serviceAdvisorDao;

    @Autowired
    public ServiceAdvisorService(ServiceAdvisorDao serviceAdvisorDao) {
        this.serviceAdvisorDao = serviceAdvisorDao;
    }

    @Transactional(readOnly = true)
    public List<ServiceAdvisorDto> findAll() {
        return serviceAdvisorDao.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServiceAdvisorDto findById(Integer id) throws ServiceException {
        ServiceAdvisor advisor = serviceAdvisorDao.findById(id);
        if (advisor == null) {
            throw new ServiceException("Service advisor not found with id: " + id);
        }
        return convertToDto(advisor);
    }

    @Transactional(readOnly = true)
    public List<ServiceAdvisorDto> findAllSortedByName() {
        return serviceAdvisorDao.findAllSortedByName().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return serviceAdvisorDao.countAll();
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return serviceAdvisorDao.existsByName(name);
    }

    @Transactional
    public ServiceAdvisorDto create(ServiceAdvisorDto advisorDto) throws ServiceException {
        if (advisorDto.getName() == null || advisorDto.getName().trim().isEmpty()) {
            throw new ServiceException("Service advisor name cannot be empty");
        }

        if (serviceAdvisorDao.existsByName(advisorDto.getName())) {
            throw new ServiceException("Service advisor with name '" + advisorDto.getName() + "' already exists");
        }

        ServiceAdvisor advisor = new ServiceAdvisor(advisorDto.getName());
        Integer id = serviceAdvisorDao.save(advisor);
        return findById(id);
    }

    @Transactional
    public ServiceAdvisorDto update(Integer id, ServiceAdvisorDto advisorDto) throws ServiceException {
        ServiceAdvisor advisor = serviceAdvisorDao.findById(id);
        if (advisor == null) {
            throw new ServiceException("Service advisor not found with id: " + id);
        }

        if (advisorDto.getName() != null && !advisorDto.getName().trim().isEmpty()) {
            if (!advisor.getName().equals(advisorDto.getName()) &&
                    serviceAdvisorDao.existsByName(advisorDto.getName())) {
                throw new ServiceException("Service advisor with name '" + advisorDto.getName() + "' already exists");
            }
            advisor.setName(advisorDto.getName());
            serviceAdvisorDao.update(advisor);
        }

        return convertToDto(advisor);
    }

    @Transactional
    public void updateName(Integer id, String newName) throws ServiceException {
        ServiceAdvisor advisor = serviceAdvisorDao.findById(id);
        if (advisor == null) {
            throw new ServiceException("Service advisor not found with id: " + id);
        }

        if (newName == null || newName.trim().isEmpty()) {
            throw new ServiceException("Service advisor name cannot be empty");
        }

        if (!advisor.getName().equals(newName) && serviceAdvisorDao.existsByName(newName)) {
            throw new ServiceException("Service advisor with name '" + newName + "' already exists");
        }

        serviceAdvisorDao.updateName(id, newName);
    }

    @Transactional
    public void delete(Integer id) throws ServiceException {
        if (!serviceAdvisorDao.existsById(id)) {
            throw new ServiceException("Service advisor not found with id: " + id);
        }
        serviceAdvisorDao.delete(id);
    }

    private ServiceAdvisorDto convertToDto(ServiceAdvisor advisor) {
        if (advisor == null) return null;
        return new ServiceAdvisorDto(advisor.getId(), advisor.getName());
    }
}