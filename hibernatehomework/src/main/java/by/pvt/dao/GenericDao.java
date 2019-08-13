package by.pvt.dao;

import java.util.List;

public interface GenericDao<T> {

    T getById(Long id);

    List<T> getList();

    void update(Long id, T t);

    boolean save(T entity);

    void delete(Long id);
}
