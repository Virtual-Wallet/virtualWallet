package org.example.virtual_wallet;

import org.example.virtual_wallet.filters.TransactionFilterOptions;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.Wallet;
import org.example.virtual_wallet.repositories.contracts.TransactionsInternalRepository;
import org.example.virtual_wallet.services.TransactionsInternalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TransactionsInternalServiceTest {

    @InjectMocks
    private TransactionsInternalServiceImpl transactionsInternalService;

    @Mock
    private TransactionsInternalRepository transactionsInternalRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getIncoming_WhenCalledWithUser_ReturnsIncomingTransactions() {
        // Arrange
        User user = new User();
        TransactionsInternal transaction1 = new TransactionsInternal();
        TransactionsInternal transaction2 = new TransactionsInternal();
        List<TransactionsInternal> expectedTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionsInternalRepository.getIncoming(user)).thenReturn(expectedTransactions);

        // Act
        List<TransactionsInternal> actualTransactions = transactionsInternalService.getIncoming(user);

        // Assert
        assertEquals(expectedTransactions, actualTransactions);
    }


    @Test
    public void getOutgoing_WhenCalledWithUser_ReturnsOutgoingTransactions() {
        // Arrange
        User user = new User();
        TransactionsInternal transaction1 = new TransactionsInternal();
        TransactionsInternal transaction2 = new TransactionsInternal();
        List<TransactionsInternal> expectedTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionsInternalRepository.getOutgoing(user)).thenReturn(expectedTransactions);

        // Act
        List<TransactionsInternal> actualTransactions = transactionsInternalService.getOutgoing(user);

        // Assert
        assertEquals(expectedTransactions, actualTransactions);
    }

    @Test
    public void getOutgoingPerCategory_WhenCalledWithCategoryIdAndUser_ReturnsOutgoingTransactionsPerCategory() {
        // Arrange
        User user = new User();
        int categoryId = 1;
        TransactionsInternal transaction1 = new TransactionsInternal();
        TransactionsInternal transaction2 = new TransactionsInternal();
        List<TransactionsInternal> expectedTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionsInternalRepository.getOutgoingPerCategory(categoryId, user)).thenReturn(expectedTransactions);

        // Act
        List<TransactionsInternal> actualTransactions = transactionsInternalService.getOutgoingPerCategory(categoryId, user);

        // Assert
        assertEquals(expectedTransactions, actualTransactions);
    }


    @Test
    public void getFilteredIncoming_WhenCalledWithFilterOptionsAndUser_ReturnsFilteredIncomingTransactions() {
        // Arrange
        User user = new User();
        TransactionFilterOptions filterOptions = new TransactionFilterOptions();
        TransactionsInternal transaction1 = new TransactionsInternal();
        TransactionsInternal transaction2 = new TransactionsInternal();
        List<TransactionsInternal> expectedTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionsInternalRepository.getFilteredIncoming(filterOptions, user)).thenReturn(expectedTransactions);

        // Act
        List<TransactionsInternal> actualTransactions = transactionsInternalService.getFilteredIncoming(filterOptions, user);

        // Assert
        assertEquals(expectedTransactions, actualTransactions);
    }

    @Test
    public void getFilteredOutgoing_WhenCalledWithFilterOptionsAndUser_ReturnsFilteredOutgoingTransactions() {
        // Arrange
        User user = new User();
        TransactionFilterOptions filterOptions = new TransactionFilterOptions();
        TransactionsInternal transaction1 = new TransactionsInternal();
        TransactionsInternal transaction2 = new TransactionsInternal();
        List<TransactionsInternal> expectedTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionsInternalRepository.getFilteredOutgoing(filterOptions, user)).thenReturn(expectedTransactions);

        // Act
        List<TransactionsInternal> actualTransactions = transactionsInternalService.getFilteredOutgoing(filterOptions, user);

        // Assert
        assertEquals(expectedTransactions, actualTransactions);
    }

    @Test
    public void getAll_WhenCalled_ReturnsAllTransactions() {
        // Arrange
        TransactionsInternal transaction1 = new TransactionsInternal();
        TransactionsInternal transaction2 = new TransactionsInternal();
        List<TransactionsInternal> expectedTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionsInternalRepository.getAll()).thenReturn(expectedTransactions);

        // Act
        List<TransactionsInternal> actualTransactions = transactionsInternalService.getAll();

        // Assert
        assertEquals(expectedTransactions, actualTransactions);
    }
}