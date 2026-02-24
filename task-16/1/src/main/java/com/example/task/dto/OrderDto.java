package com.example.task.dto;

import com.example.task.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {
    private Integer id;

    private Integer serviceAdvisorId;
    private String serviceAdvisorName;

    private Integer customerId;
    private String customerName;

    private Integer carPlaceId;
    private Integer carPlaceSquare;

    @JsonProperty("carPlaceHasLift")
    private Boolean carPlaceHasLift;

    private List<Integer> technicianIds;
    private List<String> technicianNames;

    private OrderStatus status;
    private Integer cost;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loadingDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    public List<Integer> getTechnicianIds() { return technicianIds; }
    public void setTechnicianIds(List<Integer> technicianIds) { this.technicianIds = technicianIds; }

    public List<String> getTechnicianNames() { return technicianNames; }
    public void setTechnicianNames(List<String> technicianNames) { this.technicianNames = technicianNames; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getServiceAdvisorId() { return serviceAdvisorId; }
    public void setServiceAdvisorId(Integer serviceAdvisorId) { this.serviceAdvisorId = serviceAdvisorId; }

    public String getServiceAdvisorName() { return serviceAdvisorName; }
    public void setServiceAdvisorName(String serviceAdvisorName) { this.serviceAdvisorName = serviceAdvisorName; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public Integer getCarPlaceId() { return carPlaceId; }
    public void setCarPlaceId(Integer carPlaceId) { this.carPlaceId = carPlaceId; }

    public Integer getCarPlaceSquare() { return carPlaceSquare; }
    public void setCarPlaceSquare(Integer carPlaceSquare) { this.carPlaceSquare = carPlaceSquare; }

    public Boolean getCarPlaceHasLift() { return carPlaceHasLift; }
    public void setCarPlaceHasLift(Boolean carPlaceHasLift) { this.carPlaceHasLift = carPlaceHasLift; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public Integer getCost() { return cost; }
    public void setCost(Integer cost) { this.cost = cost; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getLoadingDate() { return loadingDate; }
    public void setLoadingDate(LocalDateTime loadingDate) { this.loadingDate = loadingDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
}