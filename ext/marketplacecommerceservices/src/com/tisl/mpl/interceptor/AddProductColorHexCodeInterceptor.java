package com.tisl.mpl.interceptor;

/**
 *@author TCS
 */

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.util.ExceptionUtil;




public class AddProductColorHexCodeInterceptor implements PrepareInterceptor
{

	@Autowired
	private ConfigurationService configurationService;

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

			if (object instanceof PcmProductVariantModel)
			{
				final PcmProductVariantModel product = (PcmProductVariantModel) object;
				if (product.getColour() != null && !product.getColour().isEmpty())
				{
					final String inputColours = configurationService.getConfiguration().getString(
							"colorhexcode." + product.getColour().toLowerCase());

					if (inputColours != null)
					{
						product.setColourHexCode(inputColours);
					}

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
