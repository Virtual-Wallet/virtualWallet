package org.example.virtual_wallet.repositories;

import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.models.Sessions;
import org.example.virtual_wallet.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class SessionsRepository extends AbstractCRUDRepository<Sessions> {
    public SessionsRepository(SessionFactory sessionFactory) {
        super(Sessions.class, sessionFactory);
    }

    public Sessions getSecret(String secret) {
        try (Session session = sessionFactory.openSession()) {
            Query<Sessions> query = session.createQuery("FROM Sessions WHERE username =:username", Sessions.class);
            query.setParameter("username", secret);

            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("User", "secret", secret);
            }
            return query.list().get(0);
        }
    }
}
