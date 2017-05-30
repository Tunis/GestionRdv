package metier.hibernate.data.exceptions;

public class DbCreateException extends Exception {
	private static final long serialVersionUID = 1L;

	public DbCreateException(){super("Error in creating new entry");}

	public DbCreateException(String s) {
		super(s);
	}
}
