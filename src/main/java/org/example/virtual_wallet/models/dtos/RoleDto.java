package org.example.virtual_wallet.models.dtos;

import jakarta.validation.constraints.Size;

public class RoleDto {
    @Size(min = 1,max = 32, message = "Content must be exactly 32 characters long!")
    private String name;

    public RoleDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
