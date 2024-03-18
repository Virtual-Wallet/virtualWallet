package org.example.virtual_wallet.models.dtos;

public class CategoryDto {

//    @NotEmpty(message = "The category cannot be empty!")
    private String categoryName;

    public CategoryDto() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
