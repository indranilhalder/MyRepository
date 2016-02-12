package com.tisl.mpl.interceptor;

/**
 *
 */

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.util.localization.Localization;

import org.apache.log4j.Logger;


public class CartSaveInterceptor implements PrepareInterceptor
{

	private static final Logger LOG = Logger.getLogger(CartSaveInterceptor.class);


	/**
	 * @Description : The Method checks the Promotion Priority prior to its creation from HMC
	 * @param: object
	 * @param: arg1
	 * @return: void
	 */
	@Override
	public void onPrepare(final Object object, final InterceptorContext arg1) throws InterceptorException
	{
		LOG.debug(Localization.getLocalizedString("payment.cartsaveinterceptor.message"));
		if (object instanceof CartModel)
		{
			final CartModel cartModel = (CartModel) object;

			if (null != cartModel.getConvenienceCharges())
			{
				cartModel.setTotalPriceWithConv(Double.valueOf(cartModel.getTotalPrice().doubleValue()
						+ cartModel.getConvenienceCharges().doubleValue()));
			}
			else
			{
				cartModel.setConvenienceCharges(Double.valueOf(0.0));
				cartModel.setTotalPriceWithConv(cartModel.getTotalPrice());
			}
		}

	}

}
