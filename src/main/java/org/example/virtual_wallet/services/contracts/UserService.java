package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.filters.UserFilterOptions;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<User> getAll(User user);
    List<User> getAll();
    Page<User> findPage(List<User> filteredList, Pageable pageable);

    List<User> getAllFiltered(UserFilterOptions userFilterOptions,User user);

    List<Card> getAllUserCards(int userId,User executor);

    User getById(int id);

    User getByUsername(String username);

    User getByPhoneNumber(String phoneNumber);

    User getByEmail(String email);

    User unblockUserByAdmin(User userToUnblock, User executor);

    User blockUserByAdmin(User userToBlock, User executor);
    void advanceAccountStatus(User user);
    void revertAccountStatus(User user);

    void create(User user);

    void update(User user);

    void delete(int userId);

    void addUserToContactList(User userId, User contactId);

    void removeUserFromContactList(User owner, User toRemove);
    void promoteUserToAdmin(User toPromote,User admin);
    User getByUserInput(String userInput);
}
