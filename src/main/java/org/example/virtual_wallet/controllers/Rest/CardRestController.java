package org.example.virtual_wallet.controllers.Rest;

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

    @GetMapping
    public List<CardDto> getAll(@RequestHeader HttpHeaders httpHeaders){
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return cardService.getAllCards().stream()
                    .map(cardModelMapper::toCardDto)
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public CardDto getById(@PathVariable int id, @RequestHeader HttpHeaders httpHeaders) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return cardModelMapper.toCardDto(cardService.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public CardDto create(@RequestHeader HttpHeaders httpHeaders, @Valid @RequestBody CardDto dto) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            Card card = cardModelMapper.dtoCardCreate(dto, user);
            cardService.create(card, user);
            return cardModelMapper.toCardDto(card);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public CardDto update(@PathVariable int id, @RequestHeader HttpHeaders headers, @Valid @RequestBody CardDto dto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Card card = cardModelMapper.dtoCardUpdate(dto, id);
            cardService.update(card, user);
            return cardModelMapper.toCardDto(card);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            cardService.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
