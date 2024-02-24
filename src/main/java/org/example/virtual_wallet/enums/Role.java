package org.example.virtual_wallet.enums;

public enum Role {
    ADMIN,
    REGULAR,
    BANNED;

    public String toString() {
        switch (this) {
            case ADMIN:
                return "admin";
            case REGULAR:
                return "regular";
            case BANNED:
                return "banned";
            default:
                return "invalid";
        }
    }
}
