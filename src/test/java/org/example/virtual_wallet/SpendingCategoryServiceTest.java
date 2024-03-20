package org.example.virtual_wallet;

import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.exceptions.InvalidOperationException;
import org.example.virtual_wallet.helpers.AuthorizationHelper;
import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.CardRepository;
import org.example.virtual_wallet.repositories.contracts.SpendingCategoryRepository;
import org.example.virtual_wallet.services.CardServiceImpl;
import org.example.virtual_wallet.services.SpendingCategoryServiceImpl;
import org.example.virtual_wallet.services.contracts.SpendingCategoryService;
import org.example.virtual_wallet.services.contracts.TransactionsInternalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpendingCategoryServiceTest {
    @Mock
    AuthorizationHelper authorizationHelper;
    @Mock
    SpendingCategoryRepository categoryRepository;
    @InjectMocks
    SpendingCategoryServiceImpl categoryService;

    @Test
    void create_Should_CreateCategory_When_NotAlreadyExists() {
        // Arrange
        SpendingCategory category = new SpendingCategory();
        category.setName("Test Category");
        User user = new User();

        // Mock repository behavior
        when(categoryRepository.getByCategoryAndUser(category.getName(), user)).thenThrow(EntityNotFoundException.class);

        // Act
        categoryService.create(category, user);

        // Assert
        verify(categoryRepository, times(1)).create(category);
    }

    @Test
    void create_Should_ThrowDuplicateException_When_CategoryAlreadyExists() {
        // Arrange
        SpendingCategory category = new SpendingCategory();
        category.setName("Test Category");
        User user = new User();

        // Mock repository behavior
        when(categoryRepository.getByCategoryAndUser(category.getName(), user)).thenReturn(category);

        // Act & Assert
        assertThrows(EntityDuplicateException.class, () -> categoryService.create(category, user));
    }
    @Test
    void update_Should_UpdateCategoryAndReturnIt() {
        // Arrange
        SpendingCategory category = Helpers.spendingCategory();
        User user = Helpers.createMockUserRegular();

        // Mock repository behavior
       categoryRepository.update(category);

        // Act
        SpendingCategory updatedCategory = categoryService.update(category, user);

        // Assert
        assertEquals(category, updatedCategory);
        verify(categoryRepository, times(2)).update(category);
    }

    @Test
    void getByCategoryAndUser_Should_ReturnCategoryFromRepository() {
        // Arrange
        String categoryName = "Test Category";
        User user = new User();
        SpendingCategory expectedCategory = new SpendingCategory();

        // Mock repository behavior
        when(categoryRepository.getByCategoryAndUser(categoryName, user)).thenReturn(expectedCategory);

        // Act
        SpendingCategory retrievedCategory = categoryService.getByCategoryAndUser(categoryName, user);

        // Assert
        assertEquals(expectedCategory, retrievedCategory);
        verify(categoryRepository, times(1)).getByCategoryAndUser(categoryName, user);
    }
    @Test
    void getById_Should_ReturnCategory_When_ValidId() {
        // Arrange
        int categoryId = 1;
        SpendingCategory expectedCategory = new SpendingCategory();
        expectedCategory.setSpendingCategoryId(categoryId);

        // Mock repository behavior
        when(categoryRepository.getById(categoryId)).thenReturn(expectedCategory);

        // Act
        SpendingCategory actualCategory = categoryService.getById(categoryId);

        // Assert
        assertEquals(expectedCategory, actualCategory);
        verify(categoryRepository, times(1)).getById(categoryId);
    }
    @Test
    void getAllUserCategories_Should_ReturnCategories_When_ValidUser() {
        // Arrange
        User user = new User();
        List<SpendingCategory> expectedCategories = new ArrayList<>();

        // Mock repository behavior
        when(categoryRepository.getAllUserCategories(user)).thenReturn(expectedCategories);

        // Act
        List<SpendingCategory> actualCategories = categoryService.getAllUserCategories(user);

        // Assert
        assertEquals(expectedCategories, actualCategories);
        verify(categoryRepository, times(1)).getAllUserCategories(user);
    }

}


