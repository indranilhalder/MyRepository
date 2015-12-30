package com.tisl.mpl.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.CartEntry;
import de.hybris.platform.jalo.type.ComposedType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


public class CustomShippingChargesPromotionAdjustAction extends GeneratedCustomShippingChargesPromotionAdjustAction
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CustomShippingChargesPromotionAdjustAction.class.getName());

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

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.promotions.jalo.AbstractPromotionAction#apply(de.hybris.platform.jalo.SessionContext)
	 */
	@Override
	public boolean apply(final SessionContext ctx)
	{
		boolean needsCalc = false;
		final AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
		final Integer orderEntryNumber = getOrderEntryNumber(ctx);
		final AbstractOrderEntry orderEntry = findOrderEntry(order, ctx, orderEntryNumber);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("(" + getPK() + ") apply: Applying OrderEntry adjustment action for order [" + order.getPK() + "]");
		}

		if (orderEntry != null)
		{
			Map<String, Integer> qualifyingCountMap = null;
			String productPromoCode = null;
			String cartPromoCode = null;
			Map<String, List<String>> productAssociatedItemsMap = null;
			Map<String, Map<String, Double>> prodPrevCurrDelChargeMap = null;
			Map<String, AbstractOrderEntry> validProductList = null;

			if (ctx.getAttributes() != null)
			{
				validProductList = ctx.getAttributes().get(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST) != null ? (Map<String, AbstractOrderEntry>) ctx
						.getAttributes().get(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST) : null;
				cartPromoCode = ctx.getAttributes().get(MarketplacecommerceservicesConstants.CARTPROMOCODE) != null ? (String) ctx
						.getAttributes().get(MarketplacecommerceservicesConstants.CARTPROMOCODE) : null;
				productPromoCode = ctx.getAttributes().get(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE) != null ? (String) ctx
						.getAttributes().get(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE) : null;
				qualifyingCountMap = ctx.getAttributes().get(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT) != null ? (Map<String, Integer>) ctx
						.getAttributes().get(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT) : null;
				productAssociatedItemsMap = ctx.getAttributes().get(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) != null ? (Map<String, List<String>>) ctx
						.getAttributes().get(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) : null;
				prodPrevCurrDelChargeMap = ctx.getAttributes().get(MarketplacecommerceservicesConstants.PRODPREVCURRDELCHARGEMAP) != null ? (Map<String, Map<String, Double>>) ctx
						.getAttributes().get(MarketplacecommerceservicesConstants.PRODPREVCURRDELCHARGEMAP) : null;
			}



			if (validProductList != null)
			{
				final Iterator iter = validProductList.entrySet().iterator();
				while (iter.hasNext())
				{
					final Map.Entry mapEntry = (Map.Entry) iter.next();
					final CartEntry cartEntry = (CartEntry) mapEntry.getValue();
					final String validProdUSSID = (String) mapEntry.getKey();

					List<String> associatedItemsList = new ArrayList<String>();
					if (null != productAssociatedItemsMap && !productAssociatedItemsMap.isEmpty())
					{
						associatedItemsList = productAssociatedItemsMap.get(validProdUSSID);
					}
					//					int qualifyingCount = 0;
					//					if (null != qualifyingCountMap && !qualifyingCountMap.isEmpty())
					//					{
					//						qualifyingCount = qualifyingCountMap.get(validProdUSSID).intValue();
					//					}

					final int qualifyingCount = (null != qualifyingCountMap && !qualifyingCountMap.isEmpty()) ? (qualifyingCountMap
							.get(validProdUSSID) != null ? qualifyingCountMap.get(validProdUSSID).intValue() : 0) : 0;

					double prevDelCharge = 0.00D;
					double currDelCharge = 0.00D;

					if (null != prodPrevCurrDelChargeMap && !prodPrevCurrDelChargeMap.isEmpty())
					{
						final Map<String, Double> prevCurrDeliveryChargeMap = prodPrevCurrDelChargeMap.get(validProdUSSID);
						prevDelCharge = prevCurrDeliveryChargeMap.get(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE)
								.doubleValue();
						currDelCharge = prevCurrDeliveryChargeMap.get(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE)
								.doubleValue();
					}

					final double lineItemLevelPrice = cartEntry.getTotalPriceAsPrimitive();//TODO
					final double totalProdLevelDisc = 0.00D;
					final double netSellingPrice = lineItemLevelPrice - totalProdLevelDisc;
					final double totalCartLevelDisc = 0.00D;
					final double netAmountAfterAllDisc = netSellingPrice - totalCartLevelDisc;

					final List<String> prevAssociatedItemList = cartEntry
							.getProperty(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) != null ? (List<String>) cartEntry
							.getProperty(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) : null;
					if (prevAssociatedItemList != null && !prevAssociatedItemList.isEmpty())
					{
						final Set associatedItemsSet = new HashSet(prevAssociatedItemList);
						associatedItemsSet.addAll(associatedItemsList);

						associatedItemsList.clear();
						associatedItemsList.addAll(associatedItemsSet);
					}

					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.DESCRIPTION, cartEntry.getProduct()
							.getDescription());
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, Integer.valueOf(qualifyingCount));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, associatedItemsList);
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, productPromoCode);
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTPROMOCODE, cartPromoCode);
					cartEntry
							.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALSALEPRICE, Double.valueOf(lineItemLevelPrice));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC,
							Double.valueOf(totalProdLevelDisc));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETSELLINGPRICE, Double.valueOf(netSellingPrice));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTLEVELDISC, Double.valueOf(totalCartLevelDisc));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETAMOUNTAFTERALLDISC,
							Double.valueOf(netAmountAfterAllDisc));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE, Double.valueOf(prevDelCharge));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE,
							Double.valueOf(currDelCharge));

					if (LOG.isDebugEnabled())
					{
						LOG.debug("(" + getPK() + ") apply: After apply Shipping charge promotion=[" + "Previous Delivery Charge: "
								+ prevDelCharge + ", Current Delivery Charge: " + currDelCharge + "]");
					}
				}
			}
			needsCalc = true;
			setMarkedApplied(ctx, true);
		}

		return needsCalc;
	}

	/**
	 * @Description : This method is called when promotional products are removed from cart and cart is recalculated.
	 * @param : ctx
	 * @return : true/false
	 */
	@Override
	public boolean undo(final SessionContext ctx)
	{
		boolean calculate = false;

		final AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
		if (order != null)
		{
			Map<String, Map<String, Double>> prodPrevCurrDelChargeMap = null;
			if (ctx.getAttributes() != null)
			{
				prodPrevCurrDelChargeMap = ctx.getAttributes().get(MarketplacecommerceservicesConstants.PRODPREVCURRDELCHARGEMAP) != null ? (Map<String, Map<String, Double>>) ctx
						.getAttributes().get(MarketplacecommerceservicesConstants.PRODPREVCURRDELCHARGEMAP) : null;
				if (null != prodPrevCurrDelChargeMap && !prodPrevCurrDelChargeMap.isEmpty())
				{
					getDefaultPromotionsManager().undoDeliveryCharges(order, prodPrevCurrDelChargeMap, ctx);
					calculate = true;
				}

			}

			//			final Map<String, AbstractOrderEntry> validProductList = ctx.getAttributes().get(
			//					MarketplacecommerceservicesConstants.VALIDPRODUCTLIST) != null ? (Map<String, AbstractOrderEntry>) ctx
			//					.getAttributes().get(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST) : null;
			//
			//			final List<AbstractOrderEntry> entryList = new ArrayList<AbstractOrderEntry>();
			//
			//			if (validProductList != null && !validProductList.isEmpty())
			//			{
			//				entryList.addAll(validProductList.values());
			//
			//			}
			//			else
			//			{
			//				entryList.addAll(order.getEntries());
			//			}
			//
			//			getDefaultPromotionsManager().undoPromotionalAttributes(ctx, entryList);
		}



		setMarkedApplied(ctx, false);

		return calculate;
	}

	/**
	 * @Description : OOB method
	 * @param : ctx
	 * @return : double
	 */
	@Override
	public double getValue(final SessionContext ctx)
	{
		return 0.0D;
	}

	/**
	 * @Description : OOB method
	 * @param : ctx
	 * @param : values
	 */
	@Override
	protected void deepCloneAttributes(final SessionContext ctx, final Map values)
	{
		super.deepCloneAttributes(ctx, values);
	}

	/**
	 * @Description : Find Order Entry
	 * @param : ctx
	 * @param : order
	 * @param : orderEntryNumber
	 * @return : AbstractOrderEntry
	 */
	private AbstractOrderEntry findOrderEntry(final AbstractOrder order, final SessionContext ctx, final Integer orderEntryNumber)
	{
		AbstractOrderEntry result = null;

		if (orderEntryNumber == null)
		{
			final Collection<AbstractOrderEntry> abstractOrderEntries = order.getAllEntries();
			for (final AbstractOrderEntry oe : abstractOrderEntries)
			{
				if ((!(oe.getProduct(ctx).equals(getOrderEntryProduct(ctx))))
						|| (oe.getQuantity(ctx).longValue() < getOrderEntryQuantityAsPrimitive(ctx)))
				{
					continue;
				}
				result = oe;
			}

		}
		else
		{
			result = order.getEntry(orderEntryNumber.intValue());
		}

		return result;
	}

	/**
	 * @Description : OOB method
	 * @param : ctx
	 * @return : true/false
	 */
	@Override
	public boolean isAppliedToOrder(final SessionContext paramSessionContext)
	{
		// YTODO Auto-generated method stub
		return true;
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

}