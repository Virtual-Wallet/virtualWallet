package org.example.virtual_wallet.services;

import org.example.virtual_wallet.exceptions.InvalidTokenException;
import org.example.virtual_wallet.models.Token;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.TokenRepository;
import org.example.virtual_wallet.services.contracts.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {
    private static final String TOKEN_EXPIRATION_MSG = "This verification code is expired!";
    private static final String WRONG_TOKEN_CODE_MSG = "This verification code is invalid!";
    private static final int TOKEN_EXPIRATION_TIME = 5;
    private final TokenRepository tokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @Override
    public List<Token> getAllActive() {
        return tokenRepository.getAll();
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
        checkIfTokenIsValid(token);
        if (!tokenRepository.getUserToken(user.getId()).getCode().equals(token.getCode())) {
            throw new InvalidTokenException(WRONG_TOKEN_CODE_MSG);
        }
    }

    @Override
    public Token create(User user) {
        Token token = new Token();
        token.setCode(generateNewToken());
        token.setUser(user);
        token.setExpirationTime(LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_TIME));
        tokenRepository.create(token);
        return token;
    }

    private void checkIfTokenIsValid(Token token) {
        if (token.getExpirationTime().isBefore(LocalDateTime.now())) {
            token.setActive(false);
            tokenRepository.update(token);
            throw new InvalidTokenException(TOKEN_EXPIRATION_MSG);
        }
    }

    private String generateNewToken() {
        byte[] randomBytes = new byte[6];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
