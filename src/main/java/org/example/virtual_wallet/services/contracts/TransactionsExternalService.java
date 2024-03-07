package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.models.TransactionsExternal;
import org.example.virtual_wallet.models.User;

import java.util.List;

public interface TransactionsExternalService {

    TransactionsExternal createDeposit(TransactionsExternal transactionsExternal);

    TransactionsExternal createWithdrawal(TransactionsExternal transactionsExternal);

    List<TransactionsExternal> getAll();

    List<TransactionsExternal> getDeposits(User user);

    List<TransactionsExternal> getWithdrawals(User user);
}
