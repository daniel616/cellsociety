package Utils;

public class CellException extends Exception{
	private static final long serialVersionUID = 1L;

	public CellException (Throwable cause, String message, Object ... values) {
        super(String.format(message, values), cause);
    }
    
    public CellException (Throwable exception) {
        super(exception);
    }
}
