package org.example.virtual_wallet.enums;

public enum ExternalTransactionsType {
    DEPOSIT,
    WITHDRAWAL;

    public String toString() {
        switch (this) {
            case DEPOSIT:
                return "withdrawal";
            case WITHDRAWAL:
                return "deposit";
            default:
                return "invalid";
        }
    }
}
