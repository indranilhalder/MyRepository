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
import de.hybris.platform.voucher.jalo.ProductCategoryRestriction;
import de.hybris.platform.voucher.jalo.Restriction;
import de.hybris.platform.voucher.jalo.util.VoucherEntry;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;

import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


public class MplOrderRestriction extends GeneratedMplOrderRestriction
{
	private static final String NETAMOUNTAFTERALLDISC = "netAmountAfterAllDisc";
	private static final String TOTALPRICE = "totalPrice";
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

	@SuppressWarnings("deprecation")
	@Override
	protected boolean isFulfilledInternal(final AbstractOrder anOrder)
	{
		//SDI-5493 Customization
		final Currency minimumOrderValueCurrency = getCurrency();
		final Currency currentOrderCurrency = anOrder.getCurrency();

		final double minimumTotal = minimumOrderValueCurrency.convert(currentOrderCurrency, getTotalAsPrimitive());
		boolean checkFlag = false;
		final VoucherEntrySet entries = new VoucherEntrySet(anOrder.getAllEntries());
		final Iterator iterator = getVoucher().getRestrictions().iterator();
		while (!entries.isEmpty() && iterator.hasNext())
		{
			final Restriction nextRestriction = (Restriction) iterator.next();
			if (nextRestriction instanceof ProductCategoryRestriction)
			{
				entries.retainAll(((ProductCategoryRestriction) nextRestriction).getApplicableEntries(anOrder));
				LOG.debug("Product Category Restriction present" + nextRestriction);
			}
			else if (nextRestriction instanceof MplOrderRestriction)
			{
				LOG.debug("MplOrderRestriction skipped" + nextRestriction);
				continue;
			}
			else
			{
				LOG.debug("Restriction present" + nextRestriction);
				entries.retainAll(nextRestriction.getApplicableEntries(anOrder));
			}
		}
		LOG.debug("Entries" + entries);
		if (CollectionUtils.isNotEmpty(entries))
		{
			try
			{
				checkFlag = checkEligibility(entries, minimumTotal, anOrder);
			}
			catch (final Exception exception)
			{
				LOG.error("Exception in Coupon evaluation for Cart Thresh" + exception.getMessage());
			}
		}
		return checkFlag;
	}

	protected boolean checkEligibility(final VoucherEntrySet voucherEntries, final double minimumTotal, final AbstractOrder anOrder)
			throws JaloInvalidParameterException, JaloSecurityException
	{
		boolean checkFlag = false;
		double currentTotal = 0;
		for (final Object entry : voucherEntries)
		{
			final AbstractOrderEntry abEntry = ((VoucherEntry) entry).getOrderEntry();
			final Double netAmountAfterAllDiscount = (Double) abEntry.getAttribute(NETAMOUNTAFTERALLDISC);
			final Double productVal = (Double) abEntry.getAttribute(TOTALPRICE);
			currentTotal += (netAmountAfterAllDiscount.doubleValue() > 0) ? (netAmountAfterAllDiscount.doubleValue()) : (productVal
					.doubleValue());
		}
		boolean cliqCashValidation = true;

		//		String splitModeInfo = null;
		//		if (null != anOrder.getAttribute("splitModeInfo"))
		//		{
		//			splitModeInfo = (String) anOrder.getAttribute("splitModeInfo");
		//		}

		final String splitModeInfo = anOrder.getAttribute("splitModeInfo") != null ? (String) anOrder.getAttribute("splitModeInfo")
				: null;

		if (null != splitModeInfo && splitModeInfo.trim().equalsIgnoreCase("Split") /* && checkForBankVoucher */)
		{
			cliqCashValidation = checkCliqCashValue(minimumTotal, anOrder, currentTotal);
			if (isPositiveAsPrimitive() && cliqCashValidation)
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		if (currentTotal >= minimumTotal)
		{
			checkFlag = true;
		}
		return checkFlag;
	}

	private boolean checkCliqCashValue(final double minimumTotal, final AbstractOrder anOrder, final double currentTotal)
			throws JaloInvalidParameterException, JaloSecurityException
	{
		LOG.debug("Inside Order Retriction checkCliqCashValue");
		double totalPayableAmount = currentTotal;
		final Double walletAmount = (Double) anOrder.getAttribute("totalWalletAmount");
		totalPayableAmount -= null != walletAmount ? walletAmount.doubleValue() : 0.0d;

		if (totalPayableAmount >= minimumTotal)
		{
			return true;
		}
		return false;
	}
}