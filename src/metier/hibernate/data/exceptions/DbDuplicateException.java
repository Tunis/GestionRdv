package metier.hibernate.data.exceptions;

public class DbDuplicateException extends Exception {
	private static final long serialVersionUID = 1L;

	public DbDuplicateException() {
		super("Duplicate Entry");
	}

	public DbDuplicateException(String s) {
		super(s);
	}
}
