package org.example.virtual_wallet.repositories;

import org.example.virtual_wallet.models.TransactionsExternal;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.TransactionsExternalRepository;
import org.example.virtual_wallet.repositories.contracts.WalletRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionsExternalRepositoryImpl extends AbstractCRUDRepository<TransactionsExternal> implements TransactionsExternalRepository {

    @Autowired
    protected TransactionsExternalRepositoryImpl(SessionFactory sessionFactory,
                                                 WalletRepository walletRepository) {
        super(TransactionsExternal.class, sessionFactory);
    }

    @Override
    public List<TransactionsExternal> getDeposits(User user) {
        int userId = user.getId();
        try (Session session = sessionFactory.openSession()) {
            Query<TransactionsExternal> query = session.createQuery(
                    "FROM TransactionsInternal WHERE id =:userId", TransactionsExternal.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public List<TransactionsExternal> getWithdrawals(User user) {
        int userId = user.getId();
        try (Session session = sessionFactory.openSession()) {
            Query<TransactionsExternal> query = session.createQuery(
                    "FROM TransactionsInternal WHERE id =:userId", TransactionsExternal.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }
}