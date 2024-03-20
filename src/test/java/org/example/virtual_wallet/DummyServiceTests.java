package org.example.virtual_wallet;

import org.example.virtual_wallet.exceptions.InvalidOperationException;
import org.example.virtual_wallet.services.DummyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class DummyServiceTests {

    @InjectMocks
    private DummyServiceImpl dummyService = new DummyServiceImpl();

    @Test
    void depositMoney_InvalidExpirationDate_ShouldThrowException() {
        // Arrange
        String expDate = "25/12";
        double amount = 100.0;

        // Act and Assert
        assertThrows(InvalidOperationException.class, () -> dummyService.depositMoney(expDate, amount));
    }

    @Test
    void withdrawMoney_InvalidExpirationDate_ShouldThrowException() {
        // Arrange
        String expDate = "25/12";
        double amount = 100.0;

        // Act and Assert
        assertThrows(InvalidOperationException.class, () -> dummyService.withdrawMoney(expDate, amount));
    }
}
