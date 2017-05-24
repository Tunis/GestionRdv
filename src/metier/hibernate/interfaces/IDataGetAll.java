package metier.hibernate.interfaces;

import java.util.List;

public interface IDataGetAll<T> {

	List<T> getEntities();
}
