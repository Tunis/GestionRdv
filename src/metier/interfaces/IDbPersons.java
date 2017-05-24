package metier.interfaces;

import java.util.List;

public interface IDbPersons<T> extends IDb<T> {

	List<T> getAll();
	boolean delete(T entity);
}
