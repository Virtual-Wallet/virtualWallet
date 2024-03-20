package org.example.virtual_wallet.services;

import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.exceptions.InvalidOperationException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.AuthorizationHelper;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.CardRepository;
import org.example.virtual_wallet.repositories.contracts.UserRepository;
import org.example.virtual_wallet.services.contracts.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        expirationDateIsValid(card.getExpirationDate());
        card.setUser(user);
        cardRepository.create(card);
        user.getCards().add(card);
    }

    @Override
    public void update(Card card, User user)  {
        authorizationHelper.validateUserIsCardOwner(user, card);
        verifyCardNumber(card);
        expirationDateIsValid(card.getExpirationDate());
        cardRepository.update(card);
    }

    @Override
    public void delete(int cardId, User user) {
        Card card = getById(cardId);
        authorizationHelper.validateUserIsCardOwner(user, card);
        card.setDeleted(true);
        cardRepository.update(card);
        user.getCards().remove(card);
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

    private void verifyCardNumber(Card card) {
        Card duplicate = getByCardNumber(card.getCardNumber());
        if (duplicate != null && duplicate.getCardNumber().equals(card.getCardNumber())) {
            throw new EntityDuplicateException("Card", "number", card.getCardNumber());
        }
    }

    private void expirationDateIsValid(String expirationPeriod) {
        if (expirationPeriod == null) {
            throw new IllegalArgumentException("Expiration period cannot be null"); // Or handle it differently
        }
        String[] expirationDateData = expirationPeriod.split("/");

        int expMonth = Integer.parseInt(expirationDateData[0]);
        int expYear = Integer.parseInt("20" + expirationDateData[1]);

        if (expirationDateData.length != 2 || expMonth > 12) {
            throw new InvalidOperationException("Invalid format!");
        }
        LocalDate today = LocalDate.now();


        if (today.getYear() > expYear || (today.getYear() == expYear &&
                today.getMonthValue() > expMonth)) {
            throw new InvalidOperationException("The card is expired!");
        }
    }


    }
