//package com.tisl.mpl.jalo;
//
//import de.hybris.platform.jalo.Item;
//import de.hybris.platform.jalo.JaloBusinessException;
//import de.hybris.platform.jalo.SessionContext;
//import de.hybris.platform.jalo.order.AbstractOrder;
//import de.hybris.platform.jalo.order.AbstractOrderEntry;
//import de.hybris.platform.jalo.product.Product;
//import de.hybris.platform.jalo.type.ComposedType;
//import de.hybris.platform.voucher.jalo.util.VoucherEntry;
//import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
//
//import java.util.Collection;
//import java.util.List;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.log4j.Logger;
//
//
//public class SellerRestriction extends GeneratedSellerRestriction
//{
//	@SuppressWarnings("unused")
//	private final static Logger LOG = Logger.getLogger(SellerRestriction.class.getName());
//
//	@Override
//	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
//			throws JaloBusinessException
//	{
//		// business code placed here will be executed before the item is created
//		// then create the item
//		final Item item = super.createItem(ctx, type, allAttributes);
//		// business code placed here will be executed after the item was created
//		// and return the item
//		return item;
//	}
//
//	@Override
//	protected boolean isFulfilledInternal(final AbstractOrder cart)
//	{
//		final Collection<SellerMaster> sellerList = super.getSeller();
//		boolean isValid = false;
//		final Boolean positive = super.isPositive();
//		final List<AbstractOrderEntry> entryList = cart.getAllEntries();
//		try
//		{
//			if (positive.booleanValue())
//			{
//				isValid = checkSellerIncl(sellerList, entryList);
//			}
//			else
//			{
//				isValid = checkSellerExcl(sellerList, entryList);
//			}
//		}
//		catch (final Exception ex)
//		{
//			LOG.error(ex.getMessage());
//		}
//		return isValid;
//	}
//
//	@Override
//	protected boolean isFulfilledInternal(final Product arg0)
//	{
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//
//
//	protected boolean checkSellerIncl(final Collection<SellerMaster> sellerList, final List<AbstractOrderEntry> entryList)
//	{
//		boolean checkFlag = false;
//		if (CollectionUtils.isNotEmpty(entryList) && CollectionUtils.isNotEmpty(sellerList))
//		{
//			for (final AbstractOrderEntry entry : entryList)
//			{
//				final Object ussid = entry.getProperty("selectedUSSID");
//				final String sellerId = ussid.toString().substring(0, 6);
//
//				for (final SellerMaster seller : sellerList)
//				{
//					if (seller.getId().equalsIgnoreCase(sellerId))
//					{
//						checkFlag = true;
//						break;
//					}
//				}
//			}
//		}
//		return checkFlag;
//	}
//
//
//
//	protected boolean checkSellerExcl(final Collection<SellerMaster> sellerList, final List<AbstractOrderEntry> entryList)
//	{
//		boolean checkFlag = false;
//		int count = 0;
//		int entryCount = 0;
//		if (CollectionUtils.isNotEmpty(entryList) && CollectionUtils.isNotEmpty(sellerList))
//		{
//			for (final AbstractOrderEntry entry : entryList)
//			{
//				final Object ussid = entry.getProperty("selectedUSSID");
//				final String sellerId = ussid.toString().substring(0, 6);
//
//				for (final SellerMaster seller : sellerList)
//				{
//					if (!seller.getId().equalsIgnoreCase(sellerId))
//					{
//						count++;
//					}
//				}
//
//				if (count == sellerList.size())
//				{
//					entryCount++;
//				}
//			}
//
//			if (entryCount > 0)
//			{
//				checkFlag = true;
//			}
//		}
//		return checkFlag;
//	}
//
//
//
//	@Override
//	public VoucherEntrySet getApplicableEntries(final AbstractOrder anOrder)
//	{
//		final Collection<SellerMaster> sellerList = super.getSeller();
//		final Boolean positive = super.isPositive();
//		int count = 0;
//		final VoucherEntrySet entries = new VoucherEntrySet();
//		for (final AbstractOrderEntry entry : anOrder.getEntries())
//		{
//			final Object ussid = entry.getProperty("selectedUSSID");
//			final String sellerId = ussid.toString().substring(0, 6);
//
//			for (final SellerMaster seller : sellerList)
//			{
//				if (positive.booleanValue() && seller.getId().equalsIgnoreCase(sellerId))
//				{
//					entries.add(new VoucherEntry(entry, entry.getQuantity().longValue(), entry.getUnit()));
//					break;
//				}
//				else if (!positive.booleanValue() && !seller.getId().equalsIgnoreCase(sellerId))
//				{
//					count++;
//				}
//			}
//
//			if (count == sellerList.size())
//			{
//				entries.add(new VoucherEntry(entry, entry.getQuantity().longValue(), entry.getUnit()));
//			}
//
//		}
//		return entries;
//	}
//
//}