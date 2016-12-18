package com.tisl.mpl.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.voucher.jalo.util.VoucherEntry;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


public class SellerRestriction extends GeneratedSellerRestriction
{
	/**
	 *
	 */
	private static final String SELECTED_USSID = "selectedUSSID";
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SellerRestriction.class.getName());

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

	/**
	 * CHANGES FOR TPR-715 changes for showing violation msg in hmc
	 */
	@Override
	protected String[] getMessageAttributeValues()
	{

		final StringBuilder sellerIds = new StringBuilder();
		for (final Iterator iterator = getSeller().iterator(); iterator.hasNext();)
		{
			final SellerMaster seller = (SellerMaster) iterator.next();
			sellerIds.append(seller.getId());
			if (!(iterator.hasNext()))
			{
				continue;
			}
			sellerIds.append(", ");

		}
		return new String[]
		{ Localization.getLocalizedString("type.restriction.positive." + isPositiveAsPrimitive()), sellerIds.toString() };
	}

	/**
	 * CHANGES FOR TPR-715 checking whether an individual seller restriction is fulfilled or not
	 *
	 * @param cart
	 * @return isValid
	 */
	@Override
	protected boolean isFulfilledInternal(final AbstractOrder cart)
	{
		final Collection<SellerMaster> sellerList = super.getSeller();
		boolean isValid = false;
		final Boolean positive = super.isPositive();
		final List<AbstractOrderEntry> entryList = cart.getAllEntries();
		try
		{
			if (positive.booleanValue())
			{
				isValid = checkSellerIncl(sellerList, entryList);
			}
			else
			{
				isValid = checkSellerExcl(sellerList, entryList);
			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
		}
		return isValid;
	}

	@Override
	protected boolean isFulfilledInternal(final Product arg0)
	{
		return false;
	}


	/**
	 * CHANGES FOR TPR-715 checking included seller for seller restriction
	 *
	 * @param sellerList
	 * @param entryList
	 * @return checkFlag
	 */
	protected boolean checkSellerIncl(final Collection<SellerMaster> sellerList, final List<AbstractOrderEntry> entryList)
	{
		boolean checkFlag = false;
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
					checkFlag = true;
					break;
				}
			}
		}
		return checkFlag;
	}




	/**
	 * CHANGES FOR TPR-715 checking excluded seller for seller restriction
	 *
	 * @param sellerList
	 * @param entryList
	 * @return checkFlag
	 */
	protected boolean checkSellerExcl(final Collection<SellerMaster> sellerList, final List<AbstractOrderEntry> entryList)
	{
		boolean checkFlag = false;
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
					checkFlag = true;
					break;
				}

			}
		}
		return checkFlag;
	}

	/**
	 * CHANGES FOR TPR-715 getting a list of seller entries which falls under the seller restriction
	 *
	 * @param anOrder
	 * @return entries
	 */
	@Override
	public VoucherEntrySet getApplicableEntries(final AbstractOrder anOrder)
	{
		final Collection<SellerMaster> sellerList = super.getSeller();
		final Boolean positive = super.isPositive();
		final VoucherEntrySet entries = new VoucherEntrySet();
		final List<String> sellerIdList = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(sellerList))
		{
			for (final SellerMaster seller : sellerList)
			{
				sellerIdList.add(seller.getId());
			}
		}

		for (final AbstractOrderEntry entry : anOrder.getEntries())
		{
			final Object ussid = entry.getProperty(SELECTED_USSID);
			final String sellerId = ussid.toString().substring(0, 6);
			if (positive.booleanValue() && sellerIdList.contains(sellerId))
			{
				entries.add(new VoucherEntry(entry, entry.getQuantity().longValue(), entry.getUnit()));
			}
			if (!positive.booleanValue() && !(sellerIdList.contains(sellerId)))
			{
				entries.add(new VoucherEntry(entry, entry.getQuantity().longValue(), entry.getUnit()));
			}
		}
		return entries;
	}
}
