package org.example.virtual_wallet.filters;

import java.sql.Timestamp;
import java.util.Optional;

public class TransactionFilterOptions {
    Optional<Integer> senderWalletId;
    Optional<Integer> recipientWalletId;
    Optional<Timestamp> timestamp;
    Optional<Double> amount;
    Optional<String> sortBy;
    Optional<String> sortOrder;

    public TransactionFilterOptions() {
        this(null, null, null, null, null, null);
    }

    public TransactionFilterOptions(Integer senderWalletId,
                                    Integer recipientWalletId,
                                    Timestamp timestamp,
                                    Double amount,
                                    String sortBy,
                                    String sortOrder) {
        this.senderWalletId = Optional.ofNullable(senderWalletId);
        this.recipientWalletId = Optional.ofNullable(recipientWalletId);
        this.timestamp = Optional.ofNullable(timestamp);
        this.amount = Optional.ofNullable(amount);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);

    }

    public Optional<Integer> getSenderWalletId() {
        return senderWalletId;
    }

    public Optional<Integer> getRecipientWalletId() {
        return recipientWalletId;
    }

    public Optional<Timestamp> getTimestamp() {
        return timestamp;
    }

    public Optional<Double> getAmount() {
        return amount;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
