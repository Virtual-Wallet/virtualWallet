package org.example.virtual_wallet.repositories.contracts;

import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.User;

import java.util.List;

public interface SpendingCategoryRepository {
    void create(SpendingCategory category);
    void update(SpendingCategory category);
    SpendingCategory getByCategoryAndUser(String name, User user);

    void delete(int id);
    SpendingCategory getById(int id);

    List<SpendingCategory> getAll();
    SpendingCategory getByCategory(String category);

    List<SpendingCategory> getAllUserCategories(User user);



}
