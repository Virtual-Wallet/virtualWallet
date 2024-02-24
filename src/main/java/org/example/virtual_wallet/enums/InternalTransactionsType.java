package org.example.virtual_wallet.enums;

public enum InternalTransactionsType {
    INCOMING,
    OUTGOING;

    public String toString(){
        switch (this){
            case INCOMING:
                return "incoming";
            case OUTGOING:
                return "outgoing";
            default:
                return "invalid";
        }
    }
}
