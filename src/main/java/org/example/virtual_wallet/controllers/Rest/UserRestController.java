package org.example.virtual_wallet.controllers.Rest;

import jakarta.validation.Valid;
import org.example.virtual_wallet.enums.RoleType;
import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.exceptions.InvalidOperationException;
import org.example.virtual_wallet.filters.UserFilterOptions;
import org.example.virtual_wallet.helpers.mappers.UserMapper;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.Role;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.UserDto;
import org.example.virtual_wallet.repositories.contracts.RoleRepository;
import org.example.virtual_wallet.services.contracts.CardService;
import org.example.virtual_wallet.services.contracts.RoleService;
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
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    @Autowired
    public UserRestController(UserService userService, UserMapper userMapper, CardService cardService, RoleRepository roleRepository, RoleService roleService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.cardService = cardService;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
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
    public List<Card> getAllUserCards(@PathVariable int userId) {
        User user = userService.getById(userId);
        return cardService.getUserCards(user, user);
    }

    @PostMapping("/{userId}/add/{toAddId}")
    public void addToContactList(@PathVariable int userId, @PathVariable int toAddId) {
        try {
            User user = userService.getById(userId);
            User userToAdd = userService.getById(toAddId);
            userService.addUserToContactList(user, userToAdd);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/{userId}/remove/{toAddId}")
    public void removeFromContactList(@PathVariable int userId, @PathVariable int toAddId) {
        try {
            User user = userService.getById(userId);
            User userToAdd = userService.getById(toAddId);
            userService.removeUserFromContactList(user, userToAdd);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{userId}/block")
    public void blockUser(@PathVariable int userId) {
        try {
            User dummyUser = userService.getById(1);
            User toBlock = userService.getById(userId);
            userService.blockUserByAdmin(toBlock, dummyUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (InvalidOperationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }

    @PutMapping("/{userId}/active")
    public void unblockUser(@PathVariable int userId) {
        try {
            User dummyUser = userService.getById(1);
            User toUnBlock = userService.getById(userId);
            userService.unblockUserByAdmin(toUnBlock, dummyUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (InvalidOperationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }

    @PutMapping("/{userId}/promote")
    public void promoteToAdmin(@PathVariable int userId) {
        try {
            User admin = userService.getById(userId);
            userService.promoteUserToAdmin(admin);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
