package org.example.virtual_wallet.enums;

public enum TransferType {
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
