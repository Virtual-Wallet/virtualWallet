package org.example.virtual_wallet.repositories.contracts;

import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.Wallet;

import java.util.List;

public interface WalletRepository {

    void create(Wallet wallet);

    void update(Wallet wallet);
    void delete(int walletId);
    public List<Wallet> getAll();
    public Wallet getById(int id);
    public Wallet getWalletByUserId(int userId);
}
