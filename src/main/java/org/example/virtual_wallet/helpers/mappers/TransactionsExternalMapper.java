package org.example.virtual_wallet.helpers.mappers;

import org.example.virtual_wallet.services.contracts.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class TransactionsExternalMapper {
    private final TransactionsExternalService transactionsExternalService;
    private final UserService userService;
    private final CurrencyService currencyService;
    LocalDateTime now = LocalDateTime.now();
    Timestamp timestamp = Timestamp.valueOf(now);

    public TransactionsExternalMapper(TransactionsExternalService transactionsExternalService,
                                      UserService userService,
                                      CurrencyService currencyService) {
        this.transactionsExternalService = transactionsExternalService;
        this.userService = userService;
        this.currencyService = currencyService;
    }


}
