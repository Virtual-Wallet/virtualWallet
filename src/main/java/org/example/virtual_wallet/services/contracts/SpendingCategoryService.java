package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.CategoryDto;

import java.util.List;

public interface SpendingCategoryService {
    void create(SpendingCategory category, User user);

    SpendingCategory update(SpendingCategory category, User user);
    SpendingCategory getByCategoryAndUser(String dto, User user);
    void delete(int id, User user);

    SpendingCategory getById(int id);

    List<SpendingCategory> getAll();

    SpendingCategory getByCategory(String category);

    List<SpendingCategory> getAllUserCategories(User user);
    List<TransactionsInternal> getAllTransactionsPerCategories(int categoryID, User user);

}
