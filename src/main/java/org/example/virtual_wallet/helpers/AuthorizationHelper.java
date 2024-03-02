package org.example.virtual_wallet.helpers;

import org.example.virtual_wallet.exceptions.UnauthorizedOperationException;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.Wallet;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHelper {

    private static final String INVALID_CARD_OWNER = "Invalid card owner!";
    private static final String INVALID_WALLET_OWNER = "Invalid wallet owner!";



    public AuthorizationHelper() {
    }

    public void validateUserIsCardOwner(User executor, Card card) {
        if (card.getUser().getId() != executor.getId()) {
            throw new UnauthorizedOperationException(INVALID_CARD_OWNER);
        }
    }

    public void validateUserIsWalletOwner(User executor, Wallet wallet) {
        if (wallet.getUser().getId() != executor.getId()) {
            throw new UnauthorizedOperationException(INVALID_WALLET_OWNER);
        }
    }

}
