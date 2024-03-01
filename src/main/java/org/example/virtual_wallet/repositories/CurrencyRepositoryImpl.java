package org.example.virtual_wallet.repositories;

import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.repositories.contracts.CurrencyRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CurrencyRepositoryImpl extends AbstractCRUDRepository<Currency> implements CurrencyRepository {
    @Autowired
    public CurrencyRepositoryImpl(SessionFactory sessionFactory) {
        super(Currency.class, sessionFactory);
    }

    @Override
    public Currency get(String abbreviation) {
        try (Session session = sessionFactory.openSession()) {
            Query<Currency> query = session.createQuery("FROM Currency WHERE currency =:abbreviation", Currency.class);
            query.setParameter("abbreviation", abbreviation);

            List<Currency> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Currency", "abbreviation", abbreviation);
            }
            return result.get(0);
        }
    }
}
