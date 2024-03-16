package org.example.virtual_wallet.models.dtos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import org.example.virtual_wallet.models.Currency;

@Entity()
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class OrderDto {
    @Id
    private String id;
    private String type;
    //    private int category;
//    private int contractor;
//    private Timestamp date;
    private int currency;
    private double amount;

    public OrderDto(
            String type,
//            int category,
//            int contractor,
//            Timestamp date,
            int currency,
            double amount) {
        this.type = type;
//        this.category = category;
//        this.contractor = contractor;
//        this.date = date;
        this.currency = currency;
        this.amount = amount;
    }

    public OrderDto() {
    }

    public String getType() {
        return type;
    }

//    public int getCategory() {
//        return category;
//    }
//
//    public int getContractor() {
//        return contractor;
//    }
//
//    public Timestamp getDate() {
//        return date;
//    }
//
    public int getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }
}
