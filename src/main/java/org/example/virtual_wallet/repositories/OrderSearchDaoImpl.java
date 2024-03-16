package org.example.virtual_wallet.repositories;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.virtual_wallet.filters.OrderFilterOptions;
import org.example.virtual_wallet.models.TransactionsExternal;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.OrderFilterDto;
import org.example.virtual_wallet.repositories.contracts.OrderSearchDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderSearchDaoImpl implements OrderSearchDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public OrderSearchDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<OrderFilterDto> getFiltered(OrderFilterOptions orderFilterOptions,
                                            User user) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();

            CriteriaQuery<OrderFilterDto> depositQuery = cb.createQuery(OrderFilterDto.class);
            Root<TransactionsExternal> depositRoot = depositQuery.from(TransactionsExternal.class);

            depositQuery.select(cb.construct(OrderFilterDto.class,
                    cb.literal("Deposit"),
                    cb.literal(0),
                    depositRoot.get("card").get("id"),
                    depositRoot.get("timestamp"),
                    depositRoot.get("currency").get("id"),
                    depositRoot.get("amount")));
            depositQuery.where(cb.equal(depositRoot.get("user").get("id"), user.getId()));

            CriteriaQuery<OrderFilterDto> withdrawalQuery = cb.createQuery(OrderFilterDto.class);
            Root<TransactionsExternal> withdrawalRoot = withdrawalQuery.from(TransactionsExternal.class);
            withdrawalQuery.select(cb.construct(OrderFilterDto.class,
                    cb.literal("Withdrawal"),
                    cb.literal(0),
                    withdrawalRoot.get("card").get("id"),
                    withdrawalRoot.get("timestamp"),
                    withdrawalRoot.get("currency").get("id"),
                    withdrawalRoot.get("amount")));
            withdrawalQuery.where(cb.equal(withdrawalRoot.get("user").get("id"), user.getId()));

            CriteriaQuery<OrderFilterDto> incomingQuery = cb.createQuery(OrderFilterDto.class);
            Root<TransactionsInternal> incomingRoot = incomingQuery.from(TransactionsInternal.class);
            incomingQuery.select(cb.construct(OrderFilterDto.class,
                    cb.literal("Incoming"),
                    incomingRoot.get("spendingCategory").get("id"),
                    incomingRoot.get("senderWalletId"),
                    incomingRoot.get("timestamp"),
                    incomingRoot.get("currency").get("id"),
                    incomingRoot.get("amount")));
            incomingQuery.where(cb.equal(incomingRoot.get("recipientWalletId"), user.getWallet().getId()));

            CriteriaQuery<OrderFilterDto> outgoingQuery = cb.createQuery(OrderFilterDto.class);
            Root<TransactionsInternal> outgoingRoot = outgoingQuery.from(TransactionsInternal.class);
            outgoingQuery.select(cb.construct(OrderFilterDto.class,
                    cb.literal("Outgoing"),
                    outgoingRoot.get("spendingCategory").get("id"),
                    outgoingRoot.get("recipientWalletId"),
                    outgoingRoot.get("timestamp"),
                    outgoingRoot.get("currency").get("id"),
                    outgoingRoot.get("amount")));
            outgoingQuery.where(cb.equal(outgoingRoot.get("senderWalletId"), user.getWallet().getId()));

            List<OrderFilterDto> deposits = session.createQuery(depositQuery).getResultList();
            List<OrderFilterDto> withdrawals = session.createQuery(withdrawalQuery).getResultList();
            List<OrderFilterDto> incomings = session.createQuery(incomingQuery).getResultList();
            List<OrderFilterDto> outgoings = session.createQuery(outgoingQuery).getResultList();

            List<OrderFilterDto> allOrders = new ArrayList<>();
            allOrders.addAll(deposits);
            allOrders.addAll(withdrawals);
            allOrders.addAll(incomings);
            allOrders.addAll(outgoings);

            return allOrders;
        }
    }
}
