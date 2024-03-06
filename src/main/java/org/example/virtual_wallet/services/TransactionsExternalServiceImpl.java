package org.example.virtual_wallet.services;

import org.example.virtual_wallet.models.TransactionsExternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.TransactionsExternalRepository;
import org.example.virtual_wallet.services.contracts.TransactionsExternalService;

import java.util.List;

public class TransactionsExternalServiceImpl implements TransactionsExternalService {

    private final WalletServiceImpl walletService;
    private final TransactionsExternalRepository repository;

    public TransactionsExternalServiceImpl(WalletServiceImpl walletService,
                                           TransactionsExternalRepository repository) {
        this.walletService = walletService;
        this.repository = repository;
    }

    @Override
    public TransactionsExternal create(TransactionsExternal transactionsExternal) {
        return null;
    }

    @Override
    public List<TransactionsExternal> getAll() {
        return null;
    }

    @Override
    public List<TransactionsExternal> getDeposits(User user) {
        return repository.getDeposits(user);
    }

    @Override
    public List<TransactionsExternal> getWithdrawals(User user) {
        return repository.getWithdrawals(user);
    }
}
