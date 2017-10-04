package com.tisl.mpl.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.util.DiscountValue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;


/**
 * Customized action class for order level promotions
 *
 */
@SuppressWarnings("deprecation")
public class CustomPromotionOrderAdjustTotalAction extends GeneratedCustomPromotionOrderAdjustTotalAction
{
	@SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger(CustomPromotionOrderAdjustTotalAction.class.getName());

	/**
	 * @Description : This method is for creating item type
	 * @param: ctx
	 * @param: type
	 * @param: allAttributes
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
	 * @param: ctx
	 * @return : true/false
	 */
	@Override
	public boolean apply(final SessionContext ctx)
	{
		boolean needsCalc = false;
		final AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
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

		//Apportioning calculation
		final DiscountValue discountValue = Helper.findGlobalDiscountValue(ctx, order, getGuid(ctx));
		if (discountValue != null)
		{
			needsCalc = calculateApportionedDiscount(order, ctx);
		}

		//setMarkedApplied(ctx, true);

		return needsCalc;
	}

	/**
	 * Added for TISPRO-318
	 *
	 * @param ctx
	 * @param entry
	 * @return freeItemPrice
	 */
	private double getfreeItemPrice(final SessionContext ctx, final AbstractOrderEntry entry)
	{
		double freeItemPrice = 0;
		try
		{
			if (getMplPromotionHelper().validateEntryForFreebie(entry))
			{
				freeItemPrice = entry.getTotalPrice().doubleValue();
			}
			else if ((null != entry.getAttribute(ctx, "isBOGOapplied")
					&& BooleanUtils.toBoolean(entry.getAttribute(ctx, "isBOGOapplied").toString()) && null != entry.getAttribute(ctx,
					"bogoFreeItmCount")))
			{
				final double freecount = Double.parseDouble(entry.getAttribute(ctx, "bogoFreeItmCount").toString());
				freeItemPrice = (freecount * 0.01);
			}
		}
		catch (final Exception exception)
		{
			log.debug(exception.getMessage());
			freeItemPrice = 0;
		}
		return freeItemPrice;
	}

	/**
	 * @Description : This method is called when promotional products are removed from cart and cart is recalculated.
	 * @param: ctx
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
		setMarkedApplied(ctx, false);

		return calculateTotals;
	}

	/**
	 * @Description : OOB method
	 * @param: ctx
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
	 * @param: ctx
	 * @return : true/false
	 */
	@Override
	public double getValue(final SessionContext ctx)
	{
		return (-1.0D * getAmount(ctx).doubleValue());
	}

	private boolean calculateApportionedDiscount(final AbstractOrder order, final SessionContext ctx)
	{
		boolean needsCalc = false;
		double percentageDiscount = 0.00D;
		double totalvalidproductsPricevalue = 0.00D;
		String cartPromoCode = null;
		boolean isPercentageDisc = false;


		//CR Changes : TPR-715
		Map<String, AbstractOrderEntry> validProductUssidMap = new ConcurrentHashMap<String, AbstractOrderEntry>();
		boolean isSellerPresent = false;
		boolean validateSellerData = false;
		String selectedUSSID = MarketplacecommerceservicesConstants.EMPTYSPACE;
		//CR Changes : TPR-715 : Variable declaration ends

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

			//CR Changes : TPR-715 starts
			validProductUssidMap = (ctx.getAttribute(MarketplacecommerceservicesConstants.CART_SELLER_PRODUCTS) != null ? (Map<String, AbstractOrderEntry>) ctx
					.getAttribute(MarketplacecommerceservicesConstants.CART_SELLER_PRODUCTS)
					: new ConcurrentHashMap<String, AbstractOrderEntry>());
			isSellerPresent = ctx.getAttribute(MarketplacecommerceservicesConstants.VALIDATE_SELLER) != null ? ((Boolean) ctx
					.getAttribute(MarketplacecommerceservicesConstants.VALIDATE_SELLER)).booleanValue() : false;
			//CR Changes : TPR-715 ends
		}

		//CR Changes : TPR-715
		if (MapUtils.isNotEmpty(validProductUssidMap))
		{
			validateSellerData = true;
		}//CR Changes : TPR-715 ends

		final List<AbstractOrderEntry> listOfEntries = order.getEntries();
		double totalAmtDeductedOnItemLevel = 0.00D;

		for (final AbstractOrderEntry entry : order.getEntries())
		{
			//CR Changes : TPR-715
			try
			{
				selectedUSSID = (String) entry.getAttribute(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID);

				if ((!validateSellerData && !isSellerPresent)
						|| (validateSellerData && isSellerPresent && validProductUssidMap.containsKey(selectedUSSID) && !entry
								.isGiveAway().booleanValue())
						|| (validateSellerData && !isSellerPresent && validProductUssidMap.containsKey(selectedUSSID) && !entry
								.isGiveAway().booleanValue()))//Condition modified for PR-15
				{
					double amtTobeDeductedAtlineItemLevel = 0.00D;
					final AbstractOrderEntry cartEntry = entry;
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

						final double freeItemPrice = getfreeItemPrice(ctx, entry); //Added for TISPRO-318
						amtTobeDeductedAtlineItemLevel = (percentageDiscount * (netSellingPrice - freeItemPrice)) / 100;
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
					cartEntry
							.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALSALEPRICE, Double.valueOf(lineItemLevelPrice));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC,
							Double.valueOf(productLevelDiscount));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETSELLINGPRICE, Double.valueOf(netSellingPrice));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTLEVELDISC,
							Double.valueOf(amtTobeDeductedAtlineItemLevel));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETAMOUNTAFTERALLDISC,
							Double.valueOf(aportionedItemValue));
					if (isPercentageDisc)
					{
						cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.ISPERCENTAGEDISC,
								Boolean.valueOf(isPercentageDisc));
						cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTLEVELPERCENTAGEDISC,
								Double.valueOf(percentageDiscount));
					}
				} //CR Changes : TPR-715 ends

				needsCalc = true;
				setMarkedApplied(ctx, true);
			}
			catch (JaloInvalidParameterException | JaloSecurityException e)
			{
				undo(ctx);
				log.error(e);
			}
			catch (final Exception e)
			{
				undo(ctx);
				log.error(e);
			}
		}

		return needsCalc;
	}

	/**
	 * @Description : OOB method
	 * @param: ctx
	 * @return: true/false
	 */
	@Override
	protected void deepCloneAttributes(final SessionContext ctx, final Map values)
	{
		super.deepCloneAttributes(ctx, values);
	}

	//	protected DefaultPromotionManager getDefaultPromotionsManager()
	//	{
	//		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	//	}

	//For Referring to Promotion Helper Class
	protected MplPromotionHelper getMplPromotionHelper()
	{
		return Registry.getApplicationContext().getBean("mplPromotionHelper", MplPromotionHelper.class);
	}

}
