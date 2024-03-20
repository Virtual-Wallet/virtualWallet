package org.example.virtual_wallet;

import org.example.virtual_wallet.enums.AccountStatus;
import org.example.virtual_wallet.enums.RoleType;
import org.example.virtual_wallet.enums.TransferType;
import org.example.virtual_wallet.models.*;
import org.example.virtual_wallet.models.Currency;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

public class Helpers {

    public static User createMockUserRegular() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("mockUsername");
        mockUser.setPassword("Aa123456@");
        mockUser.setEmail("mockmail@gmail.com");
        mockUser.setPhoneNumber("0888123456");
        mockUser.setAccountStatus(AccountStatus.ACTIVE);
        mockUser.setRoleType(RoleType.REGULAR);
        mockUser.setCards(new HashSet<>());
        return mockUser;
    }

    public static User createMockUserAdmin() {
        var mockUser = new User();
        Set<Card> mockCards = new HashSet<>();
        mockUser.setId(2);
        mockUser.setUsername("mockUsername");
        mockUser.setPassword("Aa123456@");
        mockUser.setEmail("mockmail@gmail.com");
        mockUser.setPhoneNumber("0888123456");
        mockUser.setAccountStatus(AccountStatus.ACTIVE);
        mockUser.setRoleType(RoleType.ADMIN);
        mockUser.setCards(mockCards);

        return mockUser;
    }

    public static User createMockUserBanned() {
        var mockUser = new User();
        Set<Card> mockCards = new HashSet<>();
        mockUser.setId(3);
        mockUser.setUsername("mockUsername");
        mockUser.setPassword("Aa123456@");
        mockUser.setEmail("mockmail@gmail.com");
        mockUser.setPhoneNumber("0888123456");
        mockUser.setCreationDate(Timestamp.valueOf("2024-03-20 12:34:56.123456"));
        mockUser.setAccountStatus(AccountStatus.ACTIVE);
        mockUser.setRoleType(RoleType.BANNED);
        mockUser.setCards(mockCards);

        return mockUser;
    }

    public static Wallet createMockWallet() {
        var mockWallet = new Wallet();
        mockWallet.setId(1);
        mockWallet.setBalance(1000.0);
        mockWallet.setActive(true);
        mockWallet.setDeleted(false);

        return mockWallet;
    }

    public static Currency createMockCurrency() {
        var mockCurrency = new Currency();
        mockCurrency.setId(1);
        mockCurrency.setCurrency("EUR");

        return mockCurrency;
    }

    public static TransactionsExternal transactionDeposit() {
        var transactionDeposit = new TransactionsExternal();
        transactionDeposit.setExternalTransactionId(1);
        transactionDeposit.setType(TransferType.DEPOSIT);
        transactionDeposit.setTimestamp(Timestamp.valueOf(LocalDate.now().toString()));
        transactionDeposit.setAmount(1000);

        return transactionDeposit;
    }

    public static TransactionsExternal transactionWithdrawal() {
        var transactionWithdrawal = new TransactionsExternal();
        transactionWithdrawal.setExternalTransactionId(1);
        transactionWithdrawal.setType(TransferType.WITHDRAWAL);
        transactionWithdrawal.setTimestamp(Timestamp.valueOf(LocalDate.now().toString()));
        transactionWithdrawal.setAmount(1000);

        return transactionWithdrawal;
    }

    public static TransactionsInternal transactionsIncoming() {
        var transactionsIncoming = new TransactionsInternal();
        transactionsIncoming.setId(1);
        transactionsIncoming.setAmount(50);
        transactionsIncoming.setTimestamp(Timestamp.valueOf(LocalDate.now().toString()));

        return transactionsIncoming;
    }

    public static TransactionsInternal transactionsOutgoing() {
        var transactionsOutgoing = new TransactionsInternal();
        transactionsOutgoing.setId(2);
        transactionsOutgoing.setAmount(20);
        transactionsOutgoing.setTimestamp(Timestamp.valueOf(LocalDate.now().toString()));

        return transactionsOutgoing;
    }

    public static SpendingCategory spendingCategory() {
        var spendingCategory = new SpendingCategory();
        spendingCategory.setSpendingCategoryId(1);
        spendingCategory.setName("mockCategory");
        spendingCategory.setDeleted(false);

        return spendingCategory;
    }

    public static Token mockToken() {
        var mockToken = new Token();
        mockToken.setId(1);
        mockToken.setCode("12345678");
        mockToken.setActive(true);

        return mockToken;
    }

    public static Card createMockCard() {
        var mockCard = new Card();
        mockCard.setUser(createMockUserRegular());
        mockCard.setId(1);
        mockCard.setCardNumber("1234512345123454");
        mockCard.setCardholderName("Mock Holder");
        mockCard.setExpirationDate("12/50");
        mockCard.setCsv("123");
        mockCard.setDeleted(false);


        return mockCard;
    }
    public static Card createMockCardTwo() {
        var mockCard = new Card();
        mockCard.setId(2);
        mockCard.setCardNumber("1234512345123454");
        mockCard.setCardholderName("Mock Holder");
        mockCard.setExpirationDate("12/33");
        mockCard.setCsv("123");
        mockCard.setDeleted(false);

        return mockCard;
    }

}
