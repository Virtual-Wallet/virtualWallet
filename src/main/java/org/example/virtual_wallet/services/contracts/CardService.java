package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;

import java.util.List;

public interface CardService {
    public void create(Card card, User user);
    public void update(Card card, User user);
    public void delete(int cardId, User user);
    public List<Card> getAllCards();
    public Card getById(int id);
    public List<Card> getUserCards(User user);
    public Card getByCardNumber(String cardNumber);
}
