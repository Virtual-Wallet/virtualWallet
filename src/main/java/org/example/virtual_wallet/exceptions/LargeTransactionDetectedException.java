package org.example.virtual_wallet.exceptions;

public class LargeTransactionDetectedException extends RuntimeException{
    public LargeTransactionDetectedException(String message) {
        super(message);
    }
}
