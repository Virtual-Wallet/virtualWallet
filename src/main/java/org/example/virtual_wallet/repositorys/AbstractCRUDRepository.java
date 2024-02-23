package org.example.virtual_wallet.repositorys;

import org.example.virtual_wallet.repositorys.contracts.BaseCRUDRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractCRUDRepository<T> extends AbstractReadRepository<T> implements BaseCRUDRepository<T> {

    protected AbstractCRUDRepository(Class<T> clazz, SessionFactory sessionFactory) {
        super(clazz, sessionFactory);
    }

    @Override
    public void create(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        T toDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(toDelete);
            session.getTransaction().commit();

        }
    }
}