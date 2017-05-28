package metier.hibernate.data.exceptions;

public class DbDeleteException extends Exception {

	public DbDeleteException() {
		super("Error in deleting entity");
	}

	public DbDeleteException(String s) {
		super(s);
	}
}
