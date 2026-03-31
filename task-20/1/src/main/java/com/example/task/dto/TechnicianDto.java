package com.example.task.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TechnicianDto {
    private Integer id;
    private String name;
    private String specialization;

    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    public TechnicianDto() {}

    public TechnicianDto(Integer id, String name, String specialization, Boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.isAvailable = isAvailable;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean available) { isAvailable = available; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TechnicianDto that = (TechnicianDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(specialization, that.specialization) && Objects.equals(isAvailable, that.isAvailable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, specialization, isAvailable);
    }

    @Override
    public String toString() {
        return "TechnicianDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}