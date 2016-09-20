package com.tisl.mpl.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.CartEntry;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.DiscountValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.jalo.CustomPromotionOrderEntryAdjustAction.OrderEntryAndDiscountValue;


public class CustomBuyAgetPercentageDiscountOnBAdjustAction extends GeneratedCustomBuyAgetPercentageDiscountOnBAdjustAction
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CustomBuyAgetPercentageDiscountOnBAdjustAction.class.getName());


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
	@SuppressWarnings("deprecation")
	@Override
	public boolean apply(final SessionContext ctx)
	{

		final AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
		boolean isFreebiePromo = false;

		//****New Code Added for TISPRO-670*******
		if (null != getPromotionResult(ctx) && null != getPromotionResult(ctx).getPromotion()
				&& getPromotionResult(ctx).getPromotion() instanceof BuyAGetPercentageDiscountOnB)
		{
			isFreebiePromo = true;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Freebie Plus Discount Promotion : Adding Associated Item Data");
			}
		}
		//****New Code Added for TISPRO-670 ends *******

		if (LOG.isDebugEnabled())
		{
			LOG.debug("(" + getPK() + ") apply: Applying OrderEntry adjustment action for order [" + order.getPK() + "]");
		}
		boolean needsCalc = false;
		final Integer orderEntryNumber = getOrderEntryNumber(ctx);

		double percentageDiscount = 0.00D;
		double totalvalidproductsPricevalue = 0.00D;
		Map<String, AbstractOrderEntry> validProductList = null;
		Map<String, Integer> qualifyingCountMap = null;
		String productPromoCode = null;
		Map<String, List<String>> productAssociatedItemsMap = null;
		boolean isPercentageDisc = false;

		if (ctx.getAttributes() != null)
		{
			percentageDiscount = ctx.getAttributes().get(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT) != null ? ((Double) ctx
					.getAttributes().get(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT)).doubleValue() : 0.00D;
			totalvalidproductsPricevalue = ctx.getAttributes()
					.get(MarketplacecommerceservicesConstants.TOTALVALIDPRODUCTSPRICEVALUE) != null ? ((Double) ctx.getAttributes()
					.get(MarketplacecommerceservicesConstants.TOTALVALIDPRODUCTSPRICEVALUE)).doubleValue() : 0.00D;
			validProductList = ctx.getAttributes().get(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST) != null ? (Map<String, AbstractOrderEntry>) ctx
					.getAttributes().get(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST) : null;
			qualifyingCountMap = ctx.getAttributes().get(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT) != null ? (Map<String, Integer>) ctx
					.getAttributes().get(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT) : null;
			productPromoCode = ctx.getAttributes().get(MarketplacecommerceservicesConstants.PROMOCODE) != null ? (String) ctx
					.getAttributes().get(MarketplacecommerceservicesConstants.PROMOCODE) : null;
			productAssociatedItemsMap = ctx.getAttributes().get(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) != null ? (Map<String, List<String>>) ctx
					.getAttributes().get(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) : null;
			isPercentageDisc = ctx.getAttribute(MarketplacecommerceservicesConstants.ISPERCENTAGEDISC) != null ? ((Boolean) ctx
					.getAttribute(MarketplacecommerceservicesConstants.ISPERCENTAGEDISC)).booleanValue() : false;
		}

		final AbstractOrderEntry orderEntry = findOrderEntry(order, ctx, orderEntryNumber);

		if (orderEntry != null)
		{
			double totalAmtDeductedOnItemLevel = 0.00D;

			if (validProductList != null)
			{
				final Iterator iter = validProductList.entrySet().iterator();
				while (iter.hasNext())
				{
					final Map.Entry mapEntry = (Map.Entry) iter.next();
					final CartEntry cartEntry = (CartEntry) mapEntry.getValue();
					final String validProductUSSID = (String) mapEntry.getKey();
					double amtTobeDeductedAtlineItemLevel = 0.00D;
					int qualifyingCount = 0;
					if (null != qualifyingCountMap && !qualifyingCountMap.isEmpty())
					{
						qualifyingCount = qualifyingCountMap.get(validProductUSSID).intValue();
					}
					List<String> associatedItemsList = new ArrayList<String>();
					if (null != productAssociatedItemsMap && !productAssociatedItemsMap.isEmpty())
					{
						associatedItemsList = productAssociatedItemsMap.get(validProductUSSID);
					}
					final double lineItemLevelPrice = cartEntry.getTotalPriceAsPrimitive();
					final double validQualifyingProdPrice = cartEntry.getBasePriceAsPrimitive() * qualifyingCount;

					if (validProductList.size() == 1)//For Buy x of A get per/ amt off, for the last element of validProductList
					{
						final double discountPriceValue = (percentageDiscount * totalvalidproductsPricevalue) / 100;
						amtTobeDeductedAtlineItemLevel = discountPriceValue - totalAmtDeductedOnItemLevel;
					}
					else
					{
						amtTobeDeductedAtlineItemLevel = (percentageDiscount * validQualifyingProdPrice) / 100;
						totalAmtDeductedOnItemLevel += amtTobeDeductedAtlineItemLevel;

					}

					final double aportionedItemValue = lineItemLevelPrice - amtTobeDeductedAtlineItemLevel;
					final double cartLevelDiscount = 0.00D;
					final double netAmountAfterAllDisc = aportionedItemValue - cartLevelDiscount;

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

					//****New Code Added for TISPRO-670*******
					if (isFreebiePromo)
					{
						cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, associatedItemsList);
					}
					//****New Code Added for TISPRO-670 ends*******

					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, productPromoCode);
					cartEntry
							.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALSALEPRICE, Double.valueOf(lineItemLevelPrice));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC,
							Double.valueOf(amtTobeDeductedAtlineItemLevel));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETSELLINGPRICE,
							Double.valueOf(aportionedItemValue));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTLEVELDISC, Double.valueOf(cartLevelDiscount));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETAMOUNTAFTERALLDISC,
							Double.valueOf(netAmountAfterAllDisc));
					if (isPercentageDisc)
					{
						cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.ISPERCENTAGEDISC,
								Boolean.valueOf(isPercentageDisc));
						cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODLEVELPERCENTAGEDISC,
								Double.valueOf(percentageDiscount));
					}
					iter.remove();
				}
			}
			final double orderEntryAdjustment = getAmount(ctx).doubleValue();

			final double unitAdjustment = orderEntryAdjustment / orderEntry.getQuantity(ctx).longValue();

			final String code = getGuid(ctx);
			@SuppressWarnings("deprecation")
			final DiscountValue dv = new DiscountValue(code, -1.0D * unitAdjustment, true, order.getCurrency(ctx).getIsoCode(ctx));

			insertFirstOrderEntryDiscountValue(ctx, orderEntry, dv);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("(" + getPK() + ") apply: Creating an adjustment of " + getAmount(ctx) + " to order entry '"
						+ orderEntry.getPK() + "'.  Order entry now has " + orderEntry.getDiscountValues(ctx).size() + " adjustments");
			}
			needsCalc = true;
		}
		else
		{
			LOG.error("(" + getPK() + ") apply: Could not find an order entry to adjust with product '" + getOrderEntryProduct(ctx)
					+ "' and quantity '" + getOrderEntryQuantity(ctx) + "'");
		}

		setMarkedApplied(ctx, true);

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
			}

		}
		else
		{
			result = order.getEntry(orderEntryNumber.intValue());
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.promotions.jalo.AbstractPromotionAction#getValue(de.hybris.platform.jalo.SessionContext)
	 */
	@Override
	public double getValue(final SessionContext arg0)
	{
		// YTODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.promotions.jalo.AbstractPromotionAction#isAppliedToOrder(de.hybris.platform.jalo.SessionContext
	 * )
	 */
	@Override
	public boolean isAppliedToOrder(final SessionContext arg0)
	{
		// YTODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.promotions.jalo.AbstractPromotionAction#undo(de.hybris.platform.jalo.SessionContext)
	 */
	@Override
	public boolean undo(final SessionContext ctx)
	{
		final AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("(" + getPK() + ") undo: Undoing order entry adjustment for order [" + order.getPK() + "]");
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
		@SuppressWarnings("deprecation")
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


}
