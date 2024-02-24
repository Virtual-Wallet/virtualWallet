package org.example.virtual_wallet.repositories.contracts;

import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.Wallet;

import java.util.List;

public interface CardRepository {

    void create(Card card);

    void update(Card card);
    void delete(int id);

    public List<Card> getAll();
    public Card getById(int id);
    public List<Card> getUserCards(int userId);
    public Card getByCardNumber(String cardNumber);

}
