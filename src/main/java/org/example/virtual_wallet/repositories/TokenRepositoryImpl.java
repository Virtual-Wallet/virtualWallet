package org.example.virtual_wallet.repositories;

import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.models.Token;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.TokenRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class TokenRepositoryImpl extends AbstractCRUDRepository<Token> implements TokenRepository {

    @Autowired
    protected TokenRepositoryImpl(SessionFactory sessionFactory) {
        super(Token.class, sessionFactory);
    }

    @Override
    public Token getById(int id) {
        try (Session session = sessionFactory.openSession()) {

            Query<Token> query = session.createQuery("from  Token where  id =:id and isActive = true ", Token.class);
            query.setParameter("id", id);
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("Token", id);
            }
            return query.list().get(0);
        }
    }

    @Override
    public List<Token> getAll() {
        try(Session session = sessionFactory.openSession()){
            Query<Token> query = session.createQuery("from Token where isActive = true ", Token.class);
            return query.list();
        }
    }
    public Token getByToken(String token){
        try (Session session = sessionFactory.openSession()){
            Query<Token> query = session.createQuery("from Token where code =:token",Token.class);
            query.setParameter("token",token);
            if (query.list().isEmpty()){
                throw new EntityNotFoundException("Token","code",token);
            }
            return query.list().get(0);
        }
    }
    public Token getUserToken(int id) {
        try(Session session = sessionFactory.openSession()){
            Query<Token> query = session.createQuery("from Token t  where t.user.id =:userId and isActive = false ", Token.class);
            query.setParameter("userId",id);
            if (query.list().isEmpty()){
                throw new EntityNotFoundException("Token for User",id);
            }
            return query.list().get(0);
        }
    }


}
