package org.example.virtual_wallet.controllers.Rest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.exceptions.InvalidOperationException;
import org.example.virtual_wallet.exceptions.UnauthorizedOperationException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.SpendingCategoryMapper;
import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.CategoryDto;
import org.example.virtual_wallet.services.contracts.SpendingCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class SpendingCategoryRestController {

    private final SpendingCategoryService service;
    private final SpendingCategoryMapper mapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public SpendingCategoryRestController(SpendingCategoryService service, SpendingCategoryMapper mapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.mapper = mapper;
        this.authenticationHelper = authenticationHelper;
    }

    @Operation(summary = "Get all spending categories", description = "Retrieve a list of all spending categories for the authenticated user.")
    @GetMapping
    public List<SpendingCategory> getAll(@RequestHeader(name = "Credentials") String credentials) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            return service.getAll();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Get all transactions for a spending category", description = "Retrieve a list of all transactions for the specified spending category.")
    @GetMapping("/{id}/transactions")
    public List<TransactionsInternal> getAllTransactions(@PathVariable int id, @RequestHeader(name = "Credentials") String credentials) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            return service.getAllTransactionsPerCategories(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Get spending category by ID", description = "Retrieve a spending category by its ID.")
    @GetMapping("/{id}")
    public SpendingCategory getById(@PathVariable int id, @RequestHeader(name = "Credentials") String credentials) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Get all spending categories for a user", description = "Retrieve a list of all spending categories for the specified user.")
    @GetMapping("/user/{userId}")
    public List<SpendingCategory> getAllUserCategories(@PathVariable int userId, @RequestHeader(name = "Credentials") String credentials) {
        try {
            User requester = authenticationHelper.tryGetUser(credentials);
            User user = authenticationHelper.getById(userId);
            return service.getAllUserCategories(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Create a new spending category", description = "Create a new spending category.")
    @PostMapping
    public SpendingCategory create(@RequestHeader(name = "Credentials") String credentials, @Valid @RequestBody CategoryDto dto) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            SpendingCategory category = mapper.createCategoryDto(dto);
            service.create(category, user);
            return category;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Update a spending category by ID", description = "Update a spending category by its ID.")
    @PutMapping("/{id}")
    public SpendingCategory update(@PathVariable int id, @RequestHeader(name = "Credentials") String credentials, @Valid @RequestBody CategoryDto dto) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            SpendingCategory category = mapper.dtoCategoryUpdate(dto, id);
            return service.update(category, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Delete a spending category by ID", description = "Delete a spending category by its ID.")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id, @RequestHeader(name = "Credentials") String credentials) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            service.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}