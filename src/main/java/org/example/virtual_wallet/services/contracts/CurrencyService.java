package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.models.Currency;

import java.util.List;

public interface CurrencyService {
    void create(Currency currency);

    Currency get(String abbreviation);

    List<Currency> getAll();

    void update(Currency currency);
}