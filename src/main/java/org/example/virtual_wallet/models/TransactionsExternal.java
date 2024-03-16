package org.example.virtual_wallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import org.example.virtual_wallet.enums.TransferType;

import java.util.Date;

@Entity
@Table(name = "external_transactions")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class TransactionsExternal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "external_transaction_id")
    private int externalTransactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransferType type;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @JsonIgnore
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

    public TransactionsExternal() {
    }

    public int getExternalTransactionId() {
        return externalTransactionId;
    }

    public TransferType getType() {
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

    public void setType(TransferType type) {
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
