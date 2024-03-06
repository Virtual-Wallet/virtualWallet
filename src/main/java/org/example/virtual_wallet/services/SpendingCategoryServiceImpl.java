package org.example.virtual_wallet.services;

import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.SpendingCategoryRepository;
import org.example.virtual_wallet.services.contracts.SpendingCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpendingCategoryServiceImpl implements SpendingCategoryService {
    private final SpendingCategoryRepository repository;

    @Autowired
    public SpendingCategoryServiceImpl(SpendingCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void create(SpendingCategory category, User user) {
        if (verifyUniqueCategory(category.getName())){
            throw new EntityDuplicateException("The category already exists!");
        }
        category.setCreator(user);
        repository.create(category);
    }

    private boolean verifyUniqueCategory(String category){
        boolean isExisting = true;
        try {
            repository.getByCategory(category);
        } catch (EntityNotFoundException e){
            isExisting = false;
        }
        return isExisting;
    }
    @Override
    public void update(SpendingCategory category, User user) {
        // todo helper for admin and creator
        repository.update(category);
    }

    @Override
    public void delete(int id, User user) {
        // todo helper for admin
        // todo verifaction for any transactions under that category
        SpendingCategory category = getById(id);
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
}
