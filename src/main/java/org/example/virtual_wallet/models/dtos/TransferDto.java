package org.example.virtual_wallet.models.dtos;

public class TransferDto {
    private double amount;
    private String cardNumber;
    private String currency;

    public TransferDto(double amount,
                       String cardNumber,
                       String currency) {
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
