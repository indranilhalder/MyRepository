package com.tisl.mpl.interceptor;

/**
 *
 */

import de.hybris.platform.core.model.order.AbstractOrderModel;
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
		if (object instanceof AbstractOrderModel) //Changes made from cartModel to abstractOrderModel TPR-629
		{
			final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) object;

			if (null != abstractOrderModel.getConvenienceCharges())
			{
				abstractOrderModel.setTotalPriceWithConv(Double.valueOf(abstractOrderModel.getTotalPrice().doubleValue()
						+ abstractOrderModel.getConvenienceCharges().doubleValue()));
			}
			else
			{
				abstractOrderModel.setConvenienceCharges(Double.valueOf(0.0));
				abstractOrderModel.setTotalPriceWithConv(abstractOrderModel.getTotalPrice());
			}
		}

	}

}
