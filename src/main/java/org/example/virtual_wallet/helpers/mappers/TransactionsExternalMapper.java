package org.example.virtual_wallet.helpers.mappers;

import org.example.virtual_wallet.enums.TransferType;
import org.example.virtual_wallet.models.TransactionsExternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.TransferDto;
import org.example.virtual_wallet.services.contracts.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class TransactionsExternalMapper {
    private final TransactionsExternalService transactionsExternalService;
    private final UserService userService;

    private final CardService cardService;
    private final CurrencyService currencyService;
    LocalDateTime now = LocalDateTime.now();
    Timestamp timestamp = Timestamp.valueOf(now);

    public TransactionsExternalMapper(TransactionsExternalService transactionsExternalService,
                                      UserService userService,
                                      CardService cardService,
                                      CurrencyService currencyService) {
        this.transactionsExternalService = transactionsExternalService;
        this.userService = userService;
        this.cardService = cardService;
        this.currencyService = currencyService;
    }


    public TransactionsExternal depositDto(User user,
                                           TransferDto dto) {
        TransactionsExternal transfer = new TransactionsExternal();
        transfer.setType(TransferType.DEPOSIT);
        transfer.setUser(user);
        transfer.setCard(cardService.getByCardNumber(dto.getCardNumber()));
        transfer.setAmount(dto.getAmount());
        transfer.setTimestamp(timestamp);
        transfer.setCurrency(currencyService.get(dto.getCurrency()));
        return transfer;
    }

    public TransactionsExternal withdrawalDto(User user,
                                              TransferDto dto) {
        TransactionsExternal transfer = new TransactionsExternal();
        transfer.setType(TransferType.WITHDRAWAL);
        transfer.setUser(user);
        transfer.setCard(cardService.getByCardNumber(dto.getCardNumber()));
        transfer.setAmount(dto.getAmount());
        transfer.setTimestamp(timestamp);
        transfer.setCurrency(currencyService.get(dto.getCurrency()));
        return transfer;
    }
}
