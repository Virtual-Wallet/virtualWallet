package org.example.virtual_wallet.services;

import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.CardRepository;
import org.example.virtual_wallet.services.contracts.CardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public void create(Card card) {
        cardRepository.create(card);
    }

    @Override
    public void update(Card card)  {
        cardRepository.update(card);
    }

    @Override
    public void delete(int userId) {
        cardRepository.delete(userId);
    }

    @Override
    public Card getById(int id) {
        return cardRepository.getById(id);
    }


    @Override
    public List<Card> getUserCards(User user, User requester) {
//        if(!requester.isAdmin() && user.getId() != requester.getId()){
//            throw new UnauthorizedOperationException(INVALID_CARD_OWNER);
//        }
        return cardRepository.getUserCards(user.getId());
    }

    @Override
    public Card getByCardNumber(String cardNumber){
        return cardRepository.getByCardNumber(cardNumber);
    }



}
