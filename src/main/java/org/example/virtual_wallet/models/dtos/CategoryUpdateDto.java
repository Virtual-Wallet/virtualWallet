package org.example.virtual_wallet.models.dtos;

public class CategoryUpdateDto {
    private int id;
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