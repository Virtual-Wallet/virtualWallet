package org.example.virtual_wallet;

import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.exceptions.InvalidOperationException;
import org.example.virtual_wallet.helpers.AuthorizationHelper;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.Wallet;
import org.example.virtual_wallet.repositories.contracts.SpendingCategoryRepository;
import org.example.virtual_wallet.repositories.contracts.WalletRepository;
import org.example.virtual_wallet.services.SpendingCategoryServiceImpl;
import org.example.virtual_wallet.services.WalletServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTests {
    @Mock
    private AuthorizationHelper authorizationHelper;

    @Mock
    WalletRepository walletRepository;
    @InjectMocks
    WalletServiceImpl walletService;
    @Test
    void create_UniqueUser_Should_CreateWallet() {
        // Arrange
        User user = new User();
        Wallet wallet = new Wallet();

        // Mock repository behavior
        when(walletRepository.getWalletByUserId(user.getId())).thenThrow(new EntityNotFoundException(""));

        // Act
        assertDoesNotThrow(() -> walletService.create(wallet, user));

        // Assert
        verify(walletRepository).create(wallet); // Verify that create method was called

    }

    @Test
    void create_DuplicateUser_Should_ThrowException() {
        // Arrange
        User user = new User();
        Wallet wallet = new Wallet();

        // Mock repository behavior
        when(walletRepository.getWalletByUserId(user.getId())).thenReturn(new Wallet());

        // Act and Assert
        assertThrows(EntityDuplicateException.class, () -> walletService.create(wallet, user));
    }

    @Test
    void delete_ValidWallet_Should_DeleteAndReturn() {
        // Arrange
        int walletId = 1;
        User user = Helpers.createMockUserRegular();
        Wallet wallet = Helpers.createMockWallet();
        wallet.setId(walletId);
        wallet.setBalance(0); // Set balance to 0 or less to allow deletion

        // Mock repository behavior
        when(walletRepository.getById(walletId)).thenReturn(wallet);
        authorizationHelper.validateUserIsWalletOwner(user, wallet);
        doNothing().when(walletRepository).update(wallet);

        // Act and Assert
        assertDoesNotThrow(() -> walletService.delete(walletId, user));

        // Assert
        assertTrue(wallet.isDeleted());
        verify(walletRepository, times(1)).update(wallet);
    }


    @Test
    void delete_InvalidWallet_Should_ThrowException() {
        // Arrange
        int walletId = 1;
        User user = Helpers.createMockUserRegular();


        // Mock repository behavior to return null
        when(walletRepository.getById(walletId)).thenReturn(null);

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> walletService.delete(walletId, user));
    }

    @Test
    void delete_UnauthorizedUser_Should_ThrowException() {
        // Arrange
        int walletId = 1;
        User user = Helpers.createMockUserRegular();
        Wallet wallet = Helpers.createMockWallet();
        wallet.setId(walletId);

        // Mock repository behavior
        when(walletRepository.getById(walletId)).thenReturn(wallet);
        authorizationHelper.validateUserIsWalletOwner(user, wallet);

        // Act and Assert
        assertThrows(InvalidOperationException.class, () -> walletService.delete(walletId, user));
    }


}

