package org.example.virtual_wallet.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

@Entity
@Table(name = "spending_categories")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class SpendingCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spending_category_id")
    private int spendingCategoryId;

    @Column(name = "name")
    private String name;
    @Column(name = "isDeleted")
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;
    public SpendingCategory() {
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setSpendingCategoryId(int spendingCategoryId) {
        this.spendingCategoryId = spendingCategoryId;
    }
}