package org.example.virtual_wallet.repositories;


import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.repositories.contracts.BaseReadRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.lang.String.format;
@Repository
public abstract class AbstractReadRepository<T> implements BaseReadRepository<T> {
    private final Class<T> clazz;
    protected final SessionFactory sessionFactory;
    @Autowired
    public AbstractReadRepository(Class<T> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<T> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(format("from %s", clazz.getName()), clazz).list();
        }
    }

    @Override
    public T getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            T entity = session.get(clazz, id);
            if (entity == null) {
                throw new EntityNotFoundException(clazz.getSimpleName(),id);
            }
            return entity;
        }
    }

    @Override
    public <V> T getByField(String name, V value) {
        final String query = format("from %s where %s =:value", clazz.getName(), name);
        final String notFountErrorMessage = format("%s with %s %s was not found!", clazz.getSimpleName(), name, value);

        try (Session session = sessionFactory.openSession()) {
            return session
                    .createQuery(query, clazz)
                    .setParameter("value", value)
                    .uniqueResultOptional()
                    .orElseThrow(() -> new EntityNotFoundException(notFountErrorMessage));
        }
    }
}
