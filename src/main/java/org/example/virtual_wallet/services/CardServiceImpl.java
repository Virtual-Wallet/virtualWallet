package org.example.virtual_wallet.services;

import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.AuthorizationHelper;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.CardRepository;
import org.example.virtual_wallet.repositories.contracts.UserRepository;
import org.example.virtual_wallet.services.contracts.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    private final AuthorizationHelper authorizationHelper;
    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(AuthorizationHelper authorizationHelper,
                           CardRepository cardRepository) {
        this.authorizationHelper = authorizationHelper;
        this.cardRepository = cardRepository;
    }

    @Override
    public void create(Card card, User user) {
        verifyCardNumber(card);
        card.setUser(user);
        cardRepository.create(card);
        user.getCards().add(card);
    }

    @Override
    public void update(Card card, User user)  {
        authorizationHelper.validateUserIsCardOwner(user, card);
        verifyCardNumber(card);
        cardRepository.update(card);
    }

    @Override
    public void delete(int cardId, User user) {
        Card card = getById(cardId);
        authorizationHelper.validateUserIsCardOwner(user, card);
        card.setDeleted(true);
        cardRepository.update(card);
    }

    @Override
    public List<Card> getAllCards() {
        return cardRepository.getAll();
    }

    @Override
    public Card getById(int id) {
        return cardRepository.getById(id);
    }


    @Override
    public List<Card> getUserCards(User user) {
        return cardRepository.getUserCards(user.getId());
    }

    @Override
    public Card getByCardNumber(String cardNumber){
        return cardRepository.getByCardNumber(cardNumber);
    }

    private void verifyCardNumber(Card card){
        boolean isExisting = true;
        try {
            Card duplicate = getByCardNumber(card.getCardNumber());
            if(duplicate.getId() == card.getId()){
                isExisting = false;
            }
        } catch (EntityNotFoundException e) {
            isExisting = false;
        }
        if (isExisting){
            throw new EntityDuplicateException("Card", "number", card.getCardNumber());
        }

    }

}
