package com.tisl.mpl.util;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


public class ExceptionUtil
{

	private static final Logger LOG = Logger.getLogger(ExceptionUtil.class);

	public static String getCustomizedExceptionTrace(final Exception posNBExp)
	{

		final StringBuffer message = new StringBuffer(40);
		final StringBuffer actualStackMessage = new StringBuffer(40);
		message.append(posNBExp.toString());
		//message.append("\n");  Avoid appending characters as strings in StringBuffer.append
		message.append('\n');
		final List<StackTraceElement> stackTraces = Arrays.asList(posNBExp.getStackTrace());//posNBExp.getStackTraceList();
		if (null == stackTraces)
		{
			return "";
		}
		message.append("\n\n*** Customized stack trace ***\n\n");
		actualStackMessage.append("\n\n*** Actual stack trace ***\n\n");
		try
		{
			for (final StackTraceElement element : stackTraces)
			{
				final String className = element.getClassName();
				final String str = String.format("at %s.%s(%s:%s)\n", className, element.getMethodName(), element.getFileName(),
						Integer.valueOf(element.getLineNumber()));
				if ((className.startsWith("com.tisl.mpl.") && !className.startsWith("com.tisl.mpl.exception.")))
				{
					message.append(str);
				}
				actualStackMessage.append(str);
			}
		}
		catch (final Exception e)
		{
			//Log probably not needed here
			LOG.error(e);
		}
		message.append(actualStackMessage);

		return message.toString();
	}

	public static void etailBusinessExceptionHandler(final EtailBusinessExceptions e, final ModelMap model)
	{
		String businessExMsg = MarketplacecommerceservicesConstants.EMPTY;
		if (null != e.getErrorMessage())
		{
			businessExMsg = e.getErrorMessage();
			LOG.error("***********ETAIL Business Exception is :: " + e.getErrorCode() + "- " + businessExMsg);
		}
		else
		{
			businessExMsg = MarketplacecommerceservicesConstants.BUSINESS_EXCEPTION_TEXT;
		}
		LOG.error(getCustomizedExceptionTrace(e));
		if (null != model)
		{
			model.addAttribute("errorMessage", businessExMsg);
		}

	}

	public static void etailNonBusinessExceptionHandler(final EtailNonBusinessExceptions e)
	{
		if (null != e.getRootCause())
		{
			LOG.error("***********ETAIL Non Business Exception is :: " + e.getErrorCode() + "- " + e.getErrorMessage());
			if (null != e.getRootCause().getClass())
			{
				LOG.error("*********ETAIL Non Business Exception is :: " + e.getRootCause().getClass().getCanonicalName());
				LOG.error("*********ETAIL Non Business Exception is :: " + e.getRootCause().getClass().getSimpleName());
			}
			LOG.error(getCustomizedExceptionTrace(e.getRootCause()));
		}
		if (null != e.toString())
		{
			LOG.error("************ETAIL Non Business Exception is :: " + e.toString());
		}
	}
}
