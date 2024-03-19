package org.example.virtual_wallet.repositories;

import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.filters.TransactionFilterOptions;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.TransactionsInternalRepository;
import org.example.virtual_wallet.repositories.contracts.WalletRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

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

    @Override
    public List<TransactionsInternal> getFilteredIncoming(TransactionFilterOptions filterOptions, User user) {

        int walletId = user.getWallet().getId();

        try (Session session = sessionFactory.openSession()) {

            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getSenderWalletId().ifPresent(value -> {
                filters.add("sender_wallet_id =: senderWalletId");
                params.put("senderWalletId", value);
            });

            filterOptions.getTimestamp().ifPresent(value -> {
                filters.add("timestamp =: timestamp");
                params.put("timestamp", value);
            });

            filterOptions.getAmount().ifPresent(value -> {
                filters.add("amount =: amount");
                params.put("amount", value);
            });

            StringBuilder queryString = new StringBuilder("from TransactionsInternal");
            if (!filters.isEmpty()) {
                queryString
                        .append(" WHERE ")
                        .append(String.join(" AND ", filters));
            }
            queryString.append(generateOrderBy(filterOptions));

            Query<TransactionsInternal> query = session.createQuery(
                    "FROM TransactionsInternal WHERE recipientWallet.id =:walletId", TransactionsInternal.class);
            query.setParameter("walletId", walletId);
            return query.list();
        }
    }

    @Override
    public List<TransactionsInternal> getFilteredOutgoing(TransactionFilterOptions filterOptions, User user) {
        int walletId = user.getWallet().getId();

        try (Session session = sessionFactory.openSession()) {

            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getRecipientWalletId().ifPresent(value -> {
                filters.add("recipient_wallet_id =: recipientWalletId");
                params.put("recipientWalletId", value);
            });

            filterOptions.getTimestamp().ifPresent(value -> {
                filters.add("timestamp =: timestamp");
                params.put("timestamp", value);
            });

            filterOptions.getAmount().ifPresent(value -> {
                filters.add("amount =: amount");
                params.put("amount", value);
            });

            StringBuilder queryString = new StringBuilder("from TransactionsInternal");
            if (!filters.isEmpty()) {
                queryString
                        .append(" WHERE ")
                        .append(String.join(" AND ", filters));
            }
            queryString.append(generateOrderBy(filterOptions));

            Query<TransactionsInternal> query = session.createQuery(
                    "FROM TransactionsInternal WHERE senderWalletId =:walletId", TransactionsInternal.class);
            query.setParameter("walletId", walletId);
            return query.list();
        }
    }

    private String generateOrderBy(TransactionFilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (filterOptions.getSortBy().get()) {
            case "name":
                orderBy = "name";
                break;
            case "abv":
                orderBy = "abv";
                break;
            case "style":
                orderBy = "style.name";
                break;
            default:
                return "";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }
}