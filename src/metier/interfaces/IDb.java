package metier.interfaces;

public interface IDb<T> {

	boolean save(T entity);
}
