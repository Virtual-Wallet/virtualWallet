package org.example.virtual_wallet.repositories.contracts;

import org.example.virtual_wallet.models.Token;

import java.util.List;

public interface TokenRepository extends BaseCRUDRepository<Token> {
    Token getById(int id);
    List<Token> getAll();
    Token getByToken(String token);
    List<Token> getUserTokens(int id);
}
