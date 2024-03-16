package org.example.virtual_wallet.repositories;

import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.TransactionsInternalRepository;
import org.example.virtual_wallet.repositories.contracts.WalletRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionsInternalRepositoryImpl extends AbstractCRUDRepository<TransactionsInternal> implements TransactionsInternalRepository {

    @Autowired
    protected TransactionsInternalRepositoryImpl(SessionFactory sessionFactory,
                                                 WalletRepository walletRepository) {
        super(TransactionsInternal.class, sessionFactory);
    }

    @Override
    public List<TransactionsInternal> getIncoming(User user) {
        int walletId = user.getWallet().getId();
        try (Session session = sessionFactory.openSession()) {
            Query<TransactionsInternal> query = session.createQuery(
                    "FROM TransactionsInternal WHERE recipientWallet.id =:walletId", TransactionsInternal.class);
            query.setParameter("walletId", walletId);
            return query.list();
        }
    }

    @Override
    public List<TransactionsInternal> getOutgoing(User user) {
        int walletId = user.getWallet().getId();
        try (Session session = sessionFactory.openSession()) {
            Query<TransactionsInternal> query = session.createQuery(
                    "FROM TransactionsInternal WHERE senderWalletId =:walletId", TransactionsInternal.class);
            query.setParameter("walletId", walletId);
            return query.list();
        }
    }

    @Override
    public List<TransactionsInternal> getIncomingPerCategory(int categoryId, User user) {
        int walletId = user.getWallet().getId();

        try (Session session = sessionFactory.openSession()) {
            Query<TransactionsInternal> query = session.createQuery(
                    "FROM TransactionsInternal WHERE spendingCategory.id =:categoryId and senderWalletId =:walletId", TransactionsInternal.class);
            query.setParameter("categoryId", categoryId);
            query.setParameter("walletId", walletId);
            return query.list();
        }
    }

    @Override
    public List<TransactionsInternal> getOutgoingPerCategory(int categoryId, User user) {
        int walletId = user.getWallet().getId();

        try (Session session = sessionFactory.openSession()) {
            Query<TransactionsInternal> query = session.createQuery(
                    "FROM TransactionsInternal WHERE spendingCategory.id =:categoryId and recipientWallet.id =:walletId", TransactionsInternal.class);
            query.setParameter("categoryId", categoryId);
            query.setParameter("walletId", walletId);

            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("No transactions under that category!");
            }

            return query.list();
        }
    }


}
