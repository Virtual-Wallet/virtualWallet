package org.example.virtual_wallet.exceptions;

public class InsufficientAmountException extends RuntimeException{
    public InsufficientAmountException(double amount) {
        super(String.format("The amount in your virtual wallet is not sufficient. Current amount: %.2f", amount));
    }
}