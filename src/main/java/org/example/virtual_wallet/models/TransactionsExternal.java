//package org.example.virtual_wallet.models;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import jakarta.persistence.*;
//
//import java.time.LocalDate;
//
//@Entity
//@Table(name = "external_transactions")
//public class TransactionsExternal {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "external_transactions_id")
//    private int id;
//
//    @Column(name = "type")
//    @Enumerated(EnumType.STRING)
//    private String type;
//
//    @Column(name = "user_id")
//    private int userId;
//
//    @Column(name = "card_id")
//    private int cardId;
//
//    @JsonFormat (pattern = "dd-mm-yyyy")
//    @Column(name = "timestamp")
//    private LocalDate timestamp;
//
//    @Column (name = "currency_id")
//    private int currencyId;
//
//    @Column (name = "amount")
//    private double amount;
//
//    public TransactionsExternal() {
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public int getCardId() {
//        return cardId;
//    }
//
//    public LocalDate getTimestamp() {
//        return timestamp;
//    }
//
//    public int getCurrencyId() {
//        return currencyId;
//    }
//
//    public double getAmount() {
//        return amount;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public void setCardId(int cardId) {
//        this.cardId = cardId;
//    }
//
//    public void setTimestamp(LocalDate timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public void setCurrencyId(int currencyId) {
//        this.currencyId = currencyId;
//    }
//
//    public void setAmount(double amount) {
//        this.amount = amount;
//    }
//}
