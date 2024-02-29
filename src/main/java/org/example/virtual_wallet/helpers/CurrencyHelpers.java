package org.example.virtual_wallet.helpers;

import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.repositories.contracts.CurrencyRepository;
import org.example.virtual_wallet.services.contracts.CurrencyService;
import org.springframework.stereotype.Component;

@Component
public class CurrencyHelpers {
    private static final String ALREADY_EXISTS = "Currency already exists.";
    private final CurrencyRepository currencyRepository;

    public CurrencyHelpers(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public void checkIfExists(Currency currency) {
        boolean duplicateExists = true;

        try {
            currencyRepository.get(currency.getCurrency());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException(ALREADY_EXISTS);
        }
    }
}
