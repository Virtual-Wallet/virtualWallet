package org.example.virtual_wallet.filters;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Optional;

public class OrderFilterOptions {
    Optional<String> type;
    Optional<Integer> category;
    Optional<Integer> contractor;
    Optional<Timestamp> date;
    Optional<Integer> currency;
    Optional<Double> amount;
    Optional<String> sortBy;
    Optional<String> sortOrder;

    public OrderFilterOptions(String type,
                              int category,
                              int contractor,
                              Timestamp date,
                              int currency,
                              double amount,
                              String sortBy,
                              String sortOrder) {
        this.type = Optional.ofNullable(type);
        this.category = Optional.ofNullable(category);
        this.contractor = Optional.ofNullable(contractor);
        this.date = Optional.ofNullable(date);
        this.currency = Optional.ofNullable(currency);
        this.amount = Optional.ofNullable(amount);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public OrderFilterOptions() {
        this(null, null, null, null, null, null, null, null);
    }

    public OrderFilterOptions(String type, Integer o, Object o1, Object date, Object o2, Object o3, Object sortBy, Object sortOrder) {

    }

    public Optional<String> getType() {
        return type;
    }

    public Optional<Integer> getCategory() {
        return category;
    }

    public Optional<Integer> getContractor() {
        return contractor;
    }

    public Optional<Timestamp> getDate() {
        return date;
    }

    public Optional<Integer> getCurrency() {
        return currency;
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
