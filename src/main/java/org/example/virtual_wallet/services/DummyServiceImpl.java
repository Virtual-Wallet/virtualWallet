package org.example.virtual_wallet.services;

import org.example.virtual_wallet.exceptions.InvalidOperationException;
import org.example.virtual_wallet.services.contracts.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DummyServiceImpl implements DummyService {


    public DummyServiceImpl() {
    }

    @Override
    public boolean depositMoney(String expDate, double amount) {
        expirationDateIsValid(expDate);
        double random = Math.random()*100;
        return random > 50;
    }

    @Override
    public boolean withdrawMoney(String expDate, double amount) {
        expirationDateIsValid(expDate);
        double random = Math.random()*100;
        return random > 50;
    }

    public void expirationDateIsValid(String expirationPeriod) {
        String[] expirationDateData = expirationPeriod.split("/");

        int expMonth = Integer.parseInt(expirationDateData[0]);
        int expYear = Integer.parseInt("20" + expirationDateData[1]);

        if (expirationDateData.length != 2 || expMonth > 12) {
            throw new InvalidOperationException("Invalid format!");
        }
        LocalDate today = LocalDate.now();


        if (today.getYear() > expYear || (today.getYear() == expYear &&
                today.getMonthValue() > expMonth)) {
            throw new InvalidOperationException("The card is expired!");
        }
    }
}
