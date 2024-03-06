package org.example.virtual_wallet.repositories;

import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.Wallet;
import org.example.virtual_wallet.repositories.contracts.WalletRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WalletRepositoryImpl extends AbstractCRUDRepository<Wallet> implements WalletRepository {

    public WalletRepositoryImpl(SessionFactory sessionFactory) {
        super(Wallet.class, sessionFactory);
    }

    @Override
    public List<Wallet> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Wallet> query = session.createQuery("SELECT w FROM Wallet w WHERE w.isActive = true", Wallet.class);
            return query.list();
        }
    }

    @Override
    public Wallet getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Wallet wallet = session.createQuery(
                            "SELECT w FROM Wallet w WHERE w.id = :id AND w.isActive = true", Wallet.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (wallet == null) {
                throw new EntityNotFoundException("Wallet", id);
            }

            transaction.commit();
            return wallet;
        }
    }

    @Override
    public Wallet getWalletByUserId(int userId){
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Wallet wallet = session.createQuery(
                            "SELECT w FROM Wallet w WHERE w.user.id = :userId AND w.isActive = true", Wallet.class)
                    .setParameter("userId", userId)
                    .uniqueResult();

            if (wallet == null) {
                throw new EntityNotFoundException("Wallet", userId);
            }

            transaction.commit();
            return wallet;
        }
    }
}
