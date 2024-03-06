package org.example.virtual_wallet.exceptions;

public class AuthorizationException extends RuntimeException{
    public AuthorizationException(String message) {
        super(message);
    }
}

