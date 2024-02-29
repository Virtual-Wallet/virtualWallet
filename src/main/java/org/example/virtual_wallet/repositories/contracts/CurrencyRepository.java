package org.example.virtual_wallet.repositories.contracts;

import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.dtos.CurrencyDto;

import java.util.List;

public interface CurrencyRepository {
    List<Currency> getAll();

    void create(Currency currency);

    void update(Currency currency);

    void getByAbbreviation(String abbreviation);
}