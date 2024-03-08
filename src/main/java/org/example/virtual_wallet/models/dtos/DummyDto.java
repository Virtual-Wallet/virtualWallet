package org.example.virtual_wallet.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DummyDto {

    @Pattern(regexp = "[0-9]+", message = "Card number must contains only digits")
    @Size(min = 16, max = 16, message = "Card number must be 16 digits")
    private String cardNumber;

    @NotEmpty(message = "Card CSV can't be empty!")
    @Pattern(regexp = "[\\d]{3}", message = "Card csv must be 3 digits")
    private String cardCheck;

    @NotEmpty(message = "Card Expiration Date can't be empty!")
    @Pattern(regexp = "(0[1-9]|1[0-2])/[0-9]{2}", message = "Expiration date must be in the format MM/YY.")
    private String expirationDate;

    private double amount;

    public DummyDto() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardCheck() {
        return cardCheck;
    }

    public void setCardCheck(String cardCheck) {
        this.cardCheck = cardCheck;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

