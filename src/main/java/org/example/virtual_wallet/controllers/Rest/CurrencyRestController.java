package org.example.virtual_wallet.controllers.Rest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.helpers.mappers.CurrencyMapper;
import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.dtos.CurrencyDto;
import org.example.virtual_wallet.services.contracts.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @Operation(summary = "Create a new currency", description = "Create a new currency.")
    @PostMapping
    public void create(@Valid @RequestBody CurrencyDto currencyDto) {
        try {
            Currency currency = currencyMapper.createCurrencyDto(currencyDto);
            currencyService.create(currency);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Operation(summary = "Get currency by abbreviation", description = "Retrieve a currency by its abbreviation.")
    @GetMapping("/{abbreviation}")
    public Currency get(@PathVariable String abbreviation) {
        try {
            return currencyService.get(abbreviation);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Operation(summary = "Get all currencies", description = "Retrieve a list of all currencies.")
    @GetMapping
    public List<Currency> getAll() {
        return currencyService.getAll();
    }

    @Operation(summary = "Update a currency by abbreviation", description = "Update a currency by its abbreviation.")
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

    @Operation(summary = "Delete a currency by abbreviation", description = "Delete a currency by its abbreviation.")
    @DeleteMapping("/{abbreviation}")
    public void delete(@PathVariable String abbreviation) {
        try {
            currencyService.delete(abbreviation);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
