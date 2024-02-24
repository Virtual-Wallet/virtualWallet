package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;

import java.util.List;

public interface CardService {
    public void create(Card card);
    public void update(Card card);
    public void delete(int cardId);
    public Card getById(int id);
    public List<Card> getUserCards(User user, User requester);
    public Card getByCardNumber(String cardNumber);
}
