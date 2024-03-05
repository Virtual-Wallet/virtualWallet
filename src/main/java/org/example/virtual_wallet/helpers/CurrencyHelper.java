package org.example.virtual_wallet.helpers;

import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.repositories.contracts.CurrencyRepository;
import org.springframework.stereotype.Component;

@Component
public class CurrencyHelper {
    private final CurrencyRepository currencyRepository;

    public CurrencyHelper(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public void checkIfDuplicate(Currency currency) {
        boolean duplicateExists = true;

        try {
            currencyRepository.get(currency.getCurrency());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Currency", "abbreviation", currency.getCurrency());
        }
    }

    public void checkIfExists(Currency currency) {

        if (currencyRepository.get(currency.getCurrency()) == null) {
            throw new EntityNotFoundException("Currency", "abbreviation", currency.getCurrency());
        }
    }
}
