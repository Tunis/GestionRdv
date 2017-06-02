package metier.hibernate.data.exceptions;

public class DbGetException extends Exception {
	private static final long serialVersionUID = 1L;

	public DbGetException() {
		super("Error in getting the entry");
	}

	public DbGetException(String s) {
		super(s);
	}
}
