package com.example.task.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "service_advisor_id", referencedColumnName = "id")
    private ServiceAdvisor serviceAdvisor;

    // Связь «Многие-ко-многим» с Technician
    @ManyToMany(mappedBy = "orders", cascade = CascadeType.PERSIST)
    private List<Technician> technicians;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "car_place_id", referencedColumnName = "id")
    private CarPlace carPlace;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "loading_date")
    private LocalDateTime loadingDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    public Order() {} // для jpa

    public Order(ServiceAdvisor serviceAdvisor, List<Technician> technicians, Customer customer,
                 CarPlace carPlace, OrderStatus status, Integer cost, LocalDateTime createdDate,
                 LocalDateTime startDate, LocalDateTime loadingDate, LocalDateTime endDate) {
        this.serviceAdvisor = serviceAdvisor;
        this.technicians = technicians;
        this.customer = customer;
        this.carPlace = carPlace;
        this.status = status;
        this.cost = cost;
        this.createdDate = createdDate;
        this.startDate = startDate;
        this.loadingDate = loadingDate;
        this.endDate = endDate;
    }

    public Order(ServiceAdvisor serviceAdvisor, List<Technician> technicians, Customer customer,
                 CarPlace carPlace, LocalDateTime createdDate, LocalDateTime startDate, LocalDateTime loadingDate,
                 LocalDateTime endDate) {
        this.serviceAdvisor = serviceAdvisor;
        this.technicians = technicians;
        this.customer = customer;
        this.carPlace = carPlace;
        this.status = OrderStatus.PENDING;
        this.cost = 0;
        this.createdDate = createdDate;
        this.startDate = startDate;
        this.loadingDate = loadingDate;
        this.endDate = endDate;
    }

    public Order(ServiceAdvisor serviceAdvisor, Customer customer,
                 CarPlace carPlace, LocalDateTime createdDate, LocalDateTime startDate, LocalDateTime loadingDate,
                 LocalDateTime endDate) {
        this.serviceAdvisor = serviceAdvisor;
        this.technicians = new ArrayList<>();
        this.customer = customer;
        this.carPlace = carPlace;
        this.status = OrderStatus.PENDING;
        this.cost = 0;
        this.createdDate = createdDate;
        this.startDate = startDate;
        this.loadingDate = loadingDate;
        this.endDate = endDate;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ServiceAdvisor getServiceAdvisor() { return serviceAdvisor; }
    public void setServiceAdvisor(ServiceAdvisor serviceAdvisor) { this.serviceAdvisor = serviceAdvisor; }

    public List<Technician> getTechnicians() { return technicians; }
    public void setTechnicians(List<Technician> technicians) { this.technicians = technicians; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public CarPlace getCarPlace() { return carPlace; }
    public void setCarPlace(CarPlace carPlace) { this.carPlace = carPlace; }

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
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(serviceAdvisor, order.serviceAdvisor) && Objects.equals(customer, order.customer) && Objects.equals(carPlace, order.carPlace) && status == order.status && Objects.equals(cost, order.cost) && Objects.equals(createdDate, order.createdDate) && Objects.equals(startDate, order.startDate) && Objects.equals(loadingDate, order.loadingDate) && Objects.equals(endDate, order.endDate) && Objects.equals(technicians, order.technicians);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceAdvisor, customer, carPlace, status, cost, createdDate, startDate, loadingDate, endDate, technicians);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", serviceAdvisor=" + serviceAdvisor +
                ", customer=" + customer +
                ", carPlace=" + carPlace +
                ", status=" + status +
                ", cost=" + cost +
                ", createdDate=" + createdDate +
                ", startDate=" + startDate +
                ", loadingDate=" + loadingDate +
                ", endDate=" + endDate +
                ", technicians=" + technicians +
                '}';
    }
}