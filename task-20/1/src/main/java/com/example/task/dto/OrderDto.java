package com.example.task.dto;

import com.example.task.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public OrderDto() {}

    public OrderDto(Integer serviceAdvisorId, String serviceAdvisorName, Integer customerId, String customerName, Integer carPlaceId, Integer carPlaceSquare, Boolean carPlaceHasLift, List<Integer> technicianIds, List<String> technicianNames, OrderStatus status, Integer cost, LocalDateTime createdDate, LocalDateTime startDate, LocalDateTime loadingDate, LocalDateTime endDate) {

        this.serviceAdvisorId = serviceAdvisorId;
        this.serviceAdvisorName = serviceAdvisorName;
        this.customerId = customerId;
        this.customerName = customerName;
        this.carPlaceId = carPlaceId;
        this.carPlaceSquare = carPlaceSquare;
        this.carPlaceHasLift = carPlaceHasLift;
        this.technicianIds = technicianIds;
        this.technicianNames = technicianNames;
        this.status = status;
        this.cost = cost;
        this.createdDate = createdDate;
        this.startDate = startDate;
        this.loadingDate = loadingDate;
        this.endDate = endDate;
    }

    public OrderDto(int id, Integer serviceAdvisorId, String serviceAdvisorName, Integer customerId, String customerName, Integer carPlaceId, Integer carPlaceSquare, LocalDateTime createdDate, LocalDateTime startDate, LocalDateTime loadingDate, LocalDateTime endDate) {
        this.id = id;
        this.serviceAdvisorId = serviceAdvisorId;
        this.serviceAdvisorName = serviceAdvisorName;
        this.customerId = customerId;
        this.customerName = customerName;
        this.carPlaceId = carPlaceId;
        this.carPlaceSquare = carPlaceSquare;
        this.createdDate = createdDate;
        this.startDate = startDate;
        this.loadingDate = loadingDate;
        this.endDate = endDate;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return Objects.equals(id, orderDto.id) && Objects.equals(serviceAdvisorId, orderDto.serviceAdvisorId) && Objects.equals(serviceAdvisorName, orderDto.serviceAdvisorName) && Objects.equals(customerId, orderDto.customerId) && Objects.equals(customerName, orderDto.customerName) && Objects.equals(carPlaceId, orderDto.carPlaceId) && Objects.equals(carPlaceSquare, orderDto.carPlaceSquare) && Objects.equals(carPlaceHasLift, orderDto.carPlaceHasLift) && Objects.equals(technicianIds, orderDto.technicianIds) && Objects.equals(technicianNames, orderDto.technicianNames) && status == orderDto.status && Objects.equals(cost, orderDto.cost) && Objects.equals(createdDate, orderDto.createdDate) && Objects.equals(startDate, orderDto.startDate) && Objects.equals(loadingDate, orderDto.loadingDate) && Objects.equals(endDate, orderDto.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceAdvisorId, serviceAdvisorName, customerId, customerName, carPlaceId, carPlaceSquare, carPlaceHasLift, technicianIds, technicianNames, status, cost, createdDate, startDate, loadingDate, endDate);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", serviceAdvisorId=" + serviceAdvisorId +
                ", serviceAdvisorName='" + serviceAdvisorName + '\'' +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", carPlaceId=" + carPlaceId +
                ", carPlaceSquare=" + carPlaceSquare +
                ", carPlaceHasLift=" + carPlaceHasLift +
                ", technicianIds=" + technicianIds +
                ", technicianNames=" + technicianNames +
                ", status=" + status +
                ", cost=" + cost +
                ", createdDate=" + createdDate +
                ", startDate=" + startDate +
                ", loadingDate=" + loadingDate +
                ", endDate=" + endDate +
                '}';
    }
}