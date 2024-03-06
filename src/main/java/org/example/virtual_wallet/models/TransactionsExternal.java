package org.example.virtual_wallet.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "external_transactions")
public class TransactionsExternal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "external_transaction_id")
    private int externalTransactionId;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "timestamp")
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(name = "amount")
    private double amount;

    public TransactionsExternal(int externalTransactionId,
                                String type,
                                User user,
                                Card card,
                                Date timestamp,
                                Currency currency,
                                double amount) {
        this.externalTransactionId = externalTransactionId;
        this.type = type;
        this.user = user;
        this.card = card;
        this.timestamp = timestamp;
        this.currency = currency;
        this.amount = amount;
    }

    public int getExternalTransactionId() {
        return externalTransactionId;
    }

    public String getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    public Card getCard() {
        return card;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setExternalTransactionId(int externalTransactionId) {
        this.externalTransactionId = externalTransactionId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
