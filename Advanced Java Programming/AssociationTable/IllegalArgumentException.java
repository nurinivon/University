/*
 * IllegalArgumentException is an extension of Exception class.
 * an IllegalArgumentException instance could be created with default constructor or with message constructor with string as parameter.
 */
public class IllegalArgumentException extends Exception{
	//the following attribute is an automatic attribute added by java for Exception extensions
	private static final long serialVersionUID = 1L;
	
	public IllegalArgumentException() {
		
	}
	
	public IllegalArgumentException(String message) {
		super(message);
	}
}
