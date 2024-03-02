package org.example.virtual_wallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "wallets")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private int id;
    @Column(name = "balance")
    private double balance;
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "isActive")
    private boolean isActive;
    @Column(name = "isDeleted")
    private boolean isDeleted;

    public Wallet() {
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return id == wallet.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
