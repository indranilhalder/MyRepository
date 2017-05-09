package com.tisl.mpl.jalo;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class PaymentModeRestriction extends GeneratedPaymentModeRestriction
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PaymentModeRestriction.class.getName());

	/**
	 * @param ctx
	 * @param type
	 * @param allAttributes
	 * @exception JaloBusinessException
	 */
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item
		final Item item = super.createItem(ctx, type, allAttributes);
		// business code placed here will be executed after the item was created
		// and return the item
		return item;
	}

	///////////////////TPR-4461///////////////////
	/**
	 * Coupon Payment Specific Evaluation Logic
	 *
	 * @param cart
	 */


	@Override
	protected boolean isFulfilledInternal(final AbstractOrder cart)
	{
		//TPR-4461 starts here for CALL CENTER COD CHECK
		Object cartChannel = null;
		final String codmode = "COD";
		final String callcenter = "callcenter";
		final List<PaymentType> paymentTypeList = new ArrayList<PaymentType>(getPaymentTypeData());

		if (cart instanceof Cart)
		{
			try
			{

				cartChannel = cart.getAttribute(CartModel.CHANNEL);

				LOG.debug("CartChannel for Cart" + cartChannel);

				if (StringUtils.containsIgnoreCase(cartChannel.toString(), callcenter))
				{
					LOG.debug("SUCCESS");

					for (final PaymentType paymentType : paymentTypeList)
					{
						LOG.debug("Inside isFulfilledInternal: coupon's payment mode: " + paymentType.getMode());
						if (!StringUtils.equalsIgnoreCase(paymentType.getMode(), codmode))
						{
							return false;
						}
					}
				}
			}

			catch (final JaloInvalidParameterException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (final JaloSecurityException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}


		}
		return true;//by default returns true irrespective of any particularly selected payment mode
		//TPR-4461 ends here
	}

	/**
	 * @param arg0
	 */
	@Override
	protected boolean isFulfilledInternal(final Product arg0)
	{
		// YTODO Auto-generated method stub
		return false;
	}



}