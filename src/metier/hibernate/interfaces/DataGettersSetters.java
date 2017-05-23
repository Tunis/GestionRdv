package metier.hibernate.interfaces;

public interface DataGettersSetters<T> {

    T getEntity(long id);

    boolean deleteEntity(T p);
}
