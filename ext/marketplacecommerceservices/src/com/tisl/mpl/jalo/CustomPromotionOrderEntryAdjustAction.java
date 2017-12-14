/*
 *
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package com.tisl.mpl.jalo;

import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.promotions.jalo.AbstractPromotion;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.util.Pair;
import de.hybris.platform.util.DiscountValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * Customized action class for product level promotions
 *
 */
public class CustomPromotionOrderEntryAdjustAction extends GeneratedCustomPromotionOrderEntryAdjustAction
{
	private final static Logger log = Logger.getLogger(CustomPromotionOrderEntryAdjustAction.class.getName());

	/**
	 * @Description : This method is called when promotion is applied
	 * @param : ctx
	 * @return : true/false
	 */
	@Override
	public boolean apply(final SessionContext ctx)
	{
		final PromotionResult result = getPromotionResult(ctx);
		final AbstractOrder order = result.getOrder(ctx);
		boolean isFreebiePromo = false;

		//****New Code Added for TISPRO-670*******

		final AbstractPromotion promotion = result.getPromotion();
		if (null != promotion && promotion instanceof BuyABFreePrecentageDiscount)
		{
			isFreebiePromo = true;
			if (log.isDebugEnabled())
			{
				log.debug("Freebie Plus Discount Promotion : Adding Associated Item Data");
			}
		}
		//****New Code Added for TISPRO-670 ends *******

		if (log.isDebugEnabled())
		{
			log.debug("(" + getPK() + ") apply: Applying OrderEntry adjustment action for order [" + order.getPK() + "]");
		}
		boolean needsCalc = false;
		final Integer orderEntryNumber = getOrderEntryNumber(ctx);
		final AbstractOrderEntry orderEntry = findOrderEntry(order, ctx, orderEntryNumber);

		if (orderEntry != null)
		{
			final double orderEntryAdjustment = getAmount(ctx).doubleValue();

			final double unitAdjustment = orderEntryAdjustment / orderEntry.getQuantity(ctx).longValue();
			//final double unitAdjustment = orderEntryAdjustment / getOrderEntryQuantity(ctx).longValue();

			final String code = getGuid(ctx);
			final DiscountValue dv = new DiscountValue(code, -1.0D * unitAdjustment, true, order.getCurrency(ctx).getIsoCode(ctx));

			insertFirstOrderEntryDiscountValue(ctx, orderEntry, dv);
			if (log.isDebugEnabled())
			{
				log.debug("(" + getPK() + ") apply: Creating an adjustment of " + getAmount(ctx) + " to order entry '"
						+ orderEntry.getPK() + "'.  Order entry now has " + orderEntry.getDiscountValues(ctx).size() + " adjustments");
			}
			//needsCalc = true;

			//Apportioning calculation
			//final OrderEntryAndDiscountValue orderEntryAndDiscountValue = findOrderEntryDiscountValue(ctx, order, getGuid(ctx));
			final DiscountValue discountValue = findOrderEntryDiscountValue(ctx, orderEntry, getGuid(ctx));
			if (discountValue != null)
			{
				needsCalc = calculateApportionedDiscount(orderEntry, ctx, isFreebiePromo, orderEntryAdjustment, promotion);
			}

			//needsCalc = true;
		}
		else
		{
			log.error("(" + getPK() + ") apply: Could not find an order entry to adjust with product '" + getOrderEntryProduct(ctx)
					+ "' and quantity '" + getOrderEntryQuantity(ctx) + "'");
		}

		//setMarkedApplied(ctx, true);

		return needsCalc;
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
				break;
			}

		}
		else
		{
			result = order.getEntry(orderEntryNumber.intValue());
		}

		return result;
	}

	/**
	 * @Description : This method is called when promotional products are removed from cart and cart is recalculated.
	 * @param : ctx
	 * @return : true/false
	 */
	@Override
	public boolean undo(final SessionContext ctx)
	{
		final AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);

		if (log.isDebugEnabled())
		{
			log.debug("(" + getPK() + ") undo: Undoing order entry adjustment for order [" + order.getPK() + "]");
		}

		boolean calculateTotals = false;

		final OrderEntryAndDiscountValue orderEntryAndDiscountValue = findOrderEntryDiscountValue(ctx, order, getGuid(ctx));
		if (orderEntryAndDiscountValue != null)
		{
			orderEntryAndDiscountValue.getKey().removeDiscountValue(ctx, orderEntryAndDiscountValue.getValue());
			calculateTotals = true;
		}

		setMarkedApplied(ctx, false);

		return calculateTotals;
	}

	/**
	 * @Description : OOB method
	 * @param : ctx
	 * @return : true/false
	 */
	@Override
	public boolean isAppliedToOrder(final SessionContext ctx)
	{
		final AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);

		if (log.isDebugEnabled())
		{
			log.debug("(" + getPK() + ") isAppliedToOrder: Checking if this action is applied to order [" + order.getPK() + "]");
		}

		return (findOrderEntryDiscountValue(ctx, order, getGuid(ctx)) != null);
	}

	/**
	 * @Description : OOB method
	 * @param : ctx
	 * @return : double
	 */
	@Override
	public double getValue(final SessionContext ctx)
	{
		return (-1.0D * getAmount(ctx).doubleValue());
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
	 * @Description : Find Discount Value for Order Entry
	 * @param : ctx
	 * @param : order
	 * @param : discountValueCode
	 * @return : OrderEntryAndDiscountValue
	 */
	private static OrderEntryAndDiscountValue findOrderEntryDiscountValue(final SessionContext ctx, final AbstractOrder order,
			final String discountValueCode)
	{
		final Collection<AbstractOrderEntry> entries = order.getAllEntries();
		for (final AbstractOrderEntry entry : entries)
		{
			final DiscountValue discountValue = findOrderEntryDiscountValue(ctx, entry, discountValueCode);
			if (discountValue != null)
			{
				return new OrderEntryAndDiscountValue(entry, discountValue);
			}
		}
		return null;
	}

	/**
	 * @Description : OOB method
	 * @param : ctx
	 * @param : orderEntry
	 * @param : discountValueCode
	 * @return : DiscountValue
	 */
	private static DiscountValue findOrderEntryDiscountValue(final SessionContext ctx, final AbstractOrderEntry orderEntry,
			final String discountValueCode)
	{
		final Collection<DiscountValue> discounts = orderEntry.getDiscountValues(ctx);
		for (final DiscountValue dv : discounts)
		{
			if (discountValueCode.equals(dv.getCode()))
			{
				return dv;
			}
		}
		return null;
	}

	static final class OrderEntryAndDiscountValue extends Pair<AbstractOrderEntry, DiscountValue>
	{
		public OrderEntryAndDiscountValue(final AbstractOrderEntry key, final DiscountValue value)
		{
			super(key, value);
		}
	}

	/**
	 * @param promotion
	 * @Description : This is for apportioning calculation
	 * @param : ctx
	 * @param : orderEntry
	 * @param : isFreebiePromo
	 * @return : orderEntryAdjustment
	 */
	private boolean calculateApportionedDiscount(final AbstractOrderEntry orderEntry, final SessionContext ctx,
			final boolean isFreebiePromo, final double orderEntryAdjustment, final AbstractPromotion promotion)
	{
		boolean needsCalc = false;
		double percentageDiscount = 0.00D;
		//Map<String, Integer> qualifyingCountMap = null;
		String productPromoCode = null;

		//TPR-7408 starts here
		Double promoCostCentreOnePercentage = null;
		Double promoCostCentreTwoPercentage = null;
		Double promoCostCentreThreePercentage = null;
		//TPR-7408 ends here



		Map<String, List<String>> productAssociatedItemsMap = null;
		boolean isPercentageDisc = false;

		if (ctx.getAttributes() != null)
		{
			percentageDiscount = ctx.getAttributes().get(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT) != null ? ((Double) ctx
					.getAttributes().get(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT)).doubleValue() : 0.00D;
			//			qualifyingCountMap = ctx.getAttributes().get(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT) != null ? (Map<String, Integer>) ctx
			//					.getAttributes().get(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT) : null;
			productPromoCode = ctx.getAttributes().get(MarketplacecommerceservicesConstants.PROMOCODE) != null ? (String) ctx
					.getAttributes().get(MarketplacecommerceservicesConstants.PROMOCODE) : null;
			productAssociatedItemsMap = ctx.getAttributes().get(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) != null ? (Map<String, List<String>>) ctx
					.getAttributes().get(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) : null;
			isPercentageDisc = ctx.getAttribute(MarketplacecommerceservicesConstants.ISPERCENTAGEDISC) != null ? ((Boolean) ctx
					.getAttribute(MarketplacecommerceservicesConstants.ISPERCENTAGEDISC)).booleanValue() : false;

			//TPR-7408 starts here
			//			promoCostCentreOnePercentage = ctx.getAttributes().get(MarketplacecommerceservicesConstants.COSTCENTREONE) != null ? (String) ctx
			//					.getAttributes().get(MarketplacecommerceservicesConstants.COSTCENTREONE) : null;
			//
			//			promoCostCentreTwoPercentage = ctx.getAttributes().get(MarketplacecommerceservicesConstants.COSTCENTRETWO) != null ? (String) ctx
			//					.getAttributes().get(MarketplacecommerceservicesConstants.COSTCENTRETWO) : null;
			//
			//			promoCostCentreThreePercentage = ctx.getAttributes().get(MarketplacecommerceservicesConstants.COSTCENTRETHREE) != null ? (String) ctx
			//					.getAttributes().get(MarketplacecommerceservicesConstants.COSTCENTRETHREE) : null;

			try
			{
				promoCostCentreOnePercentage = (Double) promotion.getAttribute(ctx,
						MarketplacecommerceservicesConstants.COSTCENTREONE);
				promoCostCentreTwoPercentage = (Double) promotion.getAttribute(ctx,
						MarketplacecommerceservicesConstants.COSTCENTRETWO);

				promoCostCentreThreePercentage = (Double) promotion.getAttribute(ctx,
						MarketplacecommerceservicesConstants.COSTCENTRETHREE);

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

			//TPR-7408 ends here


		}

		//String validProductUSSID = null;
		try
		{
			final String validProductUSSID = (String) orderEntry.getAttribute(ctx,
					MarketplacecommerceservicesConstants.SELECTEDUSSID);
			//			int qualifyingCount = 0;
			//			if (null != qualifyingCountMap && !qualifyingCountMap.isEmpty())
			//			{
			//				qualifyingCount = qualifyingCountMap.get(validProductUSSID).intValue();
			//			}
			final int qualifyingCount = getOrderEntryQuantity(ctx).intValue();
			List<String> associatedItemsList = new ArrayList<String>();
			if (null != productAssociatedItemsMap && !productAssociatedItemsMap.isEmpty())
			{
				associatedItemsList = productAssociatedItemsMap.get(validProductUSSID);
			}

			//Modified for INC144315231
			//final double lineItemLevelPrice = orderEntry.getTotalPriceAsPrimitive();
			final double lineItemLevelPrice = orderEntry.getBasePriceAsPrimitive() * orderEntry.getQuantityAsPrimitive();

			final double amtTobeDeductedAtlineItemLevel = -1.0D * orderEntryAdjustment;

			final double aportionedItemValue = lineItemLevelPrice - amtTobeDeductedAtlineItemLevel;
			final double cartLevelDiscount = 0.00D;
			final double netAmountAfterAllDisc = aportionedItemValue - cartLevelDiscount;

			final List<String> prevAssociatedItemList = orderEntry.getProperty(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) != null ? (List<String>) orderEntry
					.getProperty(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) : null;
			if (prevAssociatedItemList != null && !prevAssociatedItemList.isEmpty())
			{
				final Set associatedItemsSet = new HashSet(prevAssociatedItemList);
				associatedItemsSet.addAll(associatedItemsList);

				associatedItemsList.clear();
				associatedItemsList.addAll(associatedItemsSet);
			}

			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.DESCRIPTION, orderEntry.getProduct().getDescription());
			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, Integer.valueOf(qualifyingCount));

			//****New Code Added for TISPRO-670*******
			if (isFreebiePromo)
			{
				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, associatedItemsList);
			}
			//****New Code Added for TISPRO-670 ends*******

			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, productPromoCode);

			//TPR-7408 starts here
			if (null != promoCostCentreOnePercentage)
			{
				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCOSTCENTREONE,
						promoCostCentreOnePercentage);
			}

			if (null != promoCostCentreTwoPercentage)
			{
				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCOSTCENTRETWO,
						promoCostCentreTwoPercentage);
			}

			if (null != promoCostCentreThreePercentage)
			{
				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCOSTCENTRETHREE,
						promoCostCentreThreePercentage);
			}
			//TPR-7408 ends here

			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALSALEPRICE, Double.valueOf(lineItemLevelPrice));
			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC,
					Double.valueOf(amtTobeDeductedAtlineItemLevel));
			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETSELLINGPRICE, Double.valueOf(aportionedItemValue));
			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTLEVELDISC, Double.valueOf(cartLevelDiscount));
			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETAMOUNTAFTERALLDISC,
					Double.valueOf(netAmountAfterAllDisc));
			//if (isPercentageDisc && (-1.0D * orderEntryAdjustment) > 0)
			if (isPercentageDisc && (amtTobeDeductedAtlineItemLevel > 0))
			{
				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.ISPERCENTAGEDISC, Boolean.valueOf(isPercentageDisc));
				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODLEVELPERCENTAGEDISC,
						Double.valueOf(percentageDiscount));
			}

			needsCalc = true;
			setMarkedApplied(ctx, true);
		}
		catch (final JaloInvalidParameterException | JaloSecurityException e)
		{
			undo(ctx);
			log.error(e);
		}
		catch (final Exception e)
		{
			undo(ctx);
			log.error(e);
		}

		return needsCalc;
	}
	//	protected DefaultPromotionManager getDefaultPromotionsManager()
	//	{
	//		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	//	}
}