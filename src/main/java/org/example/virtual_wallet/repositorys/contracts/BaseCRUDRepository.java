package org.example.virtual_wallet.repositorys.contracts;

public interface BaseCRUDRepository <T> extends BaseReadRepository<T>{
    void create(T entity);
    void update(T entity);
    void delete(int id);
}
