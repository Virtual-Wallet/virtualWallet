package org.example.virtual_wallet.models.dtos;

public class TransactionDto {
    private String username;
    private String email;
    private String phoneNumber;
    private double amount;
    private String currency;
    private String category;

    public TransactionDto(String username,
                          String email,
                          String phoneNumber,
                          double amount,
                          String currency,
                          String category) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
    }

    public TransactionDto() {
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCategory() {
        return category;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
