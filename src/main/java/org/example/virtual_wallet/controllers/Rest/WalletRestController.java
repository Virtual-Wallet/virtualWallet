package org.example.virtual_wallet.controllers.Rest;

import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.WalletMapper;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.Wallet;
import org.example.virtual_wallet.models.dtos.CardDto;
import org.example.virtual_wallet.models.dtos.WalletDto;
import org.example.virtual_wallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wallets")
public class WalletRestController {
    private final WalletService walletService;
    private final AuthenticationHelper authenticationHelper;
    private final WalletMapper walletMapper;

    @Autowired
    public WalletRestController(WalletService walletService,
                                AuthenticationHelper authenticationHelper,
                                WalletMapper walletMapper) {
        this.walletService = walletService;
        this.authenticationHelper = authenticationHelper;
        this.walletMapper = walletMapper;
    }
    @GetMapping
    public List<Wallet> getAll(@RequestHeader HttpHeaders httpHeaders){
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return walletService.getAllWallets();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Wallet getById(@PathVariable int id, @RequestHeader HttpHeaders httpHeaders) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return walletService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Wallet create(@RequestHeader HttpHeaders httpHeaders, @Valid @RequestBody WalletDto dto) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            Wallet wallet = walletMapper.dtoWalletCreate(dto, user);
            walletService.create(wallet, user);
            return wallet;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
//    @PutMapping("/{id}")
//    public WalletDto update(@PathVariable int id, @RequestHeader HttpHeaders headers, @Valid @RequestBody WalletDto dto) {
//        try {
//            User user = authenticationHelper.tryGetUser(headers);
//            Wallet wallet = walletMapper.dtoWalletUpdate(dto, id);
//            walletService.update(wallet, user);
//            return walletMapper.toWalletDto(wallet);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            walletService.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
