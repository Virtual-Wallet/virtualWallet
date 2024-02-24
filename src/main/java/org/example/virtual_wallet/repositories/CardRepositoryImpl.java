package org.example.virtual_wallet.repositories;

import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.Wallet;
import org.example.virtual_wallet.repositories.contracts.CardRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CardRepositoryImpl extends AbstractCRUDRepository<Card> implements CardRepository {

    public CardRepositoryImpl(SessionFactory sessionFactory) {
        super(Card.class, sessionFactory);
    }

    @Override
    public List<Card> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Card> query = session.createQuery("SELECT c FROM Card c WHERE c.isDeleted = false", Card.class);
            return query.list();
        }
    }

    @Override
    public Card getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Card card = session.createQuery(
                            "SELECT c FROM Card c WHERE c.id = :id AND c.isDeleted = false", Card.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (card == null) {
                throw new EntityNotFoundException("Wallet", id);
            }

            transaction.commit();
            return card;
        }
    }

    @Override
    public List<Card> getUserCards(int userId){
        try (Session session = sessionFactory.openSession()) {
            Query<Card> query = session.createQuery("SELECT c FROM Card c WHERE c.user.id = :userId AND c.isDeleted = false", Card.class);
            return query.list();
        }
    }

    @Override
    public Card getByCardNumber(String cardNumber){
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Card card = session.createQuery(
                            "SELECT c FROM Card c WHERE c.cardNumber = :cardNumber AND c.isDeleted = false", Card.class)
                    .uniqueResult();

            if (card == null) {
                throw new EntityNotFoundException("Wallet", "cardNumber", cardNumber);
            }

            transaction.commit();
            return card;
        }
    }

}
