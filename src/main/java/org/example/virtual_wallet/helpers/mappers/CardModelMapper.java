package org.example.virtual_wallet.helpers.mappers;

import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.CardDto;
import org.example.virtual_wallet.models.dtos.UserDto;
import org.example.virtual_wallet.services.contracts.CardService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CardModelMapper {

    private final CardService cardService;
    private final UserService userService;

    @Autowired
    public CardModelMapper(CardService cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    public Card dtoCardCreate(CardDto cardDto, User user) {
        Card card = new Card();
        dtoToObject(cardDto, card);
        card.setUser(user);
        return card;
    }

    public Card dtoCardUpdate(CardDto cardDto, int cardId) {
        Card card = cardService.getById(cardId);
        dtoToObject(cardDto,card);
        return card;
    }

    public CardDto toCardDto(Card card){
        CardDto cardDto = new CardDto();
        cardDto.setCardCsv(card.getCsv());
        cardDto.setCardHolder(card.getCardholderName());
        cardDto.setCardNumber(card.getCardNumber());
        cardDto.setExpirationDate(card.getExpirationDate());
        return cardDto;
    }

    private static void dtoToObject(CardDto cardDto, Card card) {
        card.setCardNumber(cardDto.getCardNumber());
        card.setCsv(cardDto.getCardCsv());
        card.setCardholderName(cardDto.getCardHolder());
        card.setExpirationDate(cardDto.getExpirationDate());
    }
}