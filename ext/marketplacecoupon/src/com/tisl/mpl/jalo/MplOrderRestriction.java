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
import de.hybris.platform.voucher.jalo.ProductRestriction;
import de.hybris.platform.voucher.jalo.Restriction;
import de.hybris.platform.voucher.jalo.util.VoucherEntry;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


public class MplOrderRestriction extends GeneratedMplOrderRestriction
{
	private static final String SELECTED_USSID = "selectedUSSID";
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

		final Set<Restriction> restrictions = getVoucher().getRestrictions();
		SellerRestriction sellerRestriction = null;
		ProductRestriction productRestriction = null;
		ProductCategoryRestriction productCategoryRestriction = null;

		for (final Restriction restriction : restrictions)
		{
			if (restriction instanceof SellerRestriction)
			{
				sellerRestriction = (SellerRestriction) restriction;
			}
			if (restriction instanceof ProductRestriction)
			{
				productRestriction = (ProductRestriction) restriction;
			}
			if (restriction instanceof ProductCategoryRestriction)
			{
				productCategoryRestriction = (ProductCategoryRestriction) restriction;
			}
		}

		if (null == sellerRestriction && null == productRestriction && null == productCategoryRestriction) // OTB scenario
		{
			try
			{
				double currentTotal = 0;
				final List<AbstractOrderEntry> entryList = anOrder.getAllEntries();

				if (CollectionUtils.isNotEmpty(entryList))
				{
					for (final AbstractOrderEntry entry : entryList)
					{
						final Double netAmountAfterAllDiscount = (Double) entry.getAttribute(NETAMOUNTAFTERALLDISC);
						final Double productVal = (Double) entry.getAttribute(TOTALPRICE);

						currentTotal += (netAmountAfterAllDiscount.doubleValue() > 0) ? (netAmountAfterAllDiscount.doubleValue())
								: (productVal.doubleValue());
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
				LOG.error("Exception in Coupon evaluation for Cart Thresh OTB scenario" + exception.getMessage());
			}
		}
		else if (null != sellerRestriction && null == productRestriction && null == productCategoryRestriction) // seller
		{
			try
			{
				/*
				 * final Boolean positive = sellerRestriction.isPositive(); boolean isValid = false; final
				 * Collection<SellerMaster> sellerList = sellerRestriction.getSeller(); final List<AbstractOrderEntry>
				 * entryList = anOrder.getAllEntries(); if (positive.booleanValue()) { isValid = checkSellerIncl(sellerList,
				 * entryList, minimumTotal); } else { isValid = checkSellerExcl(sellerList, entryList, minimumTotal); }
				 * return isValid;
				 */
				boolean checkFlag = false;
				double currentTotal = 0;
				final VoucherEntrySet voucherEntries = sellerRestriction.getApplicableEntries(anOrder);
				if (CollectionUtils.isNotEmpty(voucherEntries))
				{
					for (final Object entry : voucherEntries)
					{
						final AbstractOrderEntry abEntry = ((VoucherEntry) entry).getOrderEntry();
						final Double netAmountAfterAllDiscount = (Double) abEntry.getAttribute(NETAMOUNTAFTERALLDISC);
						final Double productVal = (Double) abEntry.getAttribute(TOTALPRICE);
						currentTotal += (netAmountAfterAllDiscount.doubleValue() > 0) ? (netAmountAfterAllDiscount.doubleValue())
								: (productVal.doubleValue());
					}
				}

				if (currentTotal >= minimumTotal)
				{
					checkFlag = true;
				}
				return checkFlag;
			}
			catch (final Exception exception)
			{
				LOG.error("Exception in Coupon evaluation for Cart Thresh seller" + exception.getMessage());
			}
		}
		else if (null == sellerRestriction && null != productRestriction && null == productCategoryRestriction) // product
		{
			try
			{
				boolean checkFlag = false;
				double currentTotal = 0;
				final VoucherEntrySet voucherEntries = productRestriction.getApplicableEntries(anOrder);
				if (CollectionUtils.isNotEmpty(voucherEntries))
				{
					for (final Object entry : voucherEntries)
					{
						final AbstractOrderEntry abEntry = ((VoucherEntry) entry).getOrderEntry();
						final Double netAmountAfterAllDiscount = (Double) abEntry.getAttribute(NETAMOUNTAFTERALLDISC);
						final Double productVal = (Double) abEntry.getAttribute(TOTALPRICE);
						currentTotal += (netAmountAfterAllDiscount.doubleValue() > 0) ? (netAmountAfterAllDiscount.doubleValue())
								: (productVal.doubleValue());
					}
				}

				if (currentTotal >= minimumTotal)
				{
					checkFlag = true;
				}
				return checkFlag;
			}
			catch (final Exception exception)
			{
				LOG.error("Exception in Coupon evaluation for Cart Thresh product" + exception.getMessage());
			}
		}
		else if (null == sellerRestriction && null == productRestriction && null != productCategoryRestriction) // productCategory
		{
			try
			{
				boolean checkFlag = false;
				double currentTotal = 0;
				final VoucherEntrySet voucherEntries = productCategoryRestriction.getApplicableEntries(anOrder);
				if (CollectionUtils.isNotEmpty(voucherEntries))
				{
					for (final Object entry : voucherEntries)
					{
						final AbstractOrderEntry abEntry = ((VoucherEntry) entry).getOrderEntry();
						final Double netAmountAfterAllDiscount = (Double) abEntry.getAttribute(NETAMOUNTAFTERALLDISC);
						final Double productVal = (Double) abEntry.getAttribute(TOTALPRICE);
						currentTotal += (netAmountAfterAllDiscount.doubleValue() > 0) ? (netAmountAfterAllDiscount.doubleValue())
								: (productVal.doubleValue());
					}
				}

				if (currentTotal >= minimumTotal)
				{
					checkFlag = true;
				}
				return checkFlag;
			}
			catch (final Exception exception)
			{
				LOG.error("Exception in Coupon evaluation for Cart Thresh productCategory" + exception.getMessage());
			}
		}
		else if (null != sellerRestriction && null != productRestriction && null == productCategoryRestriction) // seller + product
		{
			try
			{
				boolean isValid = false;
				final Boolean positive = sellerRestriction.isPositive();
				final List<AbstractOrderEntry> entryList = new ArrayList<AbstractOrderEntry>();
				final VoucherEntrySet voucherEntries = productRestriction.getApplicableEntries(anOrder);
				if (CollectionUtils.isNotEmpty(voucherEntries))
				{
					for (final Object entry : voucherEntries)
					{
						final AbstractOrderEntry abEntry = ((VoucherEntry) entry).getOrderEntry();
						entryList.add(abEntry);
					}
				}

				if (CollectionUtils.isNotEmpty(entryList))
				{
					final Collection<SellerMaster> sellerList = sellerRestriction.getSeller();
					if (positive.booleanValue())
					{
						isValid = checkSellerIncl(sellerList, entryList, minimumTotal);
					}
					else
					{
						isValid = checkSellerExcl(sellerList, entryList, minimumTotal);
					}
				}
				return isValid;
			}
			catch (final Exception exception)
			{
				LOG.error("Exception in Coupon evaluation for Cart Thresh seller + product" + exception.getMessage());
			}
		}
		else if (null != sellerRestriction && null == productRestriction && null != productCategoryRestriction) // seller + productCategory
		{
			try
			{
				boolean isValid = false;
				final Boolean positive = sellerRestriction.isPositive();
				final List<AbstractOrderEntry> entryList = new ArrayList<AbstractOrderEntry>();
				final VoucherEntrySet voucherEntries = productCategoryRestriction.getApplicableEntries(anOrder);
				if (CollectionUtils.isNotEmpty(voucherEntries))
				{
					for (final Object entry : voucherEntries)
					{
						final AbstractOrderEntry abEntry = ((VoucherEntry) entry).getOrderEntry();
						entryList.add(abEntry);
					}
				}

				if (CollectionUtils.isNotEmpty(entryList))
				{
					final Collection<SellerMaster> sellerList = sellerRestriction.getSeller();
					if (positive.booleanValue())
					{
						isValid = checkSellerIncl(sellerList, entryList, minimumTotal);
					}
					else
					{
						isValid = checkSellerExcl(sellerList, entryList, minimumTotal);
					}
				}
				return isValid;
			}
			catch (final Exception exception)
			{
				LOG.error("Exception in Coupon evaluation for Cart Thresh seller + productCategory" + exception.getMessage());
			}
		}
		else if (null == sellerRestriction && null != productRestriction && null != productCategoryRestriction) // product + productCategory
		{
			try
			{
				boolean checkFlag = false;
				double currentTotal = 0;
				final VoucherEntrySet voucherProductCategoryEntries = productCategoryRestriction.getApplicableEntries(anOrder);
				if (CollectionUtils.isNotEmpty(voucherProductCategoryEntries))
				{
					for (final Object entry : voucherProductCategoryEntries)
					{
						final AbstractOrderEntry abEntry = ((VoucherEntry) entry).getOrderEntry();
						final Double netAmountAfterAllDiscount = (Double) abEntry.getAttribute(NETAMOUNTAFTERALLDISC);
						final Double productVal = (Double) abEntry.getAttribute(TOTALPRICE);
						currentTotal += (netAmountAfterAllDiscount.doubleValue() > 0) ? (netAmountAfterAllDiscount.doubleValue())
								: (productVal.doubleValue());
					}
				}

				final VoucherEntrySet voucherProductEntries = productRestriction.getApplicableEntries(anOrder);
				if (CollectionUtils.isNotEmpty(voucherProductEntries))
				{
					for (final Object entry : voucherProductEntries)
					{
						final AbstractOrderEntry abEntry = ((VoucherEntry) entry).getOrderEntry();
						final Double netAmountAfterAllDiscount = (Double) abEntry.getAttribute(NETAMOUNTAFTERALLDISC);
						final Double productVal = (Double) abEntry.getAttribute(TOTALPRICE);
						currentTotal += (netAmountAfterAllDiscount.doubleValue() > 0) ? (netAmountAfterAllDiscount.doubleValue())
								: (productVal.doubleValue());
					}
				}

				if (currentTotal >= minimumTotal)
				{
					checkFlag = true;
				}
				return checkFlag;
			}
			catch (final Exception exception)
			{
				LOG.error("Exception in Coupon evaluation for Cart Thresh product + productCategory" + exception.getMessage());
			}
		}
		else if (null != sellerRestriction && null != productRestriction && null != productCategoryRestriction) // seller + product + productCategory
		{
			try
			{
				boolean isValid = false;
				final Boolean positive = sellerRestriction.isPositive();
				final List<AbstractOrderEntry> finalEntryList = new ArrayList<AbstractOrderEntry>();
				final VoucherEntrySet voucherProductCategoryEntries = productCategoryRestriction.getApplicableEntries(anOrder);
				if (CollectionUtils.isNotEmpty(voucherProductCategoryEntries))
				{
					for (final Object entry : voucherProductCategoryEntries)
					{
						final AbstractOrderEntry abProductCategoryEntry = ((VoucherEntry) entry).getOrderEntry();
						finalEntryList.add(abProductCategoryEntry);
					}
				}

				final VoucherEntrySet voucherProductEntries = productRestriction.getApplicableEntries(anOrder);
				if (CollectionUtils.isNotEmpty(voucherProductEntries))
				{
					for (final Object entry : voucherProductEntries)
					{
						final AbstractOrderEntry abProductEntry = ((VoucherEntry) entry).getOrderEntry();
						finalEntryList.add(abProductEntry);
					}
				}

				if (CollectionUtils.isNotEmpty(finalEntryList))
				{
					final Collection<SellerMaster> sellerList = sellerRestriction.getSeller();
					if (positive.booleanValue())
					{
						isValid = checkSellerIncl(sellerList, finalEntryList, minimumTotal);
					}
					else
					{
						isValid = checkSellerExcl(sellerList, finalEntryList, minimumTotal);
					}
				}
				return isValid;

			}
			catch (final Exception exception)
			{
				LOG.error("Exception in Coupon evaluation for Cart Thresh seller + product + productCategory"
						+ exception.getMessage());
			}
		}

		return false;
	}



	protected boolean checkSellerIncl(final Collection<SellerMaster> sellerList, final List<AbstractOrderEntry> entryList,
			final double minimumTotal) throws JaloInvalidParameterException, JaloSecurityException
	{
		boolean checkFlag = false;
		double currentTotal = 0;
		final List<String> sellerIdList = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(sellerList))
		{
			for (final SellerMaster seller : sellerList)
			{
				sellerIdList.add(seller.getId());
			}
		}

		if (CollectionUtils.isNotEmpty(entryList) && CollectionUtils.isNotEmpty(sellerIdList))
		{
			for (final AbstractOrderEntry entry : entryList)
			{
				final Object ussid = entry.getProperty(SELECTED_USSID);
				final String sellerId = ussid.toString().substring(0, 6);
				if (sellerIdList.contains(sellerId))
				{
					final Double netAmountAfterAllDiscount = (Double) entry.getAttribute(NETAMOUNTAFTERALLDISC);
					final Double productVal = (Double) entry.getAttribute(TOTALPRICE);

					currentTotal += (netAmountAfterAllDiscount.doubleValue() > 0) ? (netAmountAfterAllDiscount.doubleValue())
							: (productVal.doubleValue());
				}
			}
		}

		if (currentTotal >= minimumTotal)
		{
			checkFlag = true;
		}

		return checkFlag;
	}

	protected boolean checkSellerExcl(final Collection<SellerMaster> sellerList, final List<AbstractOrderEntry> entryList,
			final double minimumTotal) throws JaloInvalidParameterException, JaloSecurityException
	{
		boolean checkFlag = false;
		double currentTotal = 0;
		final List<String> sellerIdList = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(sellerList))
		{
			for (final SellerMaster seller : sellerList)
			{
				sellerIdList.add(seller.getId());
			}
		}

		if (CollectionUtils.isNotEmpty(entryList) && CollectionUtils.isNotEmpty(sellerIdList))
		{
			for (final AbstractOrderEntry entry : entryList)
			{
				final Object ussid = entry.getProperty(SELECTED_USSID);
				final String sellerId = ussid.toString().substring(0, 6);
				if (!(sellerIdList.contains(sellerId)))
				{
					final Double netAmountAfterAllDiscount = (Double) entry.getAttribute(NETAMOUNTAFTERALLDISC);
					final Double productVal = (Double) entry.getAttribute(TOTALPRICE);

					currentTotal += (netAmountAfterAllDiscount.doubleValue() > 0) ? (netAmountAfterAllDiscount.doubleValue())
							: (productVal.doubleValue());
				}

			}
		}

		if (currentTotal >= minimumTotal)
		{
			checkFlag = true;
		}

		return checkFlag;
	}

}