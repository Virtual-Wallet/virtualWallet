package org.example.virtual_wallet.filters;

import java.util.Optional;

public class UserFilterOptions {

    Optional<String> username;
    Optional<String> phoneNumber;
    Optional<String> email;
    Optional<String> sortBy;
    Optional<String> sortOrder;

    public UserFilterOptions(String username, String phoneNumber, String email, String sortBy, String sortOrder) {
        this.username = Optional.ofNullable(username);
        this.phoneNumber = Optional.ofNullable(phoneNumber);
        this.email = Optional.ofNullable(email);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public UserFilterOptions() {
        this(null, null, null, null, null);
    }



    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<String> getEmail() {
        return email;
    }
    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

}
