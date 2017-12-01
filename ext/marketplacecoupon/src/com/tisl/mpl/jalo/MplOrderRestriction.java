package com.tisl.mpl.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.type.ComposedType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


public class MplOrderRestriction extends GeneratedMplOrderRestriction
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MplOrderRestriction.class.getName());

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

	@Override
	protected boolean isFulfilledInternal(final AbstractOrder anOrder)
	{
		final Currency minimumOrderValueCurrency = getCurrency();
		final Currency currentOrderCurrency = anOrder.getCurrency();
		String conVal = MarketplacecommerceservicesConstants.EMPTY;

		double currentTotal = anOrder.getSubtotal().doubleValue();
		final double minimumTotal = minimumOrderValueCurrency.convert(currentOrderCurrency, getTotalAsPrimitive());

		try
		{

			if (!(isValueofgoodsonlyAsPrimitive()))
			{
				currentTotal += anOrder.getDeliveryCosts();
			}


			if (null != anOrder.getAttribute("convenienceCharges"))
			{
				conVal = (String) anOrder.getAttribute("convenienceCharges");
			}


			if (StringUtils.isNotEmpty(conVal))
			{
				currentTotal += Double.parseDouble(conVal);
			}


			// Coupon Evaluation

			if (isPositiveAsPrimitive())
			{
				return (currentTotal >= minimumTotal);
			}


		}
		catch (final Exception exception)
		{
			LOG.error("Exception in Coupon evaluation for Cart Thresh" + exception.getMessage());
		}

		return (currentTotal <= minimumTotal);

	}
}
