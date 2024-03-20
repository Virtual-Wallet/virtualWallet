package org.example.virtual_wallet;

import org.example.virtual_wallet.helpers.CurrencyHelper;
import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.dtos.CurrencyDto;
import org.example.virtual_wallet.repositories.contracts.CurrencyRepository;
import org.example.virtual_wallet.services.CurrencyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class CurrencyServiceTest {

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private CurrencyHelper currencyHelper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAll_WhenCalled_ReturnCurrencyList() {
        // Arrange
        Currency currency1 = new Currency();
        Currency currency2 = new Currency();
        List<Currency> expectedCurrencies = Arrays.asList(currency1, currency2);
        when(currencyRepository.getAll()).thenReturn(expectedCurrencies);

        // Act
        List<Currency> actualCurrencies = currencyService.getAll();

        // Assert
        assertEquals(expectedCurrencies, actualCurrencies);
    }

    @Test
    public void create_WhenCalledWithUniqueCurrency_AddsCurrencyToRepository() {
        // Arrange
        Currency currency = new Currency();
        doNothing().when(currencyHelper).checkIfDuplicate(currency);

        // Act
        currencyService.create(currency);

        // Assert
        verify(currencyHelper).checkIfDuplicate(currency);
        verify(currencyRepository).create(currency);
    }

    @Test
    public void get_WhenCalledWithAbbreviation_ReturnsCurrency() {
        // Arrange
        String abbreviation = "USD";
        Currency expectedCurrency = new Currency();
        when(currencyRepository.get(abbreviation)).thenReturn(expectedCurrency);

        // Act
        Currency actualCurrency = currencyService.get(abbreviation);

        // Assert
        assertEquals(expectedCurrency, actualCurrency);
    }

    @Test
    public void get_WhenCalledWithId_ReturnsCurrency() {
        // Arrange
        int id = 1;
        Currency expectedCurrency = new Currency();
        when(currencyRepository.getById(id)).thenReturn(expectedCurrency);

        // Act
        Currency actualCurrency = currencyService.get(id);

        // Assert
        assertEquals(expectedCurrency, actualCurrency);
    }

    @Test
    public void delete_WhenCalledWithAbbreviation_DeletesCurrency() {
        // Arrange
        String abbreviation = "USD";
        Currency currency = new Currency();
        currency.setId(1);
        when(currencyRepository.get(abbreviation)).thenReturn(currency);

        // Act
        currencyService.delete(abbreviation);

        // Assert
        verify(currencyRepository).get(abbreviation);
        verify(currencyRepository).delete(currency.getId());
    }

    @Test
    public void update_WhenCalledWithCurrencyDtoAndTarget_UpdatesCurrency() {
        // Arrange
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setAbbreviation("USD");
        Currency target = new Currency();
        doNothing().when(currencyHelper).checkIfExists(target);
        doNothing().when(currencyHelper).checkIfDuplicate(target);

        // Act
        currencyService.update(currencyDto, target);

        // Assert
        verify(currencyHelper).checkIfExists(target);
        verify(currencyHelper).checkIfDuplicate(target);
        verify(currencyRepository).update(target);
    }


}