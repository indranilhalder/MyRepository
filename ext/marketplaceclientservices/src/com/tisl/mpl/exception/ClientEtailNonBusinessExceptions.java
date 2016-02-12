/**
 *
 */
package com.tisl.mpl.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * @author TCS
 *
 */
public class ClientEtailNonBusinessExceptions extends RuntimeException
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1312413124143141L;

	private String errorCode;
	private Exception exceptionCause;
	private String stackTraceMessage;
	private String externalMessage;

	private final Properties prop = new Properties();
	private static final Logger LOG = Logger.getLogger(ClientEtailNonBusinessExceptions.class);

	public ClientEtailNonBusinessExceptions()
	{
		super();
	}

	public ClientEtailNonBusinessExceptions(final String errorCode)
	{
		super();
		this.errorCode = errorCode;
	}

	public ClientEtailNonBusinessExceptions(final Exception rootCause)
	{
		super();
		this.exceptionCause = rootCause;
	}

	public ClientEtailNonBusinessExceptions(final String errorCode, final Exception exceptionCause)
	{
		super();
		this.errorCode = errorCode;
		this.exceptionCause = exceptionCause;
	}


	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(final String errorCode)
	{
		this.errorCode = errorCode;
	}

	public Throwable getExceptionCause()
	{
		return exceptionCause;
	}

	public void setExceptionCause(final Exception exceptionCause)
	{
		this.exceptionCause = exceptionCause;
	}

	public String getStackTraceMessage()
	{
		return stackTraceMessage;
	}

	public void setStackTraceMessage(final String stackTraceMessage)
	{
		this.stackTraceMessage = stackTraceMessage;
	}


	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage()
	{
		//Will change
		final String propFileName = "errorcodemessage.properties";
		//In J2EE, getClassLoader() might not work as expected. Use Thread.currentThread().getContextClassLoader() instead.
		//final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFileName);

		if (inputStream == null)
		{
			try
			{
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			catch (final Exception e)
			{
				LOG.info(e);
			}
		}

		//Will change
		try
		{
			prop.load(inputStream);
		}
		catch (final IOException e)
		{
			LOG.info(e);
		}
		return prop.getProperty(errorCode);
	}


	/**
	 * @return the externalMessage
	 */
	public String getExternalMessage()
	{
		return externalMessage;
	}


	/**
	 * @param externalMessage
	 *           the externalMessage to set
	 */
	public void setExternalMessage(final String externalMessage)
	{
		this.externalMessage = externalMessage;
	}

}
