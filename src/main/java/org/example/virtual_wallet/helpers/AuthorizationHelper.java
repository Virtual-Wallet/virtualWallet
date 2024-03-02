package org.example.virtual_wallet.helpers;

import org.example.virtual_wallet.exceptions.UnauthorizedOperationException;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHelper {

    private static final String INVALID_CARD_OWNER = "Invalid card owner!";


    public AuthorizationHelper() {
    }

    public void validateUserIsCardOwner(User executor, Card card) {
        if (card.getUser().getId() != executor.getId()) {
            throw new UnauthorizedOperationException(INVALID_CARD_OWNER);
        }
    }

}
