package com.example.task.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "technician")
public class Technician {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "is_available")
    private Boolean isAvailable;

    // Связь «Многие-ко-многим» с Order
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "order_technician",
            joinColumns = @JoinColumn(name = "technician_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<Order> orders;

    public Technician() {} // для jpa

    public Technician(String name, String specialization, Boolean isAvailable) {
        this.name = name;
        this.specialization = specialization;
        this.isAvailable = isAvailable;
    }

    public Technician(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
        this.isAvailable = true;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean available) { isAvailable = available; }

    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Technician that = (Technician) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(specialization, that.specialization) && Objects.equals(isAvailable, that.isAvailable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, specialization, isAvailable);
    }

    @Override
    public String toString() {
        return "Technician{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
