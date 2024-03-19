package org.example.virtual_wallet.services;

import org.example.virtual_wallet.exceptions.LargeTransactionDetectedException;
import org.example.virtual_wallet.filters.TransferFilterOptions;
import org.example.virtual_wallet.models.TransactionsExternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.TransactionsExternalRepository;
import org.example.virtual_wallet.services.contracts.TransactionsExternalService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionsExternalServiceImpl implements TransactionsExternalService {

    private final WalletServiceImpl walletService;
    private final TransactionsExternalRepository repository;
    private static final double LARGE_TRANSACTION_AMOUNT = 1000;
    private static final String LARGE_TRANSACTION_MESSAGE = "A verification code has been sent to your email to verify large transaction.";

    public TransactionsExternalServiceImpl(WalletServiceImpl walletService,
                                           TransactionsExternalRepository repository) {
        this.walletService = walletService;
        this.repository = repository;
    }

    @Override
    public TransactionsExternal createDeposit(TransactionsExternal transfer) {
        walletService.deposit(transfer.getUser().getWallet(), transfer.getAmount());
        repository.create(transfer);
        return transfer;
    }

    @Override
    public TransactionsExternal createWithdrawal(TransactionsExternal transfer) {
        checkForLargeTransaction(transfer);
        walletService.withdraw(transfer.getUser().getWallet(), transfer.getAmount());
        repository.create(transfer);
        return transfer;
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

    @Override
    public List<TransactionsExternal> getFiltered(TransferFilterOptions filterOptions, User user) {
        return repository.getFiltered(filterOptions, user);
    }


    @Override
    public void checkForLargeTransaction(TransactionsExternal transaction) {
        if (transaction.getAmount() > LARGE_TRANSACTION_AMOUNT) {
            throw new LargeTransactionDetectedException(LARGE_TRANSACTION_MESSAGE);
        }
    }
}
