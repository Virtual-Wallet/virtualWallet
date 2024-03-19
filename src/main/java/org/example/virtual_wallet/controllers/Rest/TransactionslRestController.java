package org.example.virtual_wallet.controllers.Rest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.TransactionsInternalMapper;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.TransactionDto;
import org.example.virtual_wallet.services.contracts.TransactionsInternalService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping({"api/transactions"})
public class TransactionslRestController {

    private final TransactionsInternalService service;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final TransactionsInternalMapper transactionsInternalMapper;

    @Autowired
    public TransactionslRestController(TransactionsInternalService service,
                                       UserService userService,
                                       AuthenticationHelper authenticationHelper,
                                       TransactionsInternalMapper transactionsInternalMapper) {
        this.service = service;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.transactionsInternalMapper = transactionsInternalMapper;
    }

    @Operation(summary = "Create a new transaction", description = "Create a new transaction.")
    @PostMapping
    public TransactionsInternal create(@RequestHeader(name = "Credentials") String credentials,
                                       @Valid @RequestBody TransactionDto dto) {
        User sender;
        User recipient;

        try {
            sender = authenticationHelper.tryGetUser(credentials);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        try {
            if (!dto.getUsername().isEmpty()) {
                recipient = userService.getByUsername(dto.getUsername());
            } else if (!dto.getEmail().isEmpty()) {
                recipient = userService.getByEmail(dto.getEmail());
            } else if (!dto.getPhoneNumber().isEmpty()) {
                recipient = userService.getByPhoneNumber(dto.getPhoneNumber());
            } else {
                throw new IllegalArgumentException("At least one of Username, Email, or Phone Number must be provided.");
            }
            TransactionsInternal transactionsInternal = transactionsInternalMapper.createDto(sender, recipient, dto);
            return service.create(transactionsInternal);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Get outgoing transactions", description = "Retrieve a list of outgoing transactions for the authenticated user.")
    @GetMapping("/outgoing")
    public List<TransactionsInternal> getOutgoing(@RequestHeader(name = "Credentials") String credentials) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            return service.getOutgoing(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Get incoming transactions", description = "Retrieve a list of incoming transactions for the authenticated user.")
    @GetMapping("/incoming")
    public List<TransactionsInternal> getIncoming(@RequestHeader(name = "Credentials") String credentials) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            return service.getIncoming(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}