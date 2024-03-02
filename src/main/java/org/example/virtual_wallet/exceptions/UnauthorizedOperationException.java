package org.example.virtual_wallet.exceptions;

public class UnauthorizedOperationException extends RuntimeException{
    public UnauthorizedOperationException(String message){
        super(message);
    }
}
