/**
 *
 */
package com.tisl.mpl.exception;

/**
 * @author TUL
 *
 */
public class QCServiceCallException extends Exception
{

	@SuppressWarnings("unused")
	private Exception exceptionCause;

	public QCServiceCallException()
	{
		super();
	}


	public QCServiceCallException(final Exception rootCause)
	{
		super();
		this.exceptionCause = rootCause;
	}

}
