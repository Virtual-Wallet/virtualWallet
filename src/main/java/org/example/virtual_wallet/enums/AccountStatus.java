package org.example.virtual_wallet.enums;

public enum AccountStatus {

    PENDING_EMAIL,
    EMAIL_CONFIRMED,
    PENDING_ID,
    ACTIVE;

    public String toString() {
        switch (this) {
            case PENDING_EMAIL:
                return "pending email";
            case EMAIL_CONFIRMED:
                return "email confirmed";
            case PENDING_ID:
                return "pending id";
            case ACTIVE:
                return "active";
            default:
                return "invalid";
        }
    }
}
