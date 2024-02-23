package org.example.virtual_wallet.repositorys.contracts;

import org.example.virtual_wallet.filters.UserFilterOptions;
import org.example.virtual_wallet.models.User;

import java.util.List;

public interface UserRepository {
    List<User> getAllFiltered(UserFilterOptions userFilterOptions);

    User getById(int id);
    User getByUsername(String username);
    User getByPhoneNumber(String phoneNumber);

    User getByEmail(String email);

    void create(User user);

    void update(User user);
    void delete(int userId);

//    User block(User user);
//
//    User unblock(User user);
}
