package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.models.Token;
import org.example.virtual_wallet.models.User;

import java.util.List;

public interface TokenService {
    List<Token> getAllActive();
    Token getByToken(String token);
    void delete(int id);
    void validateCorrectToken(Token token, User user);
    Token create(User user);
    Token getUserToken(int id);

}
