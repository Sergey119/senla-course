package com.example.task.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "car_place")
public class CarPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "square")
    private Integer square;

    @Column(name = "car_lift")
    private Boolean carLift;

    @Column(name = "is_occupied")
    private Boolean isOccupied;

    public CarPlace() {} // для jpa

    public CarPlace(Integer square, Boolean carLift, Boolean isOccupied) {
        this.square = square;
        this.carLift = carLift;
        this.isOccupied = isOccupied;
    }

    public CarPlace(Integer square, Boolean carLift) {
        this.square = square;
        this.carLift = carLift;
        this.isOccupied = false;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getSquare() { return square; }
    public void setSquare(Integer square) { this.square = square; }

    public Boolean getCarLift() { return carLift; }
    public void setCarLift(Boolean carLift) { this.carLift = carLift; }

    public Boolean getIsOccupied() { return isOccupied; }
    public void setIsOccupied(Boolean occupied) { isOccupied = occupied; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarPlace carPlace = (CarPlace) o;
        return Objects.equals(id, carPlace.id) && Objects.equals(square, carPlace.square) && Objects.equals(carLift, carPlace.carLift) && Objects.equals(isOccupied, carPlace.isOccupied);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, square, carLift, isOccupied);
    }

    @Override
    public String toString() {
        return "CarPlace{" +
                "id=" + id +
                ", square=" + square +
                ", carLift=" + carLift +
                ", isOccupied=" + isOccupied +
                '}';
    }
}
