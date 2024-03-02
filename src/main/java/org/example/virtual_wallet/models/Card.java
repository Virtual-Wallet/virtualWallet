package org.example.virtual_wallet.models;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cards")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class Card {

    public static final String INVALID_CSV_FORMAT = "Invalid CSV format - please use 3 digits only.";
    public static final String INVALID_CARD_NUMBER = "Card number must be 16 digits";
    public static final String INVALID_CARDHOLDER_NAME = "Cardholder name must contain between 2 and 40 characters which are capital or small Latin letters, or spaces.";
    public static final String INVALID_EXPIRATION_DATE = "Expiration date must be in the format MM/YY.";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private int id;

    @Column(name = "card_number")
    @Pattern(regexp = "[0-9]+", message = INVALID_CARD_NUMBER)
    private String cardNumber;

    @Column(name = "card_holder")
    @Pattern(regexp = "[A-Za-z ]{2,40}", message = INVALID_CARDHOLDER_NAME)
    private String cardholderName;

    @Column(name = "expiration_date")
    @Pattern(regexp = "(0[1-9]|1[0-2])/[0-9]{2}", message = INVALID_EXPIRATION_DATE)
    private String expirationDate;

    @Column(name = "card_csv")
    @Pattern(regexp = "[\\d]{3}", message = INVALID_CSV_FORMAT)
    private String csv;

    @Column(name = "isDeleted")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

// todo do we need the table users_cards?
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "users_cards",
//            joinColumns = @JoinColumn(name = "card_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
//    private User user;

    public Card() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass().equals(o.getClass())) {
            return false;
        } else {
            Card other = (Card) o;
            return Objects.equals(cardNumber, other.cardNumber);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cardNumber);
    }
}
