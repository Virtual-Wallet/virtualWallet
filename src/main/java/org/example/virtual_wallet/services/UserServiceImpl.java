package org.example.virtual_wallet.services;

import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.filters.UserFilterOptions;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.UserRepository;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public List<User> getAllFiltered(UserFilterOptions userFilterOptions) {
        return userRepository.getAllFiltered(userFilterOptions);
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

    private void checkIfEmailExist(User user) {
        boolean duplicateExists = true;
        try {
            userRepository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (user.getEmail().equals(user.getEmail())) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "e-mail", user.getEmail());
        }

    }

    private void checkIfUsernameExist(User user) {
        boolean duplicateExists = true;
        try {
            userRepository.getByUsername(user.getUsername());
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
            userRepository.getByPhoneNumber(user.getPhoneNumber());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (user.getPhoneNumber().equals(user.getPhoneNumber())) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "phone number", user.getPhoneNumber());
        }
    }
}
