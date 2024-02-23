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

    // alternative to ManyToOne
//    @Formula("(SELECT c.currency FROM currencies AS c WHERE c.id = id)")
//    private String currency;
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @OneToOne
    @JoinTable(
            name = "users",
            joinColumns = @JoinColumn(name = "user_id")
    )
    private User user;

    @Column(name = "isActive")
    private boolean isActive;

    public Wallet() {
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
