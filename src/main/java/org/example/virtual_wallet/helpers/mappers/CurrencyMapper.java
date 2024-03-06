package org.example.virtual_wallet.helpers.mappers;

import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.dtos.CurrencyDto;
import org.example.virtual_wallet.services.contracts.CurrencyService;
import org.springframework.stereotype.Component;

@Component
public class CurrencyMapper {
private final CurrencyService currencyService;

    public CurrencyMapper(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public Currency createCurrencyDto(CurrencyDto currencyDto){
        Currency currency = new Currency();
        currency.setCurrency(currencyDto.getAbbreviation());
        return currency;
    }
}
