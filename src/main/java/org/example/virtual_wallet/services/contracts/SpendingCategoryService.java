package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.User;

import java.util.List;

public interface SpendingCategoryService {
    void create(SpendingCategory category, User user);

    void update(SpendingCategory category, User user);

    void delete(int id, User user);

    SpendingCategory getById(int id);

    List<SpendingCategory> getAll();

    SpendingCategory getByCategory(String category);

    List<SpendingCategory> getAllUserCategories(User user);

}
