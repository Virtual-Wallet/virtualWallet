package org.example.virtual_wallet.models.dtos;

import jakarta.validation.constraints.NotEmpty;

public class CategoryDto {

    @NotEmpty(message = "The category cannot be empty!")
    private String name;

    public CategoryDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
