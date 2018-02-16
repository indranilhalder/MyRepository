package com.tisl.mpl.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


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

		double currentTotal = 0;
		final double minimumTotal = minimumOrderValueCurrency.convert(currentOrderCurrency, getTotalAsPrimitive());

		try
		{

			final List<AbstractOrderEntry> entryList = anOrder.getAllEntries();

			if (CollectionUtils.isNotEmpty(entryList))
			{
				for (final AbstractOrderEntry entry : entryList)
				{
					final Double netAmountAfterAllDiscount = (Double) entry.getAttribute("netAmountAfterAllDisc");
					final Double productVal = (Double) entry.getAttribute("totalPrice");

					currentTotal += (netAmountAfterAllDiscount.doubleValue() > 0) ? (netAmountAfterAllDiscount.doubleValue())
							: (productVal.doubleValue());
				}
			}
			boolean cliqCashValidation = true;
			String splitModeInfo = null;
			if (null != anOrder.getAttribute("splitModeInfo"))
			{
				splitModeInfo = (String) anOrder.getAttribute("splitModeInfo");
			}

			if (null != splitModeInfo && splitModeInfo.trim().equalsIgnoreCase("Split") /* && checkForBankVoucher*/)
			{
				cliqCashValidation = checkCliqCashValue(minimumTotal, anOrder , currentTotal);
				if (isPositiveAsPrimitive() && cliqCashValidation)
				{
					return true;
				}
				else
				{
					return false;
				}
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

	private boolean checkCliqCashValue(final double minimumTotal, final AbstractOrder anOrder, double currentTotal)
			throws JaloInvalidParameterException, JaloSecurityException
	{
		LOG.debug("Inside Order Retriction checkCliqCashValue");
		double totalPayableAmount = currentTotal;
		final Double walletAmount = (Double) anOrder.getAttribute("totalWalletAmount");
		totalPayableAmount -= null !=walletAmount? walletAmount.doubleValue() :0.0d;

		if (totalPayableAmount >= minimumTotal)
		{
			return true;
		}
		return false;
	}
}
