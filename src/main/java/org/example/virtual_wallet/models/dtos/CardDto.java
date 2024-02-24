package org.example.virtual_wallet.models.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CardDto {

    @Pattern(regexp = "[0-9]+", message = "Card number must contains only digits")
    @Size(min = 16, max = 16, message = "Card number must be 16 digits")
    private String cardNumber;

    @Pattern(regexp = "[A-Za-z ]{2,40}", message = "Cardholder name must contain between 2 and 40 characters which are capital or small Latin letters, or spaces.")
    private String cardHolder;

    @Pattern(regexp = "(0[1-9]|1[0-2])/[0-9]{2}", message = "Expiration date must be in the format MM/YY.")
    private String expirationDate;

    @Pattern(regexp = "[\\d]{3}", message = "Card number must be 16 digits")
    private String cardCsv;

    public CardDto() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCardCsv() {
        return cardCsv;
    }

    public void setCardCsv(String cardCsv) {
        this.cardCsv = cardCsv;
    }
}
