package org.example.virtual_wallet.controllers.Rest;

import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.TransactionsExternalMapper;
import org.example.virtual_wallet.models.TransactionsExternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.services.contracts.TransactionsExternalService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/transfers")
public class TransfersRestController {
    private final TransactionsExternalService service;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final TransactionsExternalMapper transactionsExternalMapper;

    @Autowired
    public TransfersRestController(TransactionsExternalService service,
                                   UserService userService,
                                   AuthenticationHelper authenticationHelper,
                                   TransactionsExternalMapper transactionsExternalMapper) {
        this.service = service;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.transactionsExternalMapper = transactionsExternalMapper;
    }


    @

            GetMapping("/deposits")
    public List<TransactionsExternal> getDeposits(@RequestHeader HttpHeaders httpHeaders) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return service.getDeposits(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    public List<TransactionsExternal> getWithdrawals(@RequestHeader HttpHeaders httpHeaders) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return service.getWithdrawals(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
