package org.example.virtual_wallet.services;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.example.virtual_wallet.helpers.CurrencyHelper;
import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.dtos.CurrencyDto;
import org.example.virtual_wallet.repositories.contracts.CurrencyRepository;
import org.example.virtual_wallet.services.contracts.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyHelper currencyHelper;


    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository,
                               CurrencyHelper currencyHelper) {
        this.currencyRepository = currencyRepository;
        this.currencyHelper = currencyHelper;
    }

    @Override
    public void create(Currency currency) {
        currencyHelper.checkIfDuplicate(currency);
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
        currencyHelper.checkIfExists(target);

        Currency source = new Currency();
        source.setCurrency(currencyDto.getAbbreviation());

        currencyHelper.checkIfDuplicate(source);
        target.setCurrency(currencyDto.getAbbreviation());
        currencyRepository.update(target);
    }

    @Override
    public void delete(String abbreviation) {
        Currency target = currencyRepository.get(abbreviation);
        currencyRepository.delete(target.getId());
    }

    @Override
    public Map<String, Object> consumeExchangeRate(String sourceCurrency, String targetCurrency) throws IOException {
        String url_str = "https://v6.exchangerate-api.com/v6/8779bb0271058064063db1d5/latest/" + sourceCurrency;

        // Making Request
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Convert to JSON
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        // Convert JsonObject into Map
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> responseMap = gson.fromJson(jsonobj, type);

        return responseMap;
    }

}