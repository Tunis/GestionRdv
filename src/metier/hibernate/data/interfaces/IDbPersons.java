package metier.hibernate.data.interfaces;

import metier.hibernate.data.exceptions.DbGetException;

import java.util.List;

public interface IDbPersons<T> extends IDb<T> {

	List<T> getAll() throws DbGetException;
}
