package org.example.virtual_wallet.controllers.Rest;

import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.CategoryMapper;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.CardDto;
import org.example.virtual_wallet.models.dtos.CategoryDto;
import org.example.virtual_wallet.services.contracts.SpendingCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class SpendingCategoryRestController {

    private final SpendingCategoryService service;
    private final CategoryMapper mapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public SpendingCategoryRestController(SpendingCategoryService service, CategoryMapper mapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.mapper = mapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<SpendingCategory> getAll(@RequestHeader HttpHeaders httpHeaders){
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return service.getAll();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public SpendingCategory getById(@PathVariable int id, @RequestHeader HttpHeaders httpHeaders) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/user/{userId}")
    public List<SpendingCategory> getAllUserCategories(@PathVariable int userId,
            @RequestHeader HttpHeaders httpHeaders){
        try {
            User requester = authenticationHelper.tryGetUser(httpHeaders);
            User user = authenticationHelper.getById(userId);
            return service.getAllUserCategories(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @PostMapping
    public SpendingCategory create(@RequestHeader HttpHeaders httpHeaders, @Valid @RequestBody CategoryDto dto) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            SpendingCategory category = mapper.createCategoryDto(dto);
            service.create(category, user);
            return category;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
// todo think do we need to update category
//    @PutMapping("/{id}")
//    public SpendingCategory update(@PathVariable int id, @RequestHeader HttpHeaders headers,
//                                   @Valid @RequestBody CategoryDto dto) {
//        try {
//            User user = authenticationHelper.tryGetUser(headers);
//            Card card = cardModelMapper.dtoCardUpdate(dto, id);
//            cardService.update(card, user);
//            return cardModelMapper.toCardDto(card);
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
            service.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
