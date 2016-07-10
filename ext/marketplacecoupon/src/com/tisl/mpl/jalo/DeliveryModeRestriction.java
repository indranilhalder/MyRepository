//package com.tisl.mpl.jalo;
//
//import de.hybris.platform.core.Registry;
//import de.hybris.platform.jalo.Item;
//import de.hybris.platform.jalo.JaloBusinessException;
//import de.hybris.platform.jalo.SessionContext;
//import de.hybris.platform.jalo.order.AbstractOrder;
//import de.hybris.platform.jalo.order.AbstractOrderEntry;
//import de.hybris.platform.jalo.order.delivery.DeliveryMode;
//import de.hybris.platform.jalo.product.Product;
//import de.hybris.platform.jalo.type.ComposedType;
//import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
//
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//import com.tisl.mpl.helper.MplCouponHelper;
//
//
//public class DeliveryModeRestriction extends GeneratedDeliveryModeRestriction
//{
//	@SuppressWarnings("unused")
//	private final static Logger LOG = Logger.getLogger(DeliveryModeRestriction.class.getName());
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
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see de.hybris.platform.voucher.jalo.Restriction#isFulfilledInternal(de.hybris.platform.jalo.order.AbstractOrder)
//	 */
//	@Override
//	protected boolean isFulfilledInternal(final AbstractOrder order)
//	{
//		return (!(getApplicableEntries(order).isEmpty()));
//	}
//
//	@Override
//	public VoucherEntrySet getApplicableEntries(final AbstractOrder order)
//	{
//		final VoucherEntrySet entries = new VoucherEntrySet();
//
//		final List<DeliveryMode> restrDeliveryModeList = getDeliveryModeDetailsList();
//
//		final List<AbstractOrderEntry> entryList = getMplCouponHelper().validateDelliveryMode(restrDeliveryModeList, order,
//				isPositiveAsPrimitive());
//
//		if (!entryList.isEmpty())
//		{
//			entries.addAll(entryList);
//		}
//
//		return entries;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see de.hybris.platform.voucher.jalo.Restriction#isFulfilledInternal(de.hybris.platform.jalo.product.Product)
//	 */
//	@Override
//	protected boolean isFulfilledInternal(final Product arg0)
//	{
//		// YTODO Auto-generated method stub
//		return false;
//	}
//
//	//For Referring to coupon Helper Class
//	protected MplCouponHelper getMplCouponHelper()
//	{
//		return Registry.getApplicationContext().getBean("mplCouponHelper", MplCouponHelper.class);
//	}
//
//}