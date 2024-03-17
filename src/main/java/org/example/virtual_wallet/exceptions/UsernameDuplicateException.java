package org.example.virtual_wallet.exceptions;

public class UsernameDuplicateException extends RuntimeException {
    public UsernameDuplicateException(String type, String attribute, String value) {
        super(String.format("%s with %s %s already exists.", type, attribute, value));
    }
}
