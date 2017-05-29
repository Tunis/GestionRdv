package metier.interfaces;

public interface IDataGettersSetters<T> {

    T getEntity(long id);

    boolean deleteEntity(T p);
}
