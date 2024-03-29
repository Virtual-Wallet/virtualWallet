package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.dtos.CurrencyDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CurrencyService {
    void create(Currency currency);

    Currency get(String abbreviation);

    Currency get(int id);

    List<Currency> getAll();

    void update(CurrencyDto currencyDto, Currency target);

    void delete(String abbreviation);

    Map<String, Double> getRates(Currency source) throws IOException;
}