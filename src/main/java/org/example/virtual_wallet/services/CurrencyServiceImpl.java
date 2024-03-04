package org.example.virtual_wallet.services;

import org.example.virtual_wallet.helpers.CurrencyHelpers;
import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.dtos.CurrencyDto;
import org.example.virtual_wallet.repositories.contracts.CurrencyRepository;
import org.example.virtual_wallet.services.contracts.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyHelpers currencyHelpers;


    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository,
                               CurrencyHelpers currencyHelpers) {
        this.currencyRepository = currencyRepository;
        this.currencyHelpers = currencyHelpers;
    }

    @Override
    public void create(Currency currency) {
        currencyHelpers.checkIfDuplicate(currency);
        currencyRepository.create(currency);
    }

    @Override
    public Currency get(String abbreviation) {
        return currencyRepository.get(abbreviation);
    }

    @Override
    public Currency get(int id) {
        return currencyRepository.getById(id);
    }

    @Override
    public List<Currency> getAll() {
        return currencyRepository.getAll();
    }

    @Override
    public void update(CurrencyDto currencyDto, Currency target) {
        currencyHelpers.checkIfExists(target);

        Currency source = new Currency();
        source.setCurrency(currencyDto.getAbbreviation());

        currencyHelpers.checkIfDuplicate(source);
        target.setCurrency(currencyDto.getAbbreviation());
        currencyRepository.update(target);
    }

    @Override
    public void delete(String abbreviation) {
        Currency target = currencyRepository.get(abbreviation);
        currencyRepository.delete(target.getId());
    }
}