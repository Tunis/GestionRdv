package metier.hibernate.data.exceptions;

public class DbCreateException extends Exception {

	public DbCreateException(){super("Error in creating new entry");}

	public DbCreateException(String s) {
		super(s);
	}
}
