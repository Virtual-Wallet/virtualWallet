package org.example.virtual_wallet.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LogInDto {
    @NotEmpty(message = "Username can't be empty!")
    @Size(min = 2, max = 20, message = "Username should be between 2 and 20 symbols.")
    private String username;
    @Size(min = 8, message = "Password should be at least 8 symbols.")
    @NotEmpty(message = "Password can't be empty!")
    private String password;

    public LogInDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
