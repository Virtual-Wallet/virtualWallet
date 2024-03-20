package org.example.virtual_wallet;

import org.example.virtual_wallet.models.TransactionsExternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.Wallet;
import org.example.virtual_wallet.repositories.contracts.TransactionsExternalRepository;
import org.example.virtual_wallet.services.TransactionsExternalServiceImpl;
import org.example.virtual_wallet.services.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class TransactionsExternalServiceTest {

    @InjectMocks
    private TransactionsExternalServiceImpl transactionsExternalService;

    @Mock
    private TransactionsExternalRepository transactionsExternalRepository;

    @Mock
    private WalletServiceImpl walletService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getDeposits_WhenCalledWithUser_ReturnsDeposits() {
        // Arrange
        User user = new User();
        TransactionsExternal deposit1 = new TransactionsExternal();
        TransactionsExternal deposit2 = new TransactionsExternal();
        List<TransactionsExternal> expectedDeposits = Arrays.asList(deposit1, deposit2);
        when(transactionsExternalRepository.getDeposits(user)).thenReturn(expectedDeposits);

        // Act
        List<TransactionsExternal> actualDeposits = transactionsExternalService.getDeposits(user);

        // Assert
        assertEquals(expectedDeposits, actualDeposits);
    }

    @Test
    public void getWithdrawals_WhenCalledWithUser_ReturnsWithdrawals() {
        // Arrange
        User user = new User();
        TransactionsExternal withdrawal1 = new TransactionsExternal();
        TransactionsExternal withdrawal2 = new TransactionsExternal();
        List<TransactionsExternal> expectedWithdrawals = Arrays.asList(withdrawal1, withdrawal2);
        when(transactionsExternalRepository.getWithdrawals(user)).thenReturn(expectedWithdrawals);

        // Act
        List<TransactionsExternal> actualWithdrawals = transactionsExternalService.getWithdrawals(user);

        // Assert
        assertEquals(expectedWithdrawals, actualWithdrawals);
    }


    @Test
    public void createDeposit_WhenCalledWithTransfer_CreatesDeposit() {
        // Arrange
        TransactionsExternal transfer = new TransactionsExternal();
        User user = new User();
        Wallet wallet = new Wallet();
        user.setWallet(wallet);
        transfer.setUser(user);
        transfer.setAmount(1000.0);
        doNothing().when(transactionsExternalRepository).create(transfer);
        doNothing().when(walletService).deposit(user.getWallet(), transfer.getAmount());

        // Act
        TransactionsExternal actualTransfer = transactionsExternalService.createDeposit(transfer);

        // Assert
        assertEquals(transfer, actualTransfer);
    }

    @Test
    public void createWithdrawal_WhenCalledWithTransfer_CreatesWithdrawal() {
        // Arrange
        TransactionsExternal transfer = new TransactionsExternal();
        User user = new User();
        Wallet wallet = new Wallet();
        user.setWallet(wallet);
        transfer.setUser(user);
        transfer.setAmount(1000.0);
        doNothing().when(transactionsExternalRepository).create(transfer);
        doNothing().when(walletService).withdraw(user.getWallet(), transfer.getAmount());

        // Act
        TransactionsExternal actualTransfer = transactionsExternalService.createWithdrawal(transfer);

        // Assert
        assertEquals(transfer, actualTransfer);
    }

}