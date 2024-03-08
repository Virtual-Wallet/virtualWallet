package org.example.virtual_wallet.controllers.Rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.helpers.mappers.CurrencyMapper;
import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.dtos.CurrencyApiResponse;
import org.example.virtual_wallet.models.dtos.CurrencyDto;
import org.example.virtual_wallet.services.contracts.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyRestController {
    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;

    @Autowired
    public CurrencyRestController(CurrencyService currencyService,
                                  CurrencyMapper currencyMapper) {
        this.currencyService = currencyService;
        this.currencyMapper = currencyMapper;
    }

    @PostMapping
    public void create(@Valid @RequestBody CurrencyDto currencyDto) {
        try {
            Currency currency = currencyMapper.createCurrencyDto(currencyDto);
            currencyService.create(currency);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/{abbreviation}")
    public Currency get(@PathVariable String abbreviation) {
        try {
            return currencyService.get(abbreviation);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping
    public List<Currency> getAll() {
        return currencyService.getAll();
    }

    @PutMapping("/{abbreviation}")
    public void update(@PathVariable String abbreviation,
                       @Valid @RequestBody CurrencyDto currencyDto) {
        try {
            Currency target = currencyService.get(abbreviation);
            currencyService.update(currencyDto, target);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{abbreviation}")
    public void delete(@PathVariable String abbreviation) {
        try {
            currencyService.delete(abbreviation);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
