package org.example.virtual_wallet.services;

import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.exceptions.InsufficientAmountException;
import org.example.virtual_wallet.exceptions.InvalidOperationException;
import org.example.virtual_wallet.helpers.AuthorizationHelper;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.Wallet;
import org.example.virtual_wallet.repositories.contracts.WalletRepository;
import org.example.virtual_wallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {
    private final AuthorizationHelper authorizationHelper;

    private final WalletRepository walletRepository;

    @Autowired
    public WalletServiceImpl(AuthorizationHelper authorizationHelper, WalletRepository walletRepository) {
        this.authorizationHelper = authorizationHelper;
        this.walletRepository = walletRepository;
    }

    @Override
    public void create(Wallet wallet, User user) {
        verifyUniqueUserWallet(user);
        wallet.setUser(user);
        wallet.setActive(true);
        walletRepository.create(wallet);
    }

//    @Override
//    public void update(Wallet wallet, User user)  {
//        authorizationHelper.validateUserIsWalletOwner(user, wallet);
//        walletRepository.update(wallet);
//    }

    @Override
    public void delete(int walletId, User user) {
        Wallet wallet = getById(walletId);
        authorizationHelper.validateUserIsWalletOwner(user, wallet);
        validateBalanceIfZero(wallet);
        wallet.setDeleted(true);
        walletRepository.update(wallet);
    }

    @Override
    public Wallet getById(int id) {
        return walletRepository.getById(id);
    }
    @Override
    public void withdraw(Wallet wallet, double amount) {
        validateWithdrawAmount(wallet, amount);
        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.update(wallet);
    }

    @Override
    public void deposit(Wallet wallet, double amount) {
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.update(wallet);
    }

    @Override
    public List<Wallet> getAllWallets() {
        return walletRepository.getAll();
    }

    @Override
    public Wallet getUserWallet(User user) {
        return walletRepository.getWalletByUserId(user.getId());
    }

    private void verifyUniqueUserWallet(User user){
        boolean isDuplicateExisting = true;
        try {
            getUserWallet(user);
        } catch (EntityNotFoundException e){
            isDuplicateExisting = false;
        }
        if (isDuplicateExisting){
            throw new EntityDuplicateException("There is wallet assigned to the user already!");
        }
    }

    private void validateBalanceIfZero(Wallet wallet){
        if (wallet.getBalance() > 0){
            throw new InvalidOperationException("The Wallet cannot be deleted. The Balance is above 0.");
        }
    }

    private void validateWithdrawAmount(Wallet wallet, double amount){
        double diff = wallet.getBalance() - amount;
        if (diff < 0){
            throw new InsufficientAmountException(amount);
        }
    }
}
