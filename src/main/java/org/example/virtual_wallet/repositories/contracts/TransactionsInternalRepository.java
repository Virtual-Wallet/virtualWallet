package org.example.virtual_wallet.repositories.contracts;

import org.example.virtual_wallet.filters.TransactionFilterOptions;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;

import java.util.List;

public interface TransactionsInternalRepository {

    void create(TransactionsInternal transactionsInternal);

    List<TransactionsInternal> getAll();

    List<TransactionsInternal> getIncoming(User user);

    List<TransactionsInternal> getOutgoing(User user);

    List<TransactionsInternal> getOutgoingPerCategory(int categoryId, User user);

    List<TransactionsInternal> getFilteredIncoming(TransactionFilterOptions filterOptions, User user);

    List<TransactionsInternal> getFilteredOutgoing(TransactionFilterOptions filterOptions, User user);

    List<TransactionsInternal> getIncomingPerCategory(int categoryId, User user);
}
