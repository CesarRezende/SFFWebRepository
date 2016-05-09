package business;

public class ErrorMessageException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message = "";
	
	public ErrorMessageException(String message){
		this.message = message;
	}

	@Override
	public String getMessage() {
        return this.message;
    }
	

}
