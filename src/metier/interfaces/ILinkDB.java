package metier.interfaces;

public interface ILinkDB<T> {
    boolean create(T entity);
    boolean delete(T entity);
    boolean save(T entity);
}
