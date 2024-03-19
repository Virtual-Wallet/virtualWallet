package org.example.virtual_wallet.controllers.Rest;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.CardMapper;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.CardDto;
import org.example.virtual_wallet.services.contracts.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cards")
public class CardRestController {

    private final CardService cardService;
    private final CardMapper cardModelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CardRestController(CardService cardService,
                              CardMapper cardModelMapper,
                              AuthenticationHelper authenticationHelper) {
        this.cardService = cardService;
        this.cardModelMapper = cardModelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @Operation(summary = "Get all cards", description = "Get a list of all cards.")
    @GetMapping
    public List<CardDto> getAll(@RequestHeader(name = "Credentials") String credentials){
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            return cardService.getAllCards().stream()
                    .map(cardModelMapper::toCardDto)
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Get card by ID", description = "Retrieve a card by its ID.")
    @GetMapping("/{id}")
    public CardDto getById(@PathVariable int id, @RequestHeader(name = "Credentials") String credentials) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            return cardModelMapper.toCardDto(cardService.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Create a new card", description = "Create a new card.")
    @PostMapping
    public CardDto create(@RequestHeader(name = "Credentials") String credentials, @Valid @RequestBody CardDto dto) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            Card card = cardModelMapper.dtoCardCreate(dto, user);
            cardService.create(card, user);
            return cardModelMapper.toCardDto(card);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Update an existing card", description = "Update an existing card by its ID.")
    @PutMapping("/{id}")
    public CardDto update(@PathVariable int id, @RequestHeader(name = "Credentials") String credentials, @Valid @RequestBody CardDto dto) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            Card card = cardModelMapper.dtoCardUpdate(dto, id);
            cardService.update(card, user);
            return cardModelMapper.toCardDto(card);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Delete a card by ID", description = "Delete a card by its ID.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Set response status to 204 No Content
    public void delete(@PathVariable int id, @RequestHeader(name = "Credentials") String credentials) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            cardService.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
