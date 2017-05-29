package metier.hibernate.data.interfaces;

import metier.hibernate.data.exceptions.DbSaveException;

public interface IDb<T> {

	void save(T entity) throws DbSaveException;
}
