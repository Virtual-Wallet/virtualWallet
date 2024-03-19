package org.example.virtual_wallet.repositories.contracts;

import org.example.virtual_wallet.filters.TransferFilterOptions;
import org.example.virtual_wallet.models.TransactionsExternal;
import org.example.virtual_wallet.models.User;

import java.util.List;

public interface TransactionsExternalRepository {
    void create(TransactionsExternal transactionsExternal);

    List<TransactionsExternal> getAll();

    List<TransactionsExternal> getDeposits(User user);

    List<TransactionsExternal> getWithdrawals(User user);

    List<TransactionsExternal> getFiltered(TransferFilterOptions filterOptions, User user);
}
