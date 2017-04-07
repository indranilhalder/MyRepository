/**
 * 
 */
package com.hybris.dtocache.exception;

/**
 * @author giovanni.ganassin@sap.com
 * Jan 19, 2016 10:33:52 AM
 *
 */
public class DtoCacheException extends Exception
{

	/**
	 * 
	 */
	public DtoCacheException()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public DtoCacheException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public DtoCacheException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DtoCacheException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DtoCacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
