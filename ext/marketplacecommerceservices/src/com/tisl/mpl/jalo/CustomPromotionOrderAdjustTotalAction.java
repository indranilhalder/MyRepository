package com.tisl.mpl.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.CartEntry;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.util.DiscountValue;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


public class CustomPromotionOrderAdjustTotalAction extends GeneratedCustomPromotionOrderAdjustTotalAction
{
	@SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger(CustomPromotionOrderAdjustTotalAction.class.getName());

	/**
	 * @Description : This method is for creating item type
	 * @param : ctx
	 * @param : type
	 * @param : allAttributes
	 * @return : item
	 */
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
	 * @Description : This method is called when promotion is applied
	 * @param : ctx
	 * @return : true/false
	 */
	@Override
	public boolean apply(final SessionContext ctx)
	{
		final AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
		double percentageDiscount = 0.00D;
		double totalvalidproductsPricevalue = 0.00D;
		String cartPromoCode = null;
		boolean isPercentageDisc = false;

		if (ctx.getAttributes() != null)
		{
			percentageDiscount = ctx.getAttributes().get(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT) != null ? ((Double) ctx
					.getAttributes().get(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT)).doubleValue() : 0.00D;
			totalvalidproductsPricevalue = ctx.getAttributes()
					.get(MarketplacecommerceservicesConstants.TOTALVALIDPRODUCTSPRICEVALUE) != null ? ((Double) ctx.getAttributes()
					.get(MarketplacecommerceservicesConstants.TOTALVALIDPRODUCTSPRICEVALUE)).doubleValue() : 0.00D;
			cartPromoCode = ctx.getAttributes().get(MarketplacecommerceservicesConstants.PROMOCODE) != null ? (String) ctx
					.getAttributes().get(MarketplacecommerceservicesConstants.PROMOCODE) : null;
			isPercentageDisc = ctx.getAttribute(MarketplacecommerceservicesConstants.ISPERCENTAGEDISC) != null ? ((Boolean) ctx
					.getAttribute(MarketplacecommerceservicesConstants.ISPERCENTAGEDISC)).booleanValue() : false;
		}

		final List<AbstractOrderEntry> listOfEntries = order.getEntries();
		double totalAmtDeductedOnItemLevel = 0.00D;

		for (final AbstractOrderEntry entry : order.getEntries())
		{
			double amtTobeDeductedAtlineItemLevel = 0.00D;
			final CartEntry cartEntry = (CartEntry) entry;
			final double lineItemLevelPrice = cartEntry.getBasePriceAsPrimitive() * cartEntry.getQuantityAsPrimitive();
			double productLevelDiscount = 0.00D;
			if (null != cartEntry.getProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE)
					&& !((String) cartEntry.getProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE)).isEmpty())
			{
				productLevelDiscount += ((Double) cartEntry.getProperty(ctx,
						MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC)).doubleValue();

			}
			final double netSellingPrice = lineItemLevelPrice - productLevelDiscount;

			if (listOfEntries.indexOf(entry) == (listOfEntries.size() - 1))
			{
				final double discountPriceValue = (percentageDiscount * totalvalidproductsPricevalue) / 100;
				amtTobeDeductedAtlineItemLevel = discountPriceValue - totalAmtDeductedOnItemLevel;
			}
			else
			{
				amtTobeDeductedAtlineItemLevel = (percentageDiscount * netSellingPrice) / 100;
				totalAmtDeductedOnItemLevel += amtTobeDeductedAtlineItemLevel;
			}

			double aportionedItemValue = lineItemLevelPrice - amtTobeDeductedAtlineItemLevel;

			if (null != cartEntry.getProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE)
					&& !((String) cartEntry.getProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE)).isEmpty())
			{
				aportionedItemValue = netSellingPrice - amtTobeDeductedAtlineItemLevel;
			}
			cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.DESCRIPTION, entry.getProduct().getDescription());
			cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTPROMOCODE, cartPromoCode);
			cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALSALEPRICE, Double.valueOf(lineItemLevelPrice));
			cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC,
					Double.valueOf(productLevelDiscount));
			cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETSELLINGPRICE, Double.valueOf(netSellingPrice));
			cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTLEVELDISC,
					Double.valueOf(amtTobeDeductedAtlineItemLevel));
			cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETAMOUNTAFTERALLDISC,
					Double.valueOf(aportionedItemValue));
			if (isPercentageDisc)
			{
				cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.ISPERCENTAGEDISC, Boolean.valueOf(isPercentageDisc));
				cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTLEVELPERCENTAGEDISC,
						Double.valueOf(percentageDiscount));
			}
		}

		final String code = getGuid(ctx);

		if (log.isDebugEnabled())
		{
			log.debug("(" + getPK() + ") apply: Order total is currently: " + order.getTotal(ctx));
		}

		final DiscountValue dv = new DiscountValue(code, getAmount(ctx).doubleValue() * -1.0D, true, order.getCurrency(ctx)
				.getIsoCode(ctx));
		insertFirstGlobalDiscountValue(ctx, order, dv);

		if (log.isDebugEnabled())
		{
			log.debug("(" + getPK() + ") apply: Generated discount with name '" + code + "' for " + getAmount(ctx));
		}

		setMarkedApplied(ctx, true);

		return true;
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
		boolean calculateTotals = false;

		final DiscountValue myDiscount = Helper.findGlobalDiscountValue(ctx, order, getGuid(ctx));
		if (myDiscount != null)
		{
			order.removeGlobalDiscountValue(ctx, myDiscount);
			calculateTotals = true;
		}

		//		if (order != null)
		//		{
		//			getDefaultPromotionsManager().undoPromotionalAttributes(ctx, order.getEntries());
		//		}

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

		return (Helper.findGlobalDiscountValue(ctx, order, getGuid(ctx)) != null);
	}

	/**
	 * @Description : OOB method
	 * @param : ctx
	 * @return : true/false
	 */
	@Override
	public double getValue(final SessionContext ctx)
	{
		return (-1.0D * getAmount(ctx).doubleValue());
	}

	/**
	 * @Description : OOB method
	 * @param : ctx
	 * @return : true/false
	 */
	@Override
	protected void deepCloneAttributes(final SessionContext ctx, final Map values)
	{
		super.deepCloneAttributes(ctx, values);
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

}