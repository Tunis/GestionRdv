package metier.interfaces;

import models.Patient;

public interface ILinkDB<T> {
    boolean create(T entity);
    boolean delete(T entity);
    boolean save(T entity);
}
