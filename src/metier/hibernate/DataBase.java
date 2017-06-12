package metier.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DataBase {
    private static final SessionFactory sessionFactory;
	private static Session SESSION;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            // "jdbc:mysql://mysql.hostinger.fr/u508684160_gesti?serverTimezone=" + TimeZone.getDefault().getID()
            // jdbc:derby:bdd;create=true
            configuration.setProperty("hibernate.connection.url", "jdbc:derby:bdd;create=true");

            sessionFactory = configuration.buildSessionFactory();
	        SESSION = sessionFactory.openSession();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
	    if (!SESSION.isConnected())
		    SESSION = sessionFactory.openSession();
	    return SESSION;

    }

    public static void close(){
        sessionFactory.close();
    }
}
