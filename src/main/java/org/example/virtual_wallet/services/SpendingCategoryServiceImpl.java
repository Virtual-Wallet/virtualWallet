package org.example.virtual_wallet.services;

import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.exceptions.InvalidOperationException;
import org.example.virtual_wallet.exceptions.UnauthorizedOperationException;
import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.CategoryDto;
import org.example.virtual_wallet.repositories.contracts.SpendingCategoryRepository;
import org.example.virtual_wallet.services.contracts.SpendingCategoryService;
import org.example.virtual_wallet.services.contracts.TransactionsInternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpendingCategoryServiceImpl implements SpendingCategoryService {
    private final SpendingCategoryRepository repository;
    private final TransactionsInternalService transactionsInternalService;

    @Autowired
    public SpendingCategoryServiceImpl(SpendingCategoryRepository repository, TransactionsInternalService transactionsInternalService) {
        this.repository = repository;
        this.transactionsInternalService = transactionsInternalService;
    }

    @Override
    public void create(SpendingCategory category, User user) {
        try {
            getByCategoryAndUser(category.getName(), user);
            throw new EntityDuplicateException("There is already defined category with that name");
        } catch (EntityNotFoundException e) {
            category.setCreator(user);
            repository.create(category);
        }
    }

    @Override
    public SpendingCategory update(SpendingCategory category, User user) {
//        checkCreator(category.getSpendingCategoryId(), user);
        repository.update(category);
        return category;
    }

    @Override
    public SpendingCategory getByCategoryAndUser(String category, User user) {
        return repository.getByCategoryAndUser(category, user);
    }

    @Override
    public void delete(int id, User user) {
        SpendingCategory category = getById(id);
//        checkCreator(id, user);
        checkAnyTransactions(id, user);
        category.setDeleted(true);
        repository.update(category);
    }

    @Override
    public SpendingCategory getById(int id) {
        return repository.getById(id);
    }

    @Override
    public List<SpendingCategory> getAll() {
        return repository.getAll();
    }

    @Override
    public SpendingCategory getByCategory(String category) {
        return repository.getByCategory(category);
    }

    @Override
    public List<SpendingCategory> getAllUserCategories(User user) {
        return repository.getAllUserCategories(user);
    }

    @Override
    public List<TransactionsInternal> getAllTransactionsPerCategories(int categoryID, User user) {
        return transactionsInternalService.getOutgoingPerCategory(categoryID, user);
    }

    private void checkCreator(int categoryId, User user) {
        if (categoryId != user.getId()) {
            throw new UnauthorizedOperationException("You cannot modify the category, as you are not the creator!");
        }
    }

    private void checkAnyTransactions(int categoryId, User user) {
        try {
            transactionsInternalService.getOutgoingPerCategory(categoryId, user);
            throw new InvalidOperationException("You cannot delete the category. There is associated outgoing transactions!");
        } catch (EntityNotFoundException e) {
            return;
        }

    }

}
