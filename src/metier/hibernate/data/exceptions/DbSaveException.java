package metier.hibernate.data.exceptions;

public class DbSaveException extends Exception {

	public DbSaveException() {
		super("Error in saving entity");
	}

	public DbSaveException(String s) {
		super(s);
	}
}
