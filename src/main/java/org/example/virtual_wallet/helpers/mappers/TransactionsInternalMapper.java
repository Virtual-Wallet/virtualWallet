package org.example.virtual_wallet.helpers.mappers;

import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.TransactionDto;
import org.example.virtual_wallet.models.dtos.TransactionDtoIn;
import org.example.virtual_wallet.services.contracts.CurrencyService;
import org.example.virtual_wallet.services.contracts.SpendingCategoryService;
import org.example.virtual_wallet.services.contracts.TransactionsInternalService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class TransactionsInternalMapper {
    private final TransactionsInternalService transactionsInternalService;
    private final UserService userService;
    private final CurrencyService currencyService;
    private final SpendingCategoryService categoryService;
    LocalDateTime now = LocalDateTime.now();
    Timestamp timestamp = Timestamp.valueOf(now);

    public TransactionsInternalMapper(TransactionsInternalService transactionsInternalService,
                                      CurrencyService currencyService, UserService userService,
                                      SpendingCategoryService categoryService) {
        this.transactionsInternalService = transactionsInternalService;
        this.currencyService = currencyService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public TransactionsInternal createDto(User sender, User recipient, TransactionDto dto) {
        TransactionsInternal transaction = new TransactionsInternal();
        transaction.setSenderWalletId(sender.getWallet().getId());
        transaction.setRecipientWallet(recipient.getWallet());
        transaction.setAmount(dto.getAmount());
        transaction.setTimestamp(timestamp);
        transaction.setCurrency(currencyService.get(dto.getCurrency()));

        // GV TODO: Should findByCategoryName
        transaction.setSpendingCategory(categoryService.getById(1));
//        transaction.setSpendingCategory(categoryService.getByCategory(dto.getCategory()));
        System.out.println();
        return transaction;
    }

    public TransactionsInternal createDto(User sender, TransactionDtoIn dto) {
        User recipient = userService.getByUserInput(dto.getTargetUserIdentity());

        TransactionsInternal transaction = new TransactionsInternal();
        transaction.setSenderWalletId(sender.getWallet().getId());
        transaction.setRecipientWallet(recipient.getWallet());
        transaction.setAmount(dto.getAmount());
        transaction.setTimestamp(timestamp);
        transaction.setCurrency(currencyService.get(dto.getCurrency()));
        transaction.setSpendingCategory(categoryService.getByCategory(dto.getCategoryName()));

        return transaction;
    }
}
