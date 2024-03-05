package org.example.virtual_wallet.services;

import org.example.virtual_wallet.enums.RoleType;
import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.filters.UserFilterOptions;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.UserRepository;
import org.example.virtual_wallet.services.contracts.RoleService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public List<User> getAllFiltered(UserFilterOptions userFilterOptions) {
        return userRepository.getAllFiltered(userFilterOptions);
    }

    @Override
    public List<Card> getAllUserCards(int userId) {
        return userRepository.getAllUserCards(userId);
    }


    public void create(User user) {
        checkIfUsernameExist(user);
        checkIfEmailExist(user);
        checkIfPhoneNumberExist(user);
        user.setPassword(user.getPassword());
        user.setCreationDate(new Timestamp(System.currentTimeMillis()));
        userRepository.create(user);
    }

    @Override
    public void update(User user) {
        checkIfEmailExist(user);
        checkIfPhoneNumberExist(user);
        userRepository.update(user);
    }

    @Override
    public void delete(int userId) {
        userRepository.delete(userId);
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        return userRepository.getByPhoneNumber(phoneNumber);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public void addUserToContactList(User owner, User toAdd) {
        try {
            getById(toAdd.getId());
            owner.getContactLists().add(toAdd);
            userRepository.update(owner);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User", toAdd.getId());
        }

    }

    public void removeUserFromContactList(User owner, User toRemove) {
        try {
            getById(toRemove.getId());
            owner.getContactLists().remove(toRemove);
            userRepository.update(owner);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User", toRemove.getId());
        }

    }

    @Override
    public User blockUserByAdmin(User userToBlock, User admin) {
        try{
            getById(userToBlock.getId());
        }catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User", userToBlock.getId());
        }
        roleService.assignRoleToUser(userToBlock,RoleType.BANNED);
        userRepository.update(userToBlock);
        return userToBlock;

    }

    @Override
    public User unblockUserByAdmin(User userToUnblock, User admin) {
        try{
            getById(userToUnblock.getId());
        }catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User", userToUnblock.getId());
        }
        roleService.assignRoleToUser(userToUnblock,RoleType.REGULAR);
        userRepository.update(userToUnblock);
        return userToUnblock;
    }

    @Override
    public void promoteUserToAdmin(User user) {
        try{
            getById(user.getId());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User", user.getId());
        }
        roleService.assignRoleToUser(user, RoleType.ADMIN);
    }

    private void checkIfEmailExist(User user) {
        boolean duplicateExists = true;
        try {
            User existingMail = userRepository.getByEmail(user.getEmail());
            if (existingMail.getId() == user.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateException("User", "e-mail", user.getEmail());
        }

    }

    private void checkIfUsernameExist(User user) {
        boolean duplicateExists = true;
        try {
            User existingUsername = userRepository.getByUsername(user.getUsername());
            if (existingUsername.getId() == user.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }
    }

    private void checkIfPhoneNumberExist(User user) {
        boolean duplicateExists = true;
        try {
            User existingNumber = userRepository.getByPhoneNumber(user.getPhoneNumber());
            if (existingNumber.getId() == user.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateException("User", "phone number", user.getPhoneNumber());
        }
    }
}
