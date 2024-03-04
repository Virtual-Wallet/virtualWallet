package org.example.virtual_wallet.controllers.Rest;

import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.TransactionsInternalMapper;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.TransactionsInternalDto;
import org.example.virtual_wallet.services.contracts.TransactionsInternalService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping({"api/transactions"})
public class TransactionsInternalRestController {

    private final TransactionsInternalService service;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final TransactionsInternalMapper transactionsInternalMapper;

    public TransactionsInternalRestController(TransactionsInternalService service,
                                              UserService userService,
                                              AuthenticationHelper authenticationHelper,
                                              TransactionsInternalMapper transactionsInternalMapper) {
        this.service = service;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.transactionsInternalMapper = transactionsInternalMapper;
    }

    @PostMapping
    public TransactionsInternal getAll(@RequestHeader HttpHeaders httpHeaders,
                                       @Valid @RequestBody TransactionsInternalDto dto) {

        User sender = new User();
        User recipient = new User();

        try {
            sender = authenticationHelper.tryGetUser(httpHeaders);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        try {
            if (!dto.getUsername().isBlank()) {
                recipient = userService.getByUsername(dto.getUsername());
            } else if (!dto.getEmail().isBlank()) {
                recipient = userService.getByEmail(dto.getEmail());
            } else if (!dto.getPhoneNumber().isBlank()) {
                recipient = userService.getByPhoneNumber(dto.getPhoneNumber());
            }
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

        TransactionsInternal transactionsInternal = transactionsInternalMapper.createDto(sender, recipient, dto);
        return service.create(transactionsInternal);
    }


    @GetMapping("/outgoing")
    public List<TransactionsInternal> getOutgoing(@RequestHeader HttpHeaders httpHeaders) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return service.getOutgoing(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/incoming")
    public List<TransactionsInternal> getIncoming(@RequestHeader HttpHeaders httpHeaders) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return service.getIncoming(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
