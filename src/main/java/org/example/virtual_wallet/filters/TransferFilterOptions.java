package org.example.virtual_wallet.filters;

import org.example.virtual_wallet.enums.TransferType;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.Currency;

import java.sql.Timestamp;
import java.util.Optional;

public class TransferFilterOptions {
    Optional<String> type;
    Optional<Card> card;
    Optional<Currency> currency;
    Optional<Timestamp> timestamp;
    Optional<Double> amount;
    Optional<String> sortBy;
    Optional<String> sortOrder;

    public TransferFilterOptions() {
        this(null, null, null, null, null, null, null);
    }

    public TransferFilterOptions(String type,
                                 Card card,
                                 Currency currency,
                                 Timestamp timestamp,
                                 Double amount,
                                 String sortBy,
                                 String sortOrder) {
        this.type = Optional.ofNullable(type);
        this.card = Optional.ofNullable(card);
        this.currency = Optional.ofNullable(currency);
        this.timestamp = Optional.ofNullable(timestamp);
        this.amount = Optional.ofNullable(amount);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getType() {
        return type;
    }

    public Optional<Card> getCard() {
        return card;
    }

    public Optional<Currency> getCurrency() {
        return currency;
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
