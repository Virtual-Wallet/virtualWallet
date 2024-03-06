package org.example.virtual_wallet.helpers.mappers;

import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.Wallet;
import org.example.virtual_wallet.models.dtos.WalletDto;
import org.example.virtual_wallet.services.contracts.CurrencyService;
import org.example.virtual_wallet.services.contracts.WalletService;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {
    private final WalletService walletService;
    private final CurrencyService currencyService;

    public WalletMapper(WalletService walletService, CurrencyService currencyService) {
        this.walletService = walletService;
        this.currencyService = currencyService;
    }
    public Wallet dtoWalletCreate(WalletDto walletDto, User user) {
        Wallet wallet = new Wallet();
        wallet.setCurrency(currencyService.get(walletDto.getCurrency()));
        wallet.setUser(user);
        return wallet;
    }

    public Wallet dtoWalletUpdate(WalletDto walletDto, int walletId) {
        Wallet wallet = walletService.getById(walletId);
        wallet.setCurrency(currencyService.get(walletDto.getCurrency()));
        return wallet;
    }

    public WalletDto toWalletDto(Wallet wallet){
        WalletDto walletDto = new WalletDto();
        wallet.setCurrency(wallet.getCurrency());
        return walletDto;
    }

}
