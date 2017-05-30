package metier.hibernate.data.exceptions;

public class DbDeleteException extends Exception {
	private static final long serialVersionUID = 1L;

	public DbDeleteException() {
		super("Error in deleting entity");
	}

	public DbDeleteException(String s) {
		super(s);
	}
}
