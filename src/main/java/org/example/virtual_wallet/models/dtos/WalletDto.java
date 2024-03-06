package org.example.virtual_wallet.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.example.virtual_wallet.models.Currency;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class WalletDto {

    @NotEmpty
//    @Pattern(regexp = "(USD|EUR|BGN)", message = "Wallet currency can be USD, EUR or BGN!")
    private String currency;

    public WalletDto() {
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
