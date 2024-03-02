package org.example.virtual_wallet.helpers.mappers;

import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.dtos.CategoryDto;
import org.example.virtual_wallet.models.dtos.CurrencyDto;
import org.example.virtual_wallet.services.contracts.CurrencyService;
import org.example.virtual_wallet.services.contracts.SpendingCategoryService;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
        private final SpendingCategoryService service;

        public CategoryMapper(SpendingCategoryService service) {
            this.service = service;
        }

        public SpendingCategory createCategoryDto(CategoryDto categoryDto){
            SpendingCategory category = new SpendingCategory();
            category.setName(categoryDto.getName());
            return category;
        }
    }
