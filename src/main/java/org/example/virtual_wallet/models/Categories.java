package org.example.virtual_wallet.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

@Entity
@Table(name = "spending_categories")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spending_category_id")
    private int spendingCategoryId;

    @Column(name = "name")
    private String name;

    public Categories() {
    }

    public int getSpendingCategoryId() {
        return spendingCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpendingCategoryId(int spendingCategoryId) {
        this.spendingCategoryId = spendingCategoryId;
    }
}