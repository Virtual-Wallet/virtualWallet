package org.example.virtual_wallet.models.dtos;

import jakarta.validation.constraints.NotEmpty;

public class CategoryUpdateDto {

    @NotEmpty(message = "The category id cannot be empty!")
    private int id;

    @NotEmpty(message = "The category cannot be empty!")
    private String name;

    public CategoryUpdateDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}