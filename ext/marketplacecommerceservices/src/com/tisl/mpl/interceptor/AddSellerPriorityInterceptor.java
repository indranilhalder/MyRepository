/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import com.tisl.mpl.core.enums.SellerPriorityEnum;
import com.tisl.mpl.core.model.MplSellerPriorityModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class AddSellerPriorityInterceptor implements PrepareInterceptor
{

	/**
	 *
	 */


	/*
	 * (non-Javadoc)
	 */
	@Override
	public void onPrepare(final Object modelArg, final InterceptorContext ctx) throws InterceptorException
	{
		// YTODO Auto-generated method stub
		try
		{
			if (modelArg instanceof MplSellerPriorityModel)
			{
				final MplSellerPriorityModel sellerPriority = (MplSellerPriorityModel) modelArg;
				if (null == sellerPriority.getPriorityStatus())
				{
					sellerPriority.setPriorityStatus(SellerPriorityEnum.NEW);
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
