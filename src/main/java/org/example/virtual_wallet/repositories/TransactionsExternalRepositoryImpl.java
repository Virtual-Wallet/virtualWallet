package org.example.virtual_wallet.repositories;

import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.filters.TransferFilterOptions;
import org.example.virtual_wallet.models.TransactionsExternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.TransactionsExternalRepository;
import org.example.virtual_wallet.repositories.contracts.WalletRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionsExternalRepositoryImpl extends AbstractCRUDRepository<TransactionsExternal> implements TransactionsExternalRepository {

    @Autowired
    protected TransactionsExternalRepositoryImpl(SessionFactory sessionFactory,
                                                 WalletRepository walletRepository) {
        super(TransactionsExternal.class, sessionFactory);
    }

    @Override
    public List<TransactionsExternal> getDeposits(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<TransactionsExternal> query = session.createQuery(
                    "FROM TransactionsExternal e WHERE e.user.id =:userId AND e.type ='DEPOSIT'", TransactionsExternal.class);
            query.setParameter("userId", user.getId());
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("There are no withdrawals to show!");
            }
            return query.list();
        }
    }

    @Override
    public List<TransactionsExternal> getWithdrawals(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<TransactionsExternal> query = session.createQuery(
                    "FROM TransactionsExternal e WHERE e.user.id =:userId AND e.type ='WITHDRAWAL'", TransactionsExternal.class);
            query.setParameter("userId", user.getId());
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("There are no deposits to show!");
            }
            return query.list();
        }
    }

    @Override
    public List<TransactionsExternal> getFiltered(TransferFilterOptions filterOptions, User user) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getType().ifPresent(value -> {
                filters.add("type LIKE :type");
                params.put("type", String.format("%%%s%%", value));
            });

            filterOptions.getType().ifPresent(value -> {
                filters.add("card.id LIKE :card");
                params.put("cardId", String.format("%%%s%%", value));
            });

            filterOptions.getType().ifPresent(value -> {
                filters.add("currency LIKE :currency");
                params.put("currency", String.format("%%%s%%", value));
            });

            filterOptions.getType().ifPresent(value -> {
                filters.add("timestamp LIKE :timestamp");
                params.put("timestamp", String.format("%%%s%%", value));
            });

            filterOptions.getType().ifPresent(value -> {
                filters.add("amount LIKE :amount");
                params.put("amount", String.format("%%%s%%", value));
            });

            StringBuilder queryString = new StringBuilder("FROM TransactionsExternal");
            if (!filters.isEmpty()) {
                queryString
                        .append(" WHERE ")
                        .append(String.join(" AND ", filters));
            }
            queryString.append(generateOrderBy(filterOptions));

            Query<TransactionsExternal> query = session.createQuery(queryString.toString(), TransactionsExternal.class);
            query.setProperties(params);
            return query.list();
        }
    }

    private String generateOrderBy(TransferFilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (filterOptions.getSortBy().get()) {
            case "type":
                orderBy = "type";
                break;
            case "timestamp":
                orderBy = "timestamp";
                break;
            case "amount":
                orderBy = "amount";
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