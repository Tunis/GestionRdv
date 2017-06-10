package metier.print;

public class PrintException extends Exception {

	private static final long serialVersionUID = 1L;

	public PrintException() {
		super("Error with print API, contact administrator.");
	}

	public PrintException(String s) {
		super(s);
	}
}
