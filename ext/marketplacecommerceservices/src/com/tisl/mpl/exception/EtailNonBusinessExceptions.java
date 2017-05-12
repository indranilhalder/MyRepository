package com.tisl.mpl.exception;

import de.hybris.platform.util.localization.Localization;



public class EtailNonBusinessExceptions extends RuntimeException
{
	/**
	 *
	 */
	private static final long serialVersionUID = 324324242341L;
	private Exception rootCause;
	private String errorCode;

	//private final Properties prop = new Properties();
	//private static final Logger LOG = Logger.getLogger(EtailBusinessExceptions.class);


	/**
	 * @return the errorCode
	 */
	public String getErrorCode()
	{
		return errorCode;
	}


	/**
	 * @param errorCode
	 *           the errorCode to set
	 */
	public void setErrorCode(final String errorCode)
	{
		this.errorCode = errorCode;
	}


	public Exception getRootCause()
	{
		return rootCause;
	}


	public void setRootCause(final Exception rootCause)
	{
		this.rootCause = rootCause;
	}


	public EtailNonBusinessExceptions(final Exception rootCause)
	{
		super();
		//		if(null!=stackTraceList){
		//			this.stackTraceList.addAll(stackTraceList);
		//		}
		this.rootCause = rootCause;
	}

	public EtailNonBusinessExceptions(final Exception rootCause, final String errorCode)
	{
		super();
		this.errorCode = errorCode;
		this.rootCause = rootCause;
	}

	@Override
	public String toString()
	{
		return "EtailNonBusinessExceptions:" + rootCause;
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

}
