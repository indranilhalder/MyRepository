package com.tisl.mpl.interceptor;

/**
 *@author TCS
 */

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import com.tisl.mpl.core.model.SizesystemModel;
import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.util.ExceptionUtil;




public class SizeStandardInterceptor implements PrepareInterceptor
{



	/**
	 * @Javadoc
	 * @Description : The Method sets the Product Color hex code for a variant Product
	 * @param: object
	 * @param: arg1
	 * @return: void
	 */
	@Override
	public void onPrepare(final Object object, final InterceptorContext arg1) throws InterceptorException
	{
		try
		{

			if (object instanceof SizesystemModel)
			{
				final SizesystemModel obj = (SizesystemModel) object;
				if (StringUtils.isNotBlank(obj.getFrom()) && StringUtils.isNotBlank(obj.getTo()))
				{
					final String name = obj.getFrom().toUpperCase() + "to" + obj.getTo().toUpperCase();
					obj.setName(name);
				}


			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}

	}
}
