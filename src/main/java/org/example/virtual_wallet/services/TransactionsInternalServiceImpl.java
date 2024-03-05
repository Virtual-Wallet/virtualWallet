package org.example.virtual_wallet.services;

import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.TransactionsInternalRepository;
import org.example.virtual_wallet.services.contracts.TransactionsInternalService;
import org.springframework.stereotype.Service;

import java.util.List;

//GV TODO add helpers
@Service
public class TransactionsInternalServiceImpl implements TransactionsInternalService {

    private final WalletServiceImpl walletService;
    private final TransactionsInternalRepository repository;

    public TransactionsInternalServiceImpl(WalletServiceImpl walletService,
                                           TransactionsInternalRepository repository) {
        this.walletService = walletService;
        this.repository = repository;
    }

    @Override
    public TransactionsInternal create(TransactionsInternal transaction) {
        walletService.withdraw(walletService.getById(transaction.getSenderWalletId()), transaction.getAmount());
        walletService.deposit(walletService.getById(transaction.getRecipientWalletId()), transaction.getAmount());
        repository.create(transaction);
        return transaction;
    }

    @Override
    public List<TransactionsInternal> getAll() {
        return repository.getAll();
    }

    @Override
    public List<TransactionsInternal> getIncoming(User user) {
        return repository.getIncoming(user);
    }

    @Override
    public List<TransactionsInternal> getOutgoing(User user) {
        return repository.getOutgoing(user);
    }
}
