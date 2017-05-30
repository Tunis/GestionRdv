package metier.hibernate.data.exceptions;

public class DbSaveException extends Exception {
	private static final long serialVersionUID = 1L;

	public DbSaveException() {
		super("Error in saving entity");
	}

	public DbSaveException(String s) {
		super(s);
	}
}
