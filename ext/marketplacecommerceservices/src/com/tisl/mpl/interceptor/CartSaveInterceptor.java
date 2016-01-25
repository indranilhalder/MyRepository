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
			final CartModel cart = (CartModel) object;
			//
			//			if (null != cart.getDeliveryCost() && cart.getDeliveryCost().doubleValue() > 0.0)
			//			{
			//				cart.setTotalPrice(Double.valueOf(cart.getTotalPrice().doubleValue() + cart.getDeliveryCost().doubleValue()));
			//			}
			//			else
			//			{
			//				//cart.setTotalPrice(cart.getSubtotal());
			//			}
			if (null != cart.getConvenienceCharges())
			{
				cart.setTotalPriceWithConv(Double.valueOf(cart.getTotalPrice().doubleValue()
						+ cart.getConvenienceCharges().doubleValue()));
			}
			else
			{
				cart.setTotalPriceWithConv(cart.getTotalPrice());
			}
		}

	}


}
