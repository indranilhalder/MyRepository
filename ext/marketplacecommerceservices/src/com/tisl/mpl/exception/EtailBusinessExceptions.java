package com.tisl.mpl.exception;

import de.hybris.platform.util.localization.Localization;

import java.util.Properties;

import org.apache.log4j.Logger;


public class EtailBusinessExceptions extends RuntimeException
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
	private static final Logger LOG = Logger.getLogger(EtailBusinessExceptions.class);

	public EtailBusinessExceptions()
	{
		super();
	}

	public EtailBusinessExceptions(final String errorCode)
	{
		super();
		this.errorCode = errorCode;
	}


	public EtailBusinessExceptions(final String errorCode, final Exception exceptionCause)
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
		//		final String propFileName = "errorcodemessage.properties";
		//		//In J2EE, getClassLoader() might not work as expected. Use Thread.currentThread().getContextClassLoader() instead.
		//		//final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		//		final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFileName);
		//
		//
		//		if (inputStream == null)
		//		{
		//			try
		//			{
		//				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		//			}
		//			catch (final Exception e)
		//			{
		//				LOG.info(e);
		//			}
		//		}
		//
		//		//Will change
		//		try
		//		{
		//			prop.load(inputStream);
		//		}
		//		catch (final IOException e)
		//		{
		//			LOG.info(e);
		//		}
		//		return prop.getProperty(errorCode);

		final String errorMessage = Localization.getLocalizedString(errorCode);
		return errorMessage == null ? "" : errorMessage;
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
