package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.models.Currency;

import java.util.List;

public interface CurrencyService {
    List<Currency> getAll();

    void create(Currency currency);

    void update(Currency currency);

    void getByAbbreviation (String abbreviation);
}