package org.example.virtual_wallet.repositories;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.virtual_wallet.models.TransactionsExternal;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.OrderDto;
import org.example.virtual_wallet.services.contracts.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderSearchDao implements org.example.virtual_wallet.repositories.contracts.OrderSearchDao {
    private final SessionFactory sessionFactory;

    private final TransactionsInternalRepositoryImpl repositoryTransactions;
    private final TransactionsExternalRepositoryImpl repositoryTransfers;

    private final UserService userService;

    @Autowired
    public OrderSearchDao(SessionFactory sessionFactory,
                          TransactionsInternalRepositoryImpl repositoryTransactions,
                          TransactionsExternalRepositoryImpl repositoryTransfers,
                          UserService userService) {
        this.sessionFactory = sessionFactory;
        this.repositoryTransactions = repositoryTransactions;
        this.repositoryTransfers = repositoryTransfers;
        this.userService = userService;
    }

    @Override
    public List<OrderDto> getAll(User user) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();

            CriteriaQuery<OrderDto> depositQuery = cb.createQuery(OrderDto.class);
            Root<TransactionsExternal> depositRoot = depositQuery.from(TransactionsExternal.class);
            depositQuery.select(cb.construct(OrderDto.class,
                    cb.literal("Deposit"),
//                    cb.literal(0),
//                    depositRoot.get("card").get("id"),
//                    depositRoot.get("timestamp"),
                    depositRoot.get("currency").get("id"),
                    depositRoot.get("amount")));
            depositQuery.where(cb.equal(depositRoot.get("user").get("id"), user.getId()));

            CriteriaQuery<OrderDto> withdrawalQuery = cb.createQuery(OrderDto.class);
            Root<TransactionsExternal> withdrawalRoot = withdrawalQuery.from(TransactionsExternal.class);
            withdrawalQuery.select(cb.construct(OrderDto.class,
                    cb.literal("Withdrawal"),
//                    cb.literal(0),
//                    withdrawalRoot.get("card").get("id"),
//                    withdrawalRoot.get("timestamp"),
                    withdrawalRoot.get("currency").get("id"),
            withdrawalRoot.get("amount")));
            withdrawalQuery.where(cb.equal(withdrawalRoot.get("user").get("id"), user.getId()));

            CriteriaQuery<OrderDto> incomingQuery = cb.createQuery(OrderDto.class);
            Root<TransactionsInternal> incomingRoot = incomingQuery.from(TransactionsInternal.class);
            incomingQuery.select(cb.construct(OrderDto.class,
                    cb.literal("Incoming"),
//                    incomingRoot.get("senderWalletId"),
//                    cb.literal(0),
//                    incomingRoot.get("timestamp"),
                    incomingRoot.get("currency").get("id"),
                    incomingRoot.get("amount")));
            incomingQuery.where(cb.equal(incomingRoot.get("recipientWalletId"), user.getWallet().getId()));

            CriteriaQuery<OrderDto> outgoingQuery = cb.createQuery(OrderDto.class);
            Root<TransactionsInternal> outgoingRoot = outgoingQuery.from(TransactionsInternal.class);
            outgoingQuery.select(cb.construct(OrderDto.class,
                    cb.literal("Outgoing"),
//                    outgoingRoot.get("recipientWalletId"),
//                    cb.literal(0),
//                    outgoingRoot.get("timestamp"),
                    outgoingRoot.get("currency").get("id"),
                    outgoingRoot.get("amount")));
            outgoingQuery.where(cb.equal(outgoingRoot.get("senderWalletId"), user.getWallet().getId()));

            List<OrderDto> deposits = session.createQuery(depositQuery).getResultList();
            List<OrderDto> withdrawals = session.createQuery(withdrawalQuery).getResultList();
            List<OrderDto> incomings = session.createQuery(incomingQuery).getResultList();
            List<OrderDto> outgoings = session.createQuery(outgoingQuery).getResultList();

            List<OrderDto> allOrders = new ArrayList<>();
            allOrders.addAll(deposits);
            allOrders.addAll(withdrawals);
            allOrders.addAll(incomings);
            allOrders.addAll(outgoings);

            return allOrders;
        }
    }
}
