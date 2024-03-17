package org.example.virtual_wallet.exceptions;

public class EmailDuplicateException extends RuntimeException {
    public EmailDuplicateException(String type, String attribute, String value) {
        super(String.format("%s with %s %s already exists.", type, attribute, value));
    }
}
