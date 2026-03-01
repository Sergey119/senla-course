package com.example.task.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDto {
    private Integer id;
    private String name;

    public CustomerDto() {}

    public CustomerDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}