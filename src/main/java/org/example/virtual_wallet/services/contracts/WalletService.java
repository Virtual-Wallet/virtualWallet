package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.Wallet;
import org.springframework.stereotype.Service;

import java.util.List;

public interface WalletService {
    void create(Wallet wallet, User user);
    Wallet getUserWallet(User user);
//    void update(Wallet wallet, User user);
    Wallet getById(int id);
    void delete(int walletId, User user);
    void withdraw(Wallet wallet, double amount);
    void deposit(Wallet wallet, double amount);
    List<Wallet> getAllWallets();

}
