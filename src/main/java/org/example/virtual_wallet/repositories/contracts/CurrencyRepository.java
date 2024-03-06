package org.example.virtual_wallet.repositories.contracts;

import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.dtos.CurrencyDto;

import java.util.List;

public interface CurrencyRepository {
    void create(Currency currency);
    Currency getById(int id);

    Currency get(String abbreviation);

    List<Currency> getAll();

    void update(Currency currency);

    void delete(int id);
}