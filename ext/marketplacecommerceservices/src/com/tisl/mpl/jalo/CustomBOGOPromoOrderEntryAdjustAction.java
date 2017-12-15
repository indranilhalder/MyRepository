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
import de.hybris.platform.promotions.jalo.AbstractPromotion;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
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
 * Customized action class for BOGO promotion
 *
 */
public class CustomBOGOPromoOrderEntryAdjustAction extends GeneratedCustomBOGOPromoOrderEntryAdjustAction
{
	@SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger(CustomBOGOPromoOrderEntryAdjustAction.class.getName());

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
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean apply(final SessionContext ctx)
	{
		final PromotionResult result = getPromotionResult(ctx);
		final AbstractOrder order = result.getOrder(ctx);

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

			//Apportioning calculation
			final DiscountValue discountValue = findOrderEntryDiscountValue(ctx, orderEntry, getGuid(ctx));
			if (discountValue != null)
			{
				needsCalc = calculateApportionedDiscount(orderEntry, ctx, orderEntryAdjustment, result);
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
	 * @param result
	 * @Description : This is for apportioning calculation
	 * @param : ctx
	 * @param : orderEntry
	 * @return : orderEntryAdjustment
	 */
	private boolean calculateApportionedDiscount(final AbstractOrderEntry orderEntry, final SessionContext ctx,
			final double orderEntryAdjustment, final PromotionResult result)
	{
		boolean needsCalc = false;
		//Map<String, Integer> qualifyingCountMap = null;
		String productPromoCode = null;

		//TPR-7408 starts here
		Double promoCostCentreOnePercentage = null;
		Double promoCostCentreTwoPercentage = null;
		Double promoCostCentreThreePercentage = null;
		//TPR-7408 ends here

		Map<String, List<String>> productAssociatedItemsMap = null;
		Map<String, Integer> freeItemsForCatBogo = null;
		List<PromotionOrderEntryConsumed> nonFreeConsumedEntries = null;

		if (ctx.getAttributes() != null)
		{
			//			qualifyingCountMap = ctx.getAttributes().get(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT) != null ? (Map<String, Integer>) ctx
			//					.getAttributes().get(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT) : null;
			productPromoCode = ctx.getAttributes().get(MarketplacecommerceservicesConstants.PROMOCODE) != null ? (String) ctx
					.getAttributes().get(MarketplacecommerceservicesConstants.PROMOCODE) : null;
			productAssociatedItemsMap = ctx.getAttributes().get(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) != null ? (Map<String, List<String>>) ctx
					.getAttributes().get(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) : null;
			freeItemsForCatBogo = ctx.getAttributes().get(MarketplacecommerceservicesConstants.FREEITEMFORCATBOGO) != null ? (Map<String, Integer>) ctx
					.getAttributes().get(MarketplacecommerceservicesConstants.FREEITEMFORCATBOGO) : null;

			nonFreeConsumedEntries = ctx.getAttributes().get(MarketplacecommerceservicesConstants.NONFREE_CONSUMED_ENTRIES) != null ? (List<PromotionOrderEntryConsumed>) ctx
					.getAttributes().get(MarketplacecommerceservicesConstants.NONFREE_CONSUMED_ENTRIES) : null;


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
				final AbstractPromotion promotion = result.getPromotion();
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

		try
		{
			boolean isEntryFreeNonFreeBoth = false;
			int nonFreeCount = 0;
			final String freeEntryUSSID = (String) orderEntry.getAttribute(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID);
			double amtTobeDeductedAtlineItemLevel = 0.00D;
			int qCount = 0;

			for (final PromotionOrderEntryConsumed consumedNonFree : nonFreeConsumedEntries)
			{
				final AbstractOrderEntry nonFreeEntry = consumedNonFree.getOrderEntry();
				nonFreeCount = consumedNonFree.getQuantity().intValue();
				final String nonFreeEntryUSSID = (String) nonFreeEntry.getAttribute(ctx,
						MarketplacecommerceservicesConstants.SELECTEDUSSID);

				if (nonFreeEntry.equals(orderEntry))
				{
					isEntryFreeNonFreeBoth = true;
					break;
				}
				else if (!freeItemsForCatBogo.containsKey(nonFreeEntryUSSID))
				{
					qCount = nonFreeCount;
					setPrices(nonFreeEntry, amtTobeDeductedAtlineItemLevel, ctx);
					setAssociatedItem(nonFreeEntryUSSID, productAssociatedItemsMap, nonFreeEntry, ctx);
					setQcAndOthers(nonFreeEntry, ctx, qCount, productPromoCode, promoCostCentreOnePercentage,
							promoCostCentreTwoPercentage, promoCostCentreThreePercentage);//parameter added for TPR-7408
				}
			}

			if (isEntryFreeNonFreeBoth)
			{
				amtTobeDeductedAtlineItemLevel = -1.0D * orderEntryAdjustment;
				//INC144316050
				qCount = getOrderEntryQuantity(ctx).intValue() + nonFreeCount;
				//qCount = nonFreeCount;

				setPrices(orderEntry, amtTobeDeductedAtlineItemLevel, ctx);
				setAssociatedItem(freeEntryUSSID, productAssociatedItemsMap, orderEntry, ctx);
				setQcAndOthers(orderEntry, ctx, qCount, productPromoCode, promoCostCentreOnePercentage, promoCostCentreTwoPercentage,
						promoCostCentreThreePercentage);//parameter added for TPR-7408
				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.ISBOGOAPPLIED, Boolean.TRUE);
				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.FREECOUNT,
						Integer.valueOf(getOrderEntryQuantity(ctx).intValue()));
			}
			else
			{
				amtTobeDeductedAtlineItemLevel = -1.0D * orderEntryAdjustment;
				qCount = getOrderEntryQuantity(ctx).intValue();

				setPrices(orderEntry, amtTobeDeductedAtlineItemLevel, ctx);
				setAssociatedItem(freeEntryUSSID, productAssociatedItemsMap, orderEntry, ctx);
				setQcAndOthers(orderEntry, ctx, qCount, productPromoCode, promoCostCentreOnePercentage, promoCostCentreTwoPercentage,
						promoCostCentreThreePercentage);//parameter added for TPR-7408
				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.ISBOGOAPPLIED, Boolean.TRUE);
				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.FREECOUNT,
						Integer.valueOf(getOrderEntryQuantity(ctx).intValue()));
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

	private void setAssociatedItem(final String ussid, final Map<String, List<String>> productAssociatedItemsMap,
			final AbstractOrderEntry entry, final SessionContext ctx)
	{
		List<String> associatedItemsList = new ArrayList<String>();
		if (null != productAssociatedItemsMap && !productAssociatedItemsMap.isEmpty())
		{
			associatedItemsList = productAssociatedItemsMap.get(ussid);
		}

		final List<String> prevAssociatedItemList = entry.getProperty(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) != null ? (List<String>) entry
				.getProperty(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) : null;
		if (prevAssociatedItemList != null && !prevAssociatedItemList.isEmpty())
		{
			final Set associatedItemsSet = new HashSet(prevAssociatedItemList);
			associatedItemsSet.addAll(associatedItemsList);

			associatedItemsList.clear();
			associatedItemsList.addAll(associatedItemsSet);
		}

		entry.setProperty(ctx, MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, associatedItemsList);
	}

	private void setPrices(final AbstractOrderEntry entry, final double amtTobeDeductedAtlineItemLevel, final SessionContext ctx)
	{
		//Modified for INC144315231
		//final double lineItemLevelPrice = entry.getTotalPriceAsPrimitive();
		final double lineItemLevelPrice = entry.getBasePriceAsPrimitive() * entry.getQuantityAsPrimitive();
		final double aportionedItemValue = lineItemLevelPrice - amtTobeDeductedAtlineItemLevel;
		final double cartLevelDiscount = 0.00D;
		final double netAmountAfterAllDisc = aportionedItemValue - cartLevelDiscount;

		entry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALSALEPRICE, Double.valueOf(lineItemLevelPrice));
		entry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC,
				Double.valueOf(amtTobeDeductedAtlineItemLevel));
		entry.setProperty(ctx, MarketplacecommerceservicesConstants.NETSELLINGPRICE, Double.valueOf(aportionedItemValue));
		entry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTLEVELDISC, Double.valueOf(cartLevelDiscount));
		entry.setProperty(ctx, MarketplacecommerceservicesConstants.NETAMOUNTAFTERALLDISC, Double.valueOf(netAmountAfterAllDisc));
	}

	private void setQcAndOthers(final AbstractOrderEntry orderEntry, final SessionContext ctx, final int qCount,
			final String productPromoCode, final Double promoCostCentreOnePercentage, final Double promoCostCentreTwoPercentage,
			final Double promoCostCentreThreePercentage)//parameter added for TPR-7408
	{
		orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.DESCRIPTION, orderEntry.getProduct().getDescription());
		orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, productPromoCode);
		orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, Integer.valueOf(qCount));
		//TPR-7408 starts here
		if (null != promoCostCentreOnePercentage)
		{
			orderEntry
					.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCOSTCENTREONE, promoCostCentreOnePercentage);
		}

		if (null != promoCostCentreTwoPercentage)
		{
			orderEntry
					.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCOSTCENTRETWO, promoCostCentreTwoPercentage);
		}

		if (null != promoCostCentreThreePercentage)
		{
			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCOSTCENTRETHREE,
					promoCostCentreThreePercentage);
		}
		//TPR-7408 ends here
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	@Override
	//	public boolean apply(final SessionContext ctx)
	//	{
	//		final AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
	//		final double freebieAmt = 0.01D;
	//
	//		if (log.isDebugEnabled())
	//		{
	//			log.debug("(" + getPK() + ") apply: Applying OrderEntry adjustment action for order [" + order.getPK() + "]");
	//		}
	//		boolean needsCalc = false;
	//
	//		if (null != getAllCutomBogoPromoDataMap(ctx) && getAllCutomBogoPromoDataMap(ctx).size() > 0)
	//		{
	//			for (final Entry<AbstractOrderEntry, Double> entry : getAllCutomBogoPromoDataMap(ctx).entrySet())
	//			{
	//				//final Integer orderEntryNumber = getOrderEntryNumber(ctx);
	//				final AbstractOrderEntry orderEntry = entry.getKey()/* findOrderEntry(order, ctx, orderEntryNumber) */;
	//
	//				if (orderEntry != null)
	//				{
	//					Map<String, AbstractOrderEntry> validProductList = null;
	//					Map<String, Integer> qualifyingCountMap = null;
	//					String productPromoCode = null;
	//					Map<String, List<String>> productAssociatedItemsMap = null;
	//					//List<String> freeItemsForCatBogo = null;
	//					Map<String, Integer> freeItemsForCatBogo = null;
	//
	//					if (ctx.getAttributes() != null)
	//					{
	//						validProductList = ctx.getAttributes().get(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST) != null ? (Map<String, AbstractOrderEntry>) ctx
	//								.getAttributes().get(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST) : null;
	//						qualifyingCountMap = ctx.getAttributes().get(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT) != null ? (Map<String, Integer>) ctx
	//								.getAttributes().get(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT) : null;
	//						productPromoCode = ctx.getAttributes().get(MarketplacecommerceservicesConstants.PROMOCODE) != null ? (String) ctx
	//								.getAttributes().get(MarketplacecommerceservicesConstants.PROMOCODE) : null;
	//						productAssociatedItemsMap = ctx.getAttributes().get(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) != null ? (Map<String, List<String>>) ctx
	//								.getAttributes().get(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) : null;
	//
	//						freeItemsForCatBogo = ctx.getAttributes().get(MarketplacecommerceservicesConstants.FREEITEMFORCATBOGO) != null ? (Map<String, Integer>) ctx
	//								.getAttributes().get(MarketplacecommerceservicesConstants.FREEITEMFORCATBOGO) : null;
	//
	//
	//					}
	//
	//					if (validProductList != null)
	//					{
	//						final Iterator iter = validProductList.entrySet().iterator();
	//						while (iter.hasNext())
	//						{
	//							final Map.Entry mapEntry = (Map.Entry) iter.next();
	//							final AbstractOrderEntry cartEntry = (AbstractOrderEntry) mapEntry.getValue(); //Changed to abstractOrderEntry for TPR-629
	//							final String validProductUSSID = (String) mapEntry.getKey();
	//
	//							double amtTobeDeductedAtlineItemLevel = 0.00D;
	//							int qualifyingCount = 0;
	//							if (null != qualifyingCountMap && !qualifyingCountMap.isEmpty())
	//							{
	//								qualifyingCount = qualifyingCountMap.get(validProductUSSID).intValue();
	//							}
	//							List<String> associatedItemsList = new ArrayList<String>();
	//							if (null != productAssociatedItemsMap && !productAssociatedItemsMap.isEmpty())
	//							{
	//								associatedItemsList = productAssociatedItemsMap.get(validProductUSSID);
	//							}
	//							final double lineItemLevelPrice = cartEntry.getTotalPriceAsPrimitive();
	//							long freeProductCountForBogo = 0L;
	//							final long entryQty = cartEntry.getQuantity().longValue();
	//
	//							if (freeItemsForCatBogo.containsKey(validProductUSSID))
	//							{
	//								if (entryQty == qualifyingCount)
	//								{
	//									freeProductCountForBogo = entryQty;
	//								}
	//								else
	//								{
	//									freeProductCountForBogo = entryQty - qualifyingCount;
	//								}
	//
	//								amtTobeDeductedAtlineItemLevel = (cartEntry.getBasePriceAsPrimitive() - freebieAmt)
	//										* freeProductCountForBogo;
	//
	//								cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.ISBOGOAPPLIED, Boolean.TRUE);
	//								cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.FREECOUNT,
	//										freeItemsForCatBogo.get(validProductUSSID));
	//
	//								if (freeProductCountForBogo > 0)//Added for TISPRO-318
	//								{
	//									cartEntry.setProperty(ctx, "bogoFreeItmCount", String.valueOf(freeProductCountForBogo));
	//								}
	//							}
	//
	//							final double aportionedItemValue = lineItemLevelPrice - amtTobeDeductedAtlineItemLevel;
	//							final double cartLevelDiscount = 0.00D;
	//							final double netAmountAfterAllDisc = aportionedItemValue - cartLevelDiscount;
	//
	//							final List<String> prevAssociatedItemList = cartEntry
	//									.getProperty(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) != null ? (List<String>) cartEntry
	//									.getProperty(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) : null;
	//							if (prevAssociatedItemList != null && !prevAssociatedItemList.isEmpty())
	//							{
	//								final Set associatedItemsSet = new HashSet(prevAssociatedItemList);
	//								associatedItemsSet.addAll(associatedItemsList);
	//
	//								associatedItemsList.clear();
	//								associatedItemsList.addAll(associatedItemsSet);
	//							}
	//
	//							cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.DESCRIPTION, cartEntry.getProduct()
	//									.getDescription());
	//							cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.QUALIFYINGCOUNT,
	//									Integer.valueOf(qualifyingCount));
	//							cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, associatedItemsList);
	//							cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, productPromoCode);
	//							cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALSALEPRICE,
	//									Double.valueOf(lineItemLevelPrice));
	//							cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC,
	//									Double.valueOf(amtTobeDeductedAtlineItemLevel));
	//							cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETSELLINGPRICE,
	//									Double.valueOf(aportionedItemValue));
	//							cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTLEVELDISC,
	//									Double.valueOf(cartLevelDiscount));
	//							cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETAMOUNTAFTERALLDISC,
	//									Double.valueOf(netAmountAfterAllDisc));
	//						}
	//					}
	//
	//					final double orderEntryAdjustment = entry.getValue() == null ? 0 : entry.getValue().doubleValue();
	//
	//					final double unitAdjustment = orderEntryAdjustment / orderEntry.getQuantity(ctx).longValue();
	//
	//					final String code = getGuid(ctx);
	//					final DiscountValue dv = new DiscountValue(code, -1.0D * unitAdjustment, true, order.getCurrency(ctx).getIsoCode(
	//							ctx));
	//					insertFirstOrderEntryDiscountValue(ctx, orderEntry, dv);
	//					if (log.isDebugEnabled())
	//					{
	//						log.debug("(" + getPK() + ") apply: Creating an adjustment of " + entry.getValue() + " to order entry '"
	//								+ orderEntry.getPK() + "'.  Order entry now has " + orderEntry.getDiscountValues(ctx).size()
	//								+ " adjustments");
	//					}
	//					needsCalc = true;
	//				}
	//				else
	//				{
	//					log.error("(" + getPK() + ") apply: Could not find an order entry to adjust with product '"
	//							+ entry.getKey().getProduct() + "' and quantity '" + entry.getKey().getQuantity() + "'");
	//				}
	//			}
	//
	//		}
	//
	//		setMarkedApplied(ctx, true);
	//
	//		return needsCalc;
	//	}


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

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

}