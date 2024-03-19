package org.example.virtual_wallet.repositories.contracts;

import org.example.virtual_wallet.filters.UserFilterOptions;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;

import java.util.List;

public interface UserRepository extends BaseCRUDRepository<User> {
    List<User> getAllFiltered(UserFilterOptions userFilterOptions);

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    User getByPhoneNumber(String phoneNumber);

    User getByEmail(String email);

    void create(User user);

    void update(User user);

    void delete(int userId);

    List<Card> getAllUserCards(int userId);
    User getByUserInput(String userInput);

}
