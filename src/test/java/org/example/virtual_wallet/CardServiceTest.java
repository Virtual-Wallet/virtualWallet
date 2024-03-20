package org.example.virtual_wallet;

import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.InvalidOperationException;
import org.example.virtual_wallet.exceptions.UnauthorizedOperationException;
import org.example.virtual_wallet.helpers.AuthorizationHelper;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.CardRepository;
import org.example.virtual_wallet.services.CardServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    AuthorizationHelper authorizationHelper;
    @Mock
    CardRepository cardRepository;
    @InjectMocks
    CardServiceImpl cardService;

    @Test
    public void getByUserId_Should_CallRepository() {
        User user = Helpers.createMockUserRegular();

        //Act
        cardService.getUserCards(user);

        //Assert
        Mockito.verify(cardRepository, Mockito.times(1)).getUserCards(user.getId());
    }

    @Test
    public void create_Should_Throw_When_ExpirationDateIsInvalid() {
        Card card = new Card();
        card.setExpirationDate("13/22"); // Invalid expiration date format

        // Assert
        assertThrows(InvalidOperationException.class, () -> cardService.create(card, new User()));
    }

    @Test
    public void create_Should_AddCardToUser_When_CardIsCreated() {
        Card card = Helpers.createMockCard();
        card.setCardNumber("1234567890123456");
        card.setExpirationDate("2/25");
        User user = Helpers.createMockUserRegular();

        // Call the method under test
        cardService.create(card, user);

        Mockito.verify(cardRepository, Mockito.times(1)).create(card);

        // Verify that the card is added to the user's cards
        Assertions.assertTrue(user.getCards().contains(card));
    }
    @Test
    public void getById_Should_CallRepository() {
        // Create a mock card and an id
        int id = 1;
        Card mockCard = Helpers.createMockCard();

        // Mock the behavior of the cardRepository
        Mockito.when(cardRepository.getById(id)).thenReturn(mockCard);

        // Call the method under test
        Card result = cardService.getById(id);

        // Verify that cardRepository's getById method is called with the provided id
        Mockito.verify(cardRepository, Mockito.times(1)).getById(id);

        // Assert that the returned card is the same as the mock card
        Assertions.assertEquals(mockCard.getId(), result.getId());
    }
}


