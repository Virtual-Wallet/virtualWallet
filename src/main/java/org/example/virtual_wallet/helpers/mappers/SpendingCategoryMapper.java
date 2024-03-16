package org.example.virtual_wallet.helpers.mappers;

import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.CategoryDto;
import org.example.virtual_wallet.services.contracts.SpendingCategoryService;
import org.springframework.stereotype.Component;

@Component
public class SpendingCategoryMapper {
        private final SpendingCategoryService service;

    public SpendingCategoryMapper(SpendingCategoryService service) {
        this.service = service;
    }

    public SpendingCategory createCategoryDto(CategoryDto categoryDto) {
        SpendingCategory category = new SpendingCategory();
        category.setName(categoryDto.getName());
        return category;
    }
    public SpendingCategory fromDTO(CategoryDto categoryDto, User user) {
        SpendingCategory category = new SpendingCategory();
        category.setName(categoryDto.getName());
        category.setCreator(user);
        return category;
    }
    public SpendingCategory dtoCategoryUpdate(CategoryDto categoryDto, int categoryId) {
        SpendingCategory category = service.getById(categoryId);
        category.setName(categoryDto.getName());
        return category;
    }

    public CategoryDto toDto(SpendingCategory spendingCategory){
        CategoryDto dto = new CategoryDto();
        dto.setName(spendingCategory.getName());
        return dto;
    }
}

