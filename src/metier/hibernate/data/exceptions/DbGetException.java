package metier.hibernate.data.exceptions;

public class DbGetException extends Exception {
	public DbGetException() {
		super("Error in getting the entry");
	}

	public DbGetException(String s) {
		super(s);
	}
}
