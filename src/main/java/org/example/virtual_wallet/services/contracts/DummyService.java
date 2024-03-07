package org.example.virtual_wallet.services.contracts;

import java.time.LocalDate;

public interface DummyService {
    boolean depositMoney(String expDate, double amount);

    boolean withdrawMoney(String expDate, double amount);
}

