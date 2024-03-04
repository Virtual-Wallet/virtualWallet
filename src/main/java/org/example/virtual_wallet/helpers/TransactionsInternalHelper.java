package org.example.virtual_wallet.helpers;

import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.TransactionsInternalDto;
import org.example.virtual_wallet.services.contracts.CurrencyService;
import org.example.virtual_wallet.services.contracts.SpendingCategoryService;
import org.example.virtual_wallet.services.contracts.TransactionsInternalService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class TransactionsInternalHelper {
    private final TransactionsInternalService transactionsInternalService;
    private final UserService userService;
    private final CurrencyService currencyService;
    private final SpendingCategoryService categoryService;
    LocalDateTime now = LocalDateTime.now();
    Timestamp timestamp = Timestamp.valueOf(now);

    public TransactionsInternalHelper(TransactionsInternalService transactionsInternalService,
                                      CurrencyService currencyService, UserService userService,
                                      SpendingCategoryService categoryService) {
        this.transactionsInternalService = transactionsInternalService;
        this.currencyService = currencyService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public TransactionsInternal createDto(User sender, User recipient, TransactionsInternalDto dto) {
        TransactionsInternal transaction = new TransactionsInternal();
        transaction.setSenderWalletId(sender.getWallet().getId());
        transaction.setRecipientWalletId(recipient.getWallet().getId());
        transaction.setAmount(dto.getAmount());
        transaction.setTimestamp(timestamp);
        transaction.setCurrency(currencyService.get(dto.getCurrency()));

        // GV TODO: Should findByCategoryName
        transaction.setSpendingCategory(categoryService.getById(1));
//        transaction.setSpendingCategory(categoryService.getByCategory(dto.getCategory()));
        System.out.println();
        return transaction;
    }
}
