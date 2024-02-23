package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.models.User;

public interface UserService {
     void create(User user);
        void update(User user);
        void delete(int userId);
        User getById(int id);
        User getByUsername(String username);
        User getByPhoneNumber(String phoneNumber);
        User getByEmail(String email);

}
