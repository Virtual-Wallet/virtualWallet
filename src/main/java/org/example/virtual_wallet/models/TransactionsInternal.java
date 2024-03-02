package org.example.virtual_wallet.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

@Entity
@Table(name = "internal_transactions")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class TransactionsInternal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "internal_transaction_id")
    private int id;

    @Column(name = "sender_wallet_id")
    private int senderWalletId;

    @Column(name = "recipient_wallet_id")
    private int recipientWalletId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "spending_category")
    private String spendingCategory;

    @Column(name = "currency")
    private String currency;

    public TransactionsInternal() {
    }

    public int getId() {
        return id;
    }

    public int getSenderWalletId() {
        return senderWalletId;
    }

    public int getRecipientWalletId() {
        return recipientWalletId;
    }

    public double getAmount() {
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSpendingCategory() {
        return spendingCategory;
    }

    public String getCurrency() {
        return currency;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSenderWalletId(int senderWalletId) {
        this.senderWalletId = senderWalletId;
    }

    public void setRecipientWalletId(int recipient_wallet_id) {
        this.recipientWalletId = recipient_wallet_id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setSpendingCategory(String spendingCategory) {
        this.spendingCategory = spendingCategory;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
