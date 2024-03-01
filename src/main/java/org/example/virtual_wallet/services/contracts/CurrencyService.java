package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.dtos.CurrencyDto;

import java.util.List;

public interface CurrencyService {
    void create(Currency currency);

    Currency get(String abbreviation);

    List<Currency> getAll();

    void update(CurrencyDto currencyDto, Currency target);

    void delete(String abbreviation);
}