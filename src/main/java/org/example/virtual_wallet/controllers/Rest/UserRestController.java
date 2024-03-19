package org.example.virtual_wallet.controllers.Rest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.*;
import org.example.virtual_wallet.filters.UserFilterOptions;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.UserMapper;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.UserDto;
import org.example.virtual_wallet.services.contracts.CardService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final CardService cardService;
    private final AuthenticationHelper authenticationHelper;


    @Autowired
    public UserRestController(UserService userService, UserMapper userMapper, CardService cardService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.cardService = cardService;
        this.authenticationHelper = authenticationHelper;
    }
    @Operation(summary = "Get all users", description = "Retrieve a list of all users.")
    @GetMapping
    public List<User> getAll( @RequestHeader(name = "Credentials") String credentials) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            return userService.getAll(user);
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @Operation(summary = "Filter users", description = "Filter users based on given criteria.")
    @GetMapping("/search")
    public List<User> getFiltered( @RequestHeader(name = "Credentials") String credentials,
                                  @RequestParam(required = false) String username,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String phoneNumber,
                                  @RequestParam(required = false) String sortBy,
                                  @RequestParam(required = false) String sortOrder,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size) {

        UserFilterOptions userFilterOptions = new UserFilterOptions(username, phoneNumber, email, sortBy, sortOrder);

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(2);

        try {
            User user = authenticationHelper.tryGetUser(credentials);
            List<User> filteredList = userService.getAllFiltered(userFilterOptions, user);
            Page<User> usersPage = userService.findPage(filteredList, PageRequest.of(currentPage - 1, pageSize));
            int totalPages = usersPage.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
            }
            return usersPage.stream().toList();
//            return userService.getAllFiltered(userFilterOptions, user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID.")
    @GetMapping("/{id}")
    public User getById( @RequestHeader(name = "Credentials") String credentials, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);
            return userService.getById(id);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Create a new user", description = "Create a new user.")
    @PostMapping
    public void create(@Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.dtoUserCreate(userDto);
            userService.create(user);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Operation(summary = "Update a user", description = "Update an existing user.")
    @PutMapping("/{id}")
    public void update( @RequestHeader(name = "Credentials") String credentials, @PathVariable int id, @Valid @RequestBody UserDto userDto) {
        try {
            authenticationHelper.tryGetUser(credentials);
            User user = userMapper.updateUser(id, userDto);
            userService.update(user);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Get all cards for a user", description = "Retrieve all cards associated with a user.")
    @GetMapping("/{userId}/cards")
    public List<Card> getAllUserCards( @RequestHeader(name = "Credentials") String credentials, @PathVariable int userId) {
        try {
            User executor = authenticationHelper.tryGetUser(credentials);
            User user = userService.getById(userId);

            return userService.getAllUserCards(user.getId(), executor);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Add user to contact list", description = "Add another user to the contact list of the authenticated user.")
    @PostMapping("/add/{toAddId}")
    public void addToContactList( @RequestHeader(name = "Credentials") String credentials, @PathVariable int toAddId) {
        try {

            User user = authenticationHelper.tryGetUser(credentials);

            User userToAdd = userService.getById(toAddId);

            userService.addUserToContactList(user, userToAdd);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Remove user from contact list", description = "Remove a user from the contact list of the authenticated user.")
    @PostMapping("/remove/{toAddId}")
    public void removeFromContactList( @RequestHeader(name = "Credentials") String credentials, @PathVariable int toAddId) {
        try {
            User user = authenticationHelper.tryGetUser(credentials);

            User toRemove = userService.getById(toAddId);
            userService.removeUserFromContactList(user, toRemove);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Block user", description = "Block a user by their ID.")
    @PutMapping("/{userId}/block")
    public void blockUser( @RequestHeader(name = "Credentials") String credentials, @PathVariable int userId) {
        try {
            User executor = authenticationHelper.tryGetUser(credentials);

            User toBlock = userService.getById(userId);
            userService.blockUserByAdmin(toBlock, executor);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Unblock user", description = "Unblock a user by their ID.")
    @PutMapping("/{userId}/active")
    public void unblockUser( @RequestHeader(name = "Credentials") String credentials, @PathVariable int userId) {
        try {
            User executor = authenticationHelper.tryGetUser(credentials);

            User toUnBlock = userService.getById(userId);
            userService.unblockUserByAdmin(toUnBlock, executor);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidOperationException | AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Promote user to admin", description = "Promote a user to admin by their ID.")
    @PutMapping("/{userId}/promote")
    public void promoteToAdmin( @RequestHeader(name = "Credentials") String credentials, @PathVariable int userId) {
        try {
            User admin = authenticationHelper.tryGetUser(credentials);
            User toPromote = userService.getById(userId);

            userService.promoteUserToAdmin(toPromote, admin);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidOperationException | AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Advance account status", description = "Advance the account status of a user by their ID.")
    @PutMapping("{userId}/advance")
    public void advanceAccStatus(@PathVariable int userId){
        try{
            User user = userService.getById(userId);
            userService.advanceAccountStatus(user);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }catch (InvalidOperationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    @Operation(summary = "Revert account status", description = "Revert the account status of a user by their ID.")
    @PutMapping("{userId}/revert")
    public void revertAccStatus(@PathVariable int userId){
        try{
            User user = userService.getById(userId);
            userService.revertAccountStatus(user);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }catch (InvalidOperationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
}
