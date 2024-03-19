package org.example.virtual_wallet.services;

import org.example.virtual_wallet.filters.TransactionFilterOptions;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.Wallet;
import org.example.virtual_wallet.repositories.contracts.TransactionsInternalRepository;
import org.example.virtual_wallet.services.contracts.CurrencyService;
import org.example.virtual_wallet.services.contracts.TransactionsInternalService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

//GV TODO add helpers
@Service
public class TransactionsInternalServiceImpl implements TransactionsInternalService {

    private final WalletServiceImpl walletService;
    private final TransactionsInternalRepository repository;

    private final CurrencyService currencyService;

    public TransactionsInternalServiceImpl(WalletServiceImpl walletService,
                                           TransactionsInternalRepository repository,
                                           CurrencyService currencyService) {
        this.walletService = walletService;
        this.repository = repository;
        this.currencyService = currencyService;
    }

    @Override
    public TransactionsInternal create(TransactionsInternal transaction) {
        Wallet senderWallet = getSenderWallet(transaction);
        Wallet recipientWallet = getRecipientWallet(transaction);

        try {
            BigDecimal amountSourceToTransactional = calculateAmountSourceToTransactional(transaction, senderWallet);
            BigDecimal amountTransactionalToTarget = calculateAmountTransactionalToTarget(transaction, recipientWallet);

            performTransaction(senderWallet, recipientWallet, amountSourceToTransactional, amountTransactionalToTarget);

            repository.create(transaction);
            return transaction;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Wallet getSenderWallet(TransactionsInternal transaction) {
        return walletService.getById(transaction.getSenderWalletId());
    }

    private Wallet getRecipientWallet(TransactionsInternal transaction) {
        return walletService.getById(transaction.getRecipientWallet().getId());
    }

    private BigDecimal calculateAmountSourceToTransactional(TransactionsInternal transaction,
                                                            Wallet senderWallet) throws IOException {
        Map<String, Double> rates = currencyService.getRates(senderWallet.getCurrency());
        double sourceToTransactionalRate = rates.get(transaction.getCurrency().getCurrency());
        BigDecimal amountSourceToTransactional = new BigDecimal(transaction.getAmount() / sourceToTransactionalRate);
        return amountSourceToTransactional.setScale(2, RoundingMode.HALF_DOWN);
    }

    private BigDecimal calculateAmountTransactionalToTarget(TransactionsInternal transaction,
                                                            Wallet recipientWallet) throws IOException {
        Map<String, Double> rates = currencyService.getRates(transaction.getCurrency());
        double transactionalToTargetRate = rates.get(recipientWallet.getCurrency().getCurrency());
        BigDecimal amountTransactionalToTarget = new BigDecimal(transaction.getAmount() * transactionalToTargetRate);
        return amountTransactionalToTarget.setScale(2, RoundingMode.HALF_DOWN);
    }

    private void performTransaction(Wallet senderWallet,
                                    Wallet recipientWallet,
                                    BigDecimal amountSourceToTransactional,
                                    BigDecimal amountTransactionalToTarget) {
        walletService.withdraw(senderWallet, amountSourceToTransactional.doubleValue());
        walletService.deposit(recipientWallet, amountTransactionalToTarget.doubleValue());
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

    @Override
    public List<TransactionsInternal> getOutgoingPerCategory(int categoryId, User user) {
        return repository.getOutgoingPerCategory(categoryId, user);
    }

    @Override
    public List<TransactionsInternal> getFilteredIncoming(TransactionFilterOptions filterOptions, User user) {
        return repository.getFilteredIncoming(filterOptions, user);
    }

    @Override
    public List<TransactionsInternal> getFilteredOutgoing(TransactionFilterOptions filterOptions, User user) {
        return repository.getFilteredOutgoing(filterOptions, user);
    }
}
