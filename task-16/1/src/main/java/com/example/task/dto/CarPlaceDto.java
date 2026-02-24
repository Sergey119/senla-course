package com.example.task.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarPlaceDto {
    private Integer id;
    private Integer square;

    @JsonProperty("hasCarLift")
    private Boolean carLift;

    @JsonProperty("isOccupied")
    private Boolean isOccupied;

    public CarPlaceDto() {}

    public CarPlaceDto(Integer id, Integer square, Boolean carLift, Boolean isOccupied) {
        this.id = id;
        this.square = square;
        this.carLift = carLift;
        this.isOccupied = isOccupied;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getSquare() { return square; }
    public void setSquare(Integer square) { this.square = square; }

    public Boolean getCarLift() { return carLift; }
    public void setCarLift(Boolean carLift) { this.carLift = carLift; }

    public Boolean getIsOccupied() { return isOccupied; }
    public void setIsOccupied(Boolean occupied) { isOccupied = occupied; }
}