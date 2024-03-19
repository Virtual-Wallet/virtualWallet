package org.example.virtual_wallet.models.dtos;

import jakarta.persistence.*;

import java.sql.Timestamp;

@MappedSuperclass
public class OrderFilterDto {
    @Id
    private String id;
    private String type;
    private int category;
    private int contractor;
    private Timestamp date;
    private int currency;
    private double amount;

    public OrderFilterDto(
            String type,
            int category,
            int contractor,
            Timestamp date,
            int currency,
            double amount) {
        this.type = type;
        this.category = category;
        this.contractor = contractor;
        this.date = date;
        this.currency = currency;
        this.amount = amount;
    }

    public OrderFilterDto() {
    }

    public String getType() {
        return type;
    }

    public int getCategory() {
        return category;
    }

    public int getContractor() {
        return contractor;
    }

    public Timestamp getDate() {
        return date;
    }

    public int getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }
}
