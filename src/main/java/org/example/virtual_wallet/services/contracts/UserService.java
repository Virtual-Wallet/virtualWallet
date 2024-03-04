package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.filters.UserFilterOptions;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    List<User> getAllFiltered(UserFilterOptions userFilterOptions);

    List<Card> getAllUserCards(int userId);

    void create(User user);

    void update(User user);

    void delete(int userId);

    User getById(int id);

    User getByUsername(String username);

    User getByPhoneNumber(String phoneNumber);

    User getByEmail(String email);

    void addUserToContactList(User userId, User contactId);

    void removeUserFromContactList(User owner, User toRemove);
}
