package com.opteamix.library.exception;

public class LibraryException extends Exception{
	
	
	private static final long serialVersionUID = 1997753363232807009L;

    private Integer errorCode;

    public Integer getErrorCode() {
		return errorCode;
	}

	public LibraryException()
	{
	}

	public LibraryException(String message)
	{
		super(message);
	}

	public LibraryException(Throwable cause)
	{
		super(cause);
	}

	public LibraryException(String message, Throwable cause)
	{
		super(message, cause);
	}
	public LibraryException(String message, Integer errorCode)
	{
		super(message);
		this.errorCode = errorCode;
	}
	

	public LibraryException(String message, Throwable cause, 
                                       boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}


}
