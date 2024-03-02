package org.example.virtual_wallet.controllers.Rest;

import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.filters.UserFilterOptions;
import org.example.virtual_wallet.helpers.mappers.UserMapper;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.UserDto;
import org.example.virtual_wallet.services.contracts.CardService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final CardService cardService;

    @Autowired
    public UserRestController(UserService userService, UserMapper userMapper, CardService cardService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.cardService = cardService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/search")
    public List<User> getFiltered(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        UserFilterOptions userFilterOptions = new UserFilterOptions(username, phoneNumber, email, sortBy, sortOrder);
        return userService.getAllFiltered(userFilterOptions);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        return userService.getById(id);
    }

    @PostMapping
    public void create(@Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.dtoUserCreate(userDto);
            userService.create(user);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.updateUser(id, userDto);
            userService.update(user);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/{userId}/cards")
    public List<Card> getAllUserCards(@PathVariable int userId){
        User user = userService.getById(userId);
        return cardService.getUserCards(user);
    }


}
