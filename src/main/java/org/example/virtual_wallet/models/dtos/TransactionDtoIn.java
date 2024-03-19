package org.example.virtual_wallet.models.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.NumberFormat;

public class TransactionDtoIn {

    @NotBlank
    private String targetUserIdentity;

    @NotNull
    @Positive
    private Double amount;

    @Nullable
    private String categoryName;

    private String currency;

    public TransactionDtoIn() {
    }

    public String getTargetUserIdentity() {
        return targetUserIdentity;
    }

    public void setTargetUserIdentity(String targetUserIdentity) {
        this.targetUserIdentity = targetUserIdentity;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Nullable
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(@Nullable String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
