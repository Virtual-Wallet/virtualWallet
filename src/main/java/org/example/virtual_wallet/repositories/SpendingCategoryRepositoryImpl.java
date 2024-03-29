package org.example.virtual_wallet.repositories;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.SpendingCategoryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SpendingCategoryRepositoryImpl extends AbstractCRUDRepository<SpendingCategory> implements SpendingCategoryRepository {

    @Autowired
    public SpendingCategoryRepositoryImpl(SessionFactory sessionFactory) {
        super(SpendingCategory.class, sessionFactory);
    }


    @Override
    public SpendingCategory getByCategory(String category) {
        try (Session session = sessionFactory.openSession()) {
            Query<SpendingCategory> query = session
                    .createQuery("SELECT s FROM SpendingCategory s WHERE s.name = :category AND s.isDeleted = false",
                            SpendingCategory.class)
                    .setParameter("category", category);
            List<SpendingCategory> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Category", "name", category);
            }
            return result.get(0);
        }
    }

    @Override
    public List<SpendingCategory> getAllUserCategories(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<SpendingCategory> query = session
                    .createQuery("from SpendingCategory WHERE creator.id = :id AND isDeleted = false",
                            SpendingCategory.class)
                    .setParameter("id", user.getId());
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException(String.format("No Categories under User with ID %s", user.getId()));
            }
            return query.list();
        }
    }
    @Override
    public SpendingCategory getByCategoryAndUser(String name, User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<SpendingCategory> query = session
                    .createQuery("SELECT s FROM SpendingCategory s WHERE s.creator.id = :id " +
                                    "AND s.name = :name",
                            SpendingCategory.class)
                    .setParameter("id", user.getId())
                    .setParameter("name", name);
            List<SpendingCategory> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Category", "name", name);
            }
            return result.get(0);
        }
    }

}
