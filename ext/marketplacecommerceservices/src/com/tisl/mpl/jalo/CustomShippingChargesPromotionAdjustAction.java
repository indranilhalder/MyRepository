package com.tisl.mpl.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.promotions.jalo.AbstractPromotion;
import de.hybris.platform.promotions.jalo.PromotionResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * Customized action class for shipping charges promotions
 *
 */
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
		final PromotionResult promotionResult = getPromotionResult(ctx);
		final AbstractOrder order = promotionResult.getOrder(ctx);

		final boolean isShippingCartPromo = (String) ctx.getAttributes().get(MarketplacecommerceservicesConstants.CARTPROMOCODE) != null ? true
				: false;
		if (isShippingCartPromo)
		{
			//			final Map<String, AbstractOrderEntry> validProductUssidMap = ctx.getAttributes().get(
			//					MarketplacecommerceservicesConstants.VALIDPRODUCTLIST) != null ? (Map<String, AbstractOrderEntry>) ctx
			//					.getAttributes().get(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST) : null;
			final List<AbstractOrderEntry> validProductUssidList = getValidProductUssidList();

			for (final AbstractOrderEntry entry : validProductUssidList)
			{
				calculateApportionedDiscount(entry, ctx, promotionResult);
			}
		}
		else
		{
			final Integer orderEntryNumber = getOrderEntryNumber(ctx);
			final AbstractOrderEntry orderEntry = findOrderEntry(order, ctx, orderEntryNumber);
			if (orderEntry != null)
			{
				needsCalc = calculateApportionedDiscount(orderEntry, ctx, promotionResult);
			}
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
		final Integer orderEntryNumber = getOrderEntryNumber(ctx);
		if (orderEntryNumber != null)
		{
			final AbstractOrderEntry orderEntry = findOrderEntry(order, ctx, orderEntryNumber);
			if (ctx.getAttributes() != null)
			{
				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE, Double.valueOf(0.00D));
				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE, Double.valueOf(0.00D));

				calculate = true;
			}
		}
		else
		{
			final List<AbstractOrderEntry> validProductUssidList = getValidProductUssidList();

			if (CollectionUtils.isNotEmpty(validProductUssidList))
			{
				for (final AbstractOrderEntry entry : validProductUssidList)
				{
					entry.setProperty(ctx, MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE, Double.valueOf(0.00D));
					entry.setProperty(ctx, MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE, Double.valueOf(0.00D));

				}
			}
			//			if (order != null)
			//			{
			//				for (final AbstractOrderEntry oe : order.getEntries())
			//				{
			//					oe.setProperty(ctx, MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE, Double.valueOf(0.00D));
			//					oe.setProperty(ctx, MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE, Double.valueOf(0.00D));
			//				}
			//			}

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

	private boolean calculateApportionedDiscount(final AbstractOrderEntry orderEntry, final SessionContext ctx,
			final PromotionResult result)
	{
		boolean needsCalc = false;
		Map<String, Integer> qualifyingCountMap = null;
		String productPromoCode = null;
		String cartPromoCode = null;

		//TPR-7408 starts here
		Double promoCostCentreOnePercentage = null;
		Double promoCostCentreTwoPercentage = null;
		Double promoCostCentreThreePercentage = null;
		//TPR-7408 ends here


		Map<String, List<String>> productAssociatedItemsMap = null;
		Map<String, Map<String, Double>> prodPrevCurrDelChargeMap = null;
		//Map<String, AbstractOrderEntry> validProductList = null;

		if (ctx.getAttributes() != null)
		{
			//				validProductList = ctx.getAttributes().get(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST) != null ? (Map<String, AbstractOrderEntry>) ctx
			//						.getAttributes().get(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST) : null;
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
			final String validProdUSSID = (String) orderEntry.getAttribute(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID);

			List<String> associatedItemsList = new ArrayList<String>();
			if (null != productAssociatedItemsMap && !productAssociatedItemsMap.isEmpty())
			{
				associatedItemsList = productAssociatedItemsMap.get(validProdUSSID);
			}

			final int qualifyingCount = (null != qualifyingCountMap && !qualifyingCountMap.isEmpty()) ? (qualifyingCountMap
					.get(validProdUSSID) != null ? qualifyingCountMap.get(validProdUSSID).intValue() : 0) : 0;

			double prevDelCharge = 0.00D;
			double currDelCharge = 0.00D;

			if (null != prodPrevCurrDelChargeMap && !prodPrevCurrDelChargeMap.isEmpty())
			{
				final Map<String, Double> prevCurrDeliveryChargeMap = prodPrevCurrDelChargeMap.get(validProdUSSID);
				prevDelCharge = prevCurrDeliveryChargeMap.get(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE).doubleValue();
				currDelCharge = prevCurrDeliveryChargeMap.get(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE)
						.doubleValue();
			}

			//Modified for INC144315231
			//final double lineItemLevelPrice = orderEntry.getTotalPriceAsPrimitive();//TODO
			final double lineItemLevelPrice = orderEntry.getBasePriceAsPrimitive() * orderEntry.getQuantityAsPrimitive();

			double totalProdLevelDisc = 0.00D;
			if (null != orderEntry.getProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE)
					&& !((String) orderEntry.getProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE)).isEmpty())
			{
				totalProdLevelDisc = ((Double) orderEntry
						.getProperty(ctx, MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC)).doubleValue();
				productPromoCode = (String) orderEntry.getProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE);
			}

			final double netSellingPrice = lineItemLevelPrice - totalProdLevelDisc;

			final double totalCartLevelDisc = 0.00D;
			//			if (null != orderEntry.getProperty(ctx, MarketplacecommerceservicesConstants.CARTLEVELDISC)
			//					&& !((String) orderEntry.getProperty(ctx, MarketplacecommerceservicesConstants.CARTLEVELDISC)).isEmpty())
			//			{
			//				totalCartLevelDisc = ((Double) orderEntry.getProperty(ctx, MarketplacecommerceservicesConstants.CARTLEVELDISC))
			//						.doubleValue();
			//			}

			final double netAmountAfterAllDisc = netSellingPrice - totalCartLevelDisc;

			final List<String> prevAssociatedItemList = orderEntry.getProperty(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) != null ? (List<String>) orderEntry
					.getProperty(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) : null;

			if (prevAssociatedItemList != null && !prevAssociatedItemList.isEmpty())
			{
				final Set associatedItemsSet = new HashSet(prevAssociatedItemList);
				associatedItemsSet.addAll(associatedItemsList);

				associatedItemsList.clear();
				associatedItemsList.addAll(associatedItemsSet);
			}

			//			if (null != orderEntry.getProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE)
			//					&& !((String) orderEntry.getProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE)).isEmpty())
			//			{
			//				productPromoCode = (String) orderEntry.getProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE);
			//			}

			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.DESCRIPTION, orderEntry.getProduct().getDescription());
			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, Integer.valueOf(qualifyingCount));
			//**********Blocked for TISPRO-670**************
			//cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, associatedItemsList);
			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, productPromoCode);

			//TPR-7408 starts here
			if (StringUtils.isNotEmpty(productPromoCode))
			{

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

			}
			//TPR-7408 ends here

			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTPROMOCODE, cartPromoCode);

			//TPR-7408 starts here
			if (StringUtils.isNotEmpty(cartPromoCode))
			{
				if (null != promoCostCentreOnePercentage)
				{
					orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTPROMOCOSTCENTREONE,
							promoCostCentreOnePercentage);
				}

				if (null != promoCostCentreTwoPercentage)
				{
					orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTPROMOCOSTCENTRETWO,
							promoCostCentreTwoPercentage);
				}

				if (null != promoCostCentreThreePercentage)
				{
					orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTPROMOCOSTCENTRETHREE,
							promoCostCentreThreePercentage);
				}

			}
			//TPR-7408 ends here

			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALSALEPRICE, Double.valueOf(lineItemLevelPrice));
			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC,
					Double.valueOf(totalProdLevelDisc));
			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETSELLINGPRICE, Double.valueOf(netSellingPrice));
			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTLEVELDISC, Double.valueOf(totalCartLevelDisc));
			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETAMOUNTAFTERALLDISC,
					Double.valueOf(netAmountAfterAllDisc));
			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE, Double.valueOf(prevDelCharge));
			orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE, Double.valueOf(currDelCharge));

			if (LOG.isDebugEnabled())
			{
				LOG.debug("(" + getPK() + ") apply: After apply Shipping charge promotion=[" + "Previous Delivery Charge: "
						+ prevDelCharge + ", Current Delivery Charge: " + currDelCharge + "]");
			}

			needsCalc = true;
			setMarkedApplied(ctx, true);
		}
		catch (final JaloInvalidParameterException | JaloSecurityException e)
		{
			undo(ctx);
			LOG.error(e);
		}
		catch (final Exception e)
		{
			undo(ctx);
			LOG.error(e);
		}

		return needsCalc;
	}
	//	protected DefaultPromotionManager getDefaultPromotionsManager()
	//	{
	//		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	//	}


}
