package metier.hibernate.data.exceptions;

public class DbDuplicateException extends Exception {

	public DbDuplicateException() {
		super("Duplicate Entry");
	}

	public DbDuplicateException(String s) {
		super(s);
	}
}
