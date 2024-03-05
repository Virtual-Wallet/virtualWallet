package org.example.virtual_wallet.repositories;

import org.example.virtual_wallet.enums.RoleType;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.models.Role;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.contracts.RoleRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepositoryImpl extends AbstractCRUDRepository<Role> implements RoleRepository {
    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        super(Role.class, sessionFactory);
    }


    @Override
    public Role getByName(RoleType roleType) {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> query = session.createQuery("from Role r where r.roleType = :roleType", Role.class);
            query.setParameter("roleType", roleType);
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("This role doesn't exist!");
            }
            return query.list().get(0);
        }
    }


    @Override
    public Role getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Role result = session.get(Role.class, id);
            if (result == null) {
                throw new EntityNotFoundException("Role", id);
            }
            return result;
        }
    }
}
