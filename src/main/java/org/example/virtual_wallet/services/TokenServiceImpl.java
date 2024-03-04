package org.example.virtual_wallet.services;

import org.example.virtual_wallet.exceptions.InvalidTokenException;
import org.example.virtual_wallet.models.Token;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.TokenRepository;
import org.example.virtual_wallet.services.contracts.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @Override
    public List<Token> getAllActive() {
        return tokenRepository.getAll();
    }

    @Override
    public List<String> getUserTokens(int userId) {
        return tokenRepository.getUserTokens(userId).stream()
                .map(Token::getCode)
                .collect(Collectors.toList());
    }

    @Override
    public Token getByToken(String token) {
        return tokenRepository.getByToken(token);
    }

    @Override
    public void delete(int id) {
        Token token = tokenRepository.getById(id);
        token.setActive(false);
        tokenRepository.update(token);
    }

    @Override
    public void validateCorrectToken(Token token, User user) {
        if (!getUserTokens(user.getId()).contains(token.getCode())){
            throw new InvalidTokenException("This verification code is invalid!");
        }
        if (token.getExpirationTime().isBefore(LocalDateTime.now())){
            throw new InvalidTokenException("This verification code is expired!");
        }
    }
}
