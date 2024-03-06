package org.example.virtual_wallet.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CurrencyDto {
    @NotEmpty (message = "Currency abbreviation should not be empty.")
    @Size(min = 3, max = 3, message = "Currency abbreviation should be 3 letters")
    private String abbreviation;

    public CurrencyDto() {
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
