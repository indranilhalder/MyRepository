package com.tisl.mpl.jalo;



import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * Customized action class for freebie promotion
 *
 */
public class CustomPromotionOrderAddFreeGiftAction extends GeneratedCustomPromotionOrderAddFreeGiftAction
{
	@SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger(CustomPromotionOrderAddFreeGiftAction.class.getName());

	/**
	 * @Desctription: Creates Item
	 * @param:SessionContext ctx
	 * @param:ComposedType type
	 * @param:ItemAttributeMap allAttributes
	 * @throws:JaloBusinessException
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
	 * @Description: OTB Method.Free Gift Action to Populate the Free Gift in Cart.
	 * @param:SessionContext ctx
	 */
	@Override
	public boolean apply(final SessionContext ctx)
	{
		//System.out.println("Custom free gift action........................");

		//		if (isCachingAllowed(ctx).booleanValue())
		//		{
		//undo(ctx);
		//}

		final double freebieAmt = 0.01D;
		final AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
		String freeUSSIDData = MarketplacecommerceservicesConstants.EMPTY;
		List<String> freeProductUSSIDList = new ArrayList<String>();
		final List<String> tempfreeProductUSSIDList = new ArrayList<String>();

		long freeGiftQuantity = 1L;

		if (ctx.getAttribute(MarketplacecommerceservicesConstants.FREEGIFT_QUANTITY) != null)
		{
			freeGiftQuantity = Long.parseLong((String) ctx.getAttribute(MarketplacecommerceservicesConstants.FREEGIFT_QUANTITY));
		}

		freeGiftQuantity = (null != getFreeGiftQuantity(ctx)) ? getFreeGiftQuantity(ctx).longValue() : 1L;
		//		if (null != getFreeGiftQuantity(ctx))
		//		{
		//			freeGiftQuantity = getFreeGiftQuantity(ctx).longValue();
		//		} //Blocked for TISPT-154

		if (order != null)
		{
			Map<String, Product> freeBieInfoMap = null;
			if (MapUtils.isNotEmpty(getAllFreeGiftInfoMap(ctx)))
			{
				freeBieInfoMap = new ConcurrentHashMap<String, Product>(getAllFreeGiftInfoMap(ctx)); //Added for TISPT-154
			}

			final Product product = getFreeProduct(ctx);
			//For Single Freebie
			if (null != product)
			{
				//final Product product = getFreeProduct(ctx);
				final Unit unit = product.getUnit(ctx);
				//Adding free gift to order
				final AbstractOrderEntry orderEntry = order.addNewEntry(product, freeGiftQuantity, unit, false); //Changed to abstractOrderEntry for TPR-629
				if (log.isDebugEnabled())
				{
					log.debug("(" + getPK() + ") apply: Adding " + 1L + " free gift to Cart with " + order.getAllEntries().size()
							+ " order entries.");
				}

				if (log.isDebugEnabled())
				{
					log.debug("(" + getPK() + ") apply: Adding " + 1L + " free gift.  There are now " + order.getAllEntries().size()
							+ " order entries.");
				}
				orderEntry.setGiveAway(ctx, true);

				if (null != getProductUSSID())
				{//Adding the SKUID of the Product to Cart
					orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID, getProductUSSID());
					freeUSSIDData = getProductUSSID();
				}

				orderEntry.setProperty(ctx, "basePrice", new Double(freebieAmt));
				orderEntry.setProperty(ctx, "totalPrice", new Double(freebieAmt));

				if (log.isDebugEnabled())
				{
					log.debug("(" + getPK() + ") apply: Created a free gift order entry with "
							+ orderEntry.getDiscountValues(ctx).size() + " discount values");
				}

				final PromotionResult pr = getPromotionResult(ctx);
				//			final PromotionOrderEntryConsumed consumed = PromotionsManager.getInstance().createPromotionOrderEntryConsumed(ctx,
				//					getGuid(ctx), orderEntry, 1L);
				//setCachingAllowed(ctx, order);
				final PromotionOrderEntryConsumed consumed = PromotionsManager.getInstance().createPromotionOrderEntryConsumed(ctx,
						getGuid(ctx), orderEntry, freeGiftQuantity);
				consumed.setAdjustedUnitPrice(ctx, 0.01D);
				pr.addConsumedEntry(ctx, consumed);
				setMarkedApplied(ctx, true);
			}

			//For Multiple  Freebie

			else if (MapUtils.isNotEmpty(freeBieInfoMap)) //Added for TISPT-154
			{
				for (final Map.Entry<String, Product> entry : freeBieInfoMap.entrySet()) //Added for TISPT-154
				{

					final Product freeProduct = entry.getValue();
					final Unit unit = freeProduct.getUnit(ctx);
					//Adding free gift to order
					final AbstractOrderEntry orderEntry = order.addNewEntry(freeProduct, freeGiftQuantity, unit, false);

					log.debug("Multiple Freebie USSID" + entry.getKey());
					orderEntry.setGiveAway(ctx, true);
					orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID, entry.getKey());
					orderEntry.setProperty(ctx, "basePrice", new Double(freebieAmt));
					orderEntry.setProperty(ctx, "totalPrice", new Double(freebieAmt));

					final PromotionResult pr = getPromotionResult(ctx);
					//setCachingAllowed(ctx, order);

					final PromotionOrderEntryConsumed consumed = PromotionsManager.getInstance().createPromotionOrderEntryConsumed(
							ctx, getGuid(ctx), orderEntry, freeGiftQuantity);
					consumed.setAdjustedUnitPrice(ctx, 0.01D);
					pr.addConsumedEntry(ctx, consumed);
					setMarkedApplied(ctx, true);
				}

				log.debug("Populating List of USSIDs in List");
				freeProductUSSIDList = populateFreeBieData(freeBieInfoMap);
				if (CollectionUtils.isNotEmpty(freeProductUSSIDList))
				{
					tempfreeProductUSSIDList.addAll(freeProductUSSIDList);
				}
			}




			//The below portion is added for apportioning
			Map<String, AbstractOrderEntry> validProductList = null;
			Map<String, Integer> qualifyingCountMap = null;
			String productPromoCode = null;
			String cartPromoCode = null;

			//TPR-7408 starts here
			Double promoCostCentreOnePercentage = null;
			Double promoCostCentreTwoPercentage = null;
			Double promoCostCentreThreePercentage = null;
			//TPR-7408 ends here


			Map<String, List<String>> productAssociatedItemsMap = null;
			if (ctx.getAttributes() != null)
			{
				validProductList = ctx.getAttributes().get(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST) != null ? (Map<String, AbstractOrderEntry>) ctx
						.getAttributes().get(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST) : null;
				qualifyingCountMap = ctx.getAttributes().get(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT) != null ? (Map<String, Integer>) ctx
						.getAttributes().get(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT) : null;
				productPromoCode = ctx.getAttributes().get(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE) != null ? (String) ctx
						.getAttributes().get(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE) : null;
				cartPromoCode = ctx.getAttributes().get(MarketplacecommerceservicesConstants.CARTPROMOCODE) != null ? (String) ctx
						.getAttributes().get(MarketplacecommerceservicesConstants.CARTPROMOCODE) : null;
				productAssociatedItemsMap = ctx.getAttributes().get(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) != null ? (Map<String, List<String>>) ctx
						.getAttributes().get(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) : null;//TODO null checking

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
					promoCostCentreOnePercentage = (Double) getPromotionResult(ctx).getPromotion().getAttribute(ctx,
							MarketplacecommerceservicesConstants.COSTCENTREONE);
					promoCostCentreTwoPercentage = (Double) getPromotionResult(ctx).getPromotion().getAttribute(ctx,
							MarketplacecommerceservicesConstants.COSTCENTRETWO);

					promoCostCentreThreePercentage = (Double) getPromotionResult(ctx).getPromotion().getAttribute(ctx,
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
			if (null == productPromoCode && null != cartPromoCode)
			{//This is for Cart Level Freebie Promotion
				for (final AbstractOrderEntry entry : order.getEntries())
				{
					final AbstractOrderEntry cartEntry = entry;
					//final Product entryProduct = cartEntry.getProduct();
					final double lineItemLevelPrice = cartEntry.getTotalPriceAsPrimitive();
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.DESCRIPTION, entry.getProduct().getDescription());
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTPROMOCODE, cartPromoCode);

					//TPR-7408 starts here
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTPROMOCOSTCENTREONE,
							promoCostCentreOnePercentage);
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTPROMOCOSTCENTRETWO,
							promoCostCentreTwoPercentage);
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTPROMOCOSTCENTRETHREE,
							promoCostCentreThreePercentage);
					//TPR-7408 ends here


					cartEntry
							.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALSALEPRICE, Double.valueOf(lineItemLevelPrice));

					final double productLevelDisc = cartEntry.getProperty(MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC) != null ? ((Double) cartEntry
							.getProperty(MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC)).doubleValue() : 0.00D;

					//final double productLevelDisc = 0.00D;
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC,
							Double.valueOf(productLevelDisc));

					final double netSellingPrice = lineItemLevelPrice - productLevelDisc;
					final double cartLevelDisc = 0.00D;
					final double netAmtAfterDisc = netSellingPrice - cartLevelDisc;

					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETSELLINGPRICE, Double.valueOf(netSellingPrice));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTLEVELDISC, Double.valueOf(cartLevelDisc));
					cartEntry.setProperty(ctx, MarketplacecommerceservicesConstants.NETAMOUNTAFTERALLDISC,
							Double.valueOf(netAmtAfterDisc));
				}
			}
			else if (StringUtils.isNotEmpty(productPromoCode))
			{//This is for Product Level Freebie Promotions
				String freebieUSSID = null;
				try
				{
					freebieUSSID = freeUSSIDData;
				}
				catch (final JaloInvalidParameterException /* | JaloSecurityException */e)
				{
					log.error(e.getMessage());
				}
				for (final AbstractOrderEntry entry : order.getEntries())
				{
					if (validProductList != null)
					{
						//final CartEntry cartEntry = (CartEntry) entry;
						String selectedUSSID = null;
						try
						{
							selectedUSSID = (String) entry.getAttribute(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID);
						}
						catch (final JaloInvalidParameterException | JaloSecurityException e)
						{
							log.error(e.getMessage());
						}

						/*
						 * if (validProductList.containsKey(selectedUSSID) || (selectedUSSID.equalsIgnoreCase(freebieUSSID) ||
						 * (freeProductUSSIDList.contains(selectedUSSID)) && null != entry.isGiveAway() &&
						 * entry.isGiveAway().booleanValue()))
						 */
						//TISPRDT-141

						if (validProductList.containsKey(selectedUSSID)
								|| (selectedUSSID.equalsIgnoreCase(freebieUSSID) || freeProductUSSIDList.contains(selectedUSSID))
								&& null != entry.isGiveAway() && entry.isGiveAway().booleanValue())
						{
							int qualifyingCount = 0;


							//Qualifying Count Population
							if (selectedUSSID.equalsIgnoreCase(freebieUSSID) || (freeProductUSSIDList.contains(selectedUSSID))) //checking whether cart entry product is freebie
							{
								try
								{
									if (null != entry.getAttribute(ctx, MarketplacecommerceservicesConstants.QUALIFYINGCOUNT)
											&& Integer.parseInt(entry
													.getAttribute(ctx, MarketplacecommerceservicesConstants.QUALIFYINGCOUNT).toString()) > 0)
									{
										//Sonar fixes
										//qualifyingCount = Integer.valueOf(cartEntry.getAttribute(ctx, MarketplacecommerceservicesConstants.QUALIFYINGCOUNT).toString()).intValue();
										qualifyingCount = Integer.parseInt(entry.getAttribute(ctx,
												MarketplacecommerceservicesConstants.QUALIFYINGCOUNT).toString());
									}
									else
									{
										qualifyingCount = (int) freeGiftQuantity;
									}
								}
								catch (JaloInvalidParameterException | JaloSecurityException e)
								{
									log.debug("Error is qualifying count set");
									log.error(e);
								}

							}
							else
							{
								if (null != qualifyingCountMap && !qualifyingCountMap.isEmpty())
								{
									qualifyingCount = qualifyingCountMap.get(selectedUSSID).intValue();
								}
							}



							List<String> associatedItemsList = new ArrayList<String>();
							if (null != productAssociatedItemsMap && !productAssociatedItemsMap.isEmpty())
							{
								associatedItemsList = productAssociatedItemsMap.get(selectedUSSID);
							}
							double lineItemLevelPrice = entry.getTotalPriceAsPrimitive();
							double productLevelDisc = entry.getProperty(MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC) != null ? ((Double) entry
									.getProperty(MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC)).doubleValue() : 0.00D;

							if (selectedUSSID.equalsIgnoreCase(freebieUSSID) || freeProductUSSIDList.contains(selectedUSSID)) // for free product
							{
								lineItemLevelPrice = entry.getBasePriceAsPrimitive() * qualifyingCount;
								productLevelDisc = (entry.getBasePriceAsPrimitive() - freebieAmt) * qualifyingCount;
							}

							final List<String> prevAssociatedItemList = entry
									.getProperty(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) != null ? (List<String>) entry
									.getProperty(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS) : null;
							if (prevAssociatedItemList != null && !prevAssociatedItemList.isEmpty() && null != entry.isGiveAway()
									&& !entry.isGiveAway().booleanValue())
							{
								final Set associatedItemsSet = new HashSet(prevAssociatedItemList);
								associatedItemsSet.addAll(associatedItemsList);

								associatedItemsList.clear();
								associatedItemsList.addAll(associatedItemsSet);
							}

							entry.setProperty(ctx, MarketplacecommerceservicesConstants.DESCRIPTION, entry.getProduct().getDescription());
							entry.setProperty(ctx, MarketplacecommerceservicesConstants.QUALIFYINGCOUNT,
									Integer.valueOf(qualifyingCount));


							//Associated Items and Promotion Code
							//For TISSIT-1770 : Fix
							if ((selectedUSSID.equalsIgnoreCase(freebieUSSID) || (freeProductUSSIDList.contains(selectedUSSID))
									&& null != entry.isGiveAway() && entry.isGiveAway().booleanValue())
									&& (null != isIsBuyAGetPromo(ctx) && isIsBuyAGetPromo(ctx).booleanValue()))
							{
								List<String> associatedData = new ArrayList<String>();
								try
								{
									//For Associated Data for Freebie Promotion
									if (null != entry.getAttribute(ctx, MarketplacecommerceservicesConstants.ASSOCIATEDITEMS))
									{
										associatedData = (List<String>) entry.getAttribute(ctx,
												MarketplacecommerceservicesConstants.ASSOCIATEDITEMS);
										if (CollectionUtils.isEmpty(associatedData))
										{
											entry.setProperty(ctx, MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, associatedItemsList);
										}
									}
									else
									{
										entry.setProperty(ctx, MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, associatedItemsList);
									}



									//For Promo Code
									if (null != entry.getAttribute(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE))
									{
										final String promoData = entry.getAttribute(ctx,
												MarketplacecommerceservicesConstants.PRODUCTPROMOCODE).toString();
										if (StringUtils.isEmpty(promoData))
										{
											log.debug("Setting New Promotion Code");
											entry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, productPromoCode);
										}

									}
									else
									{
										entry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, productPromoCode);
									}

								}
								catch (JaloInvalidParameterException | JaloSecurityException e)
								{
									log.debug("ERROR in Asscocaited data for Freebie");
									log.error(e);
								}
							}
							else
							{
								entry.setProperty(ctx, MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, associatedItemsList);
								entry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, productPromoCode);
							}

							//TPR-7408 starts here
							entry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCOSTCENTREONE,
									promoCostCentreOnePercentage);
							entry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCOSTCENTRETWO,
									promoCostCentreTwoPercentage);
							entry.setProperty(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCOSTCENTRETHREE,
									promoCostCentreThreePercentage);
							//TPR-7408 ends here



							entry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALSALEPRICE,
									Double.valueOf(lineItemLevelPrice));
							entry.setProperty(ctx, MarketplacecommerceservicesConstants.TOTALPRODUCTLEVELDISC,
									Double.valueOf(productLevelDisc));
							final double netSellingPrice = lineItemLevelPrice - productLevelDisc;
							final double cartLevelDisc = 0.00D;
							final double netAmtAfterDisc = netSellingPrice - cartLevelDisc;
							entry.setProperty(ctx, MarketplacecommerceservicesConstants.NETSELLINGPRICE, Double.valueOf(netSellingPrice));
							entry.setProperty(ctx, MarketplacecommerceservicesConstants.CARTLEVELDISC, Double.valueOf(cartLevelDisc));
							entry.setProperty(ctx, MarketplacecommerceservicesConstants.NETAMOUNTAFTERALLDISC,
									Double.valueOf(netAmtAfterDisc));
						}
					}
				}
			}
		}
		return true;
	}

	//	private void setCachingAllowed(final SessionContext ctx, final AbstractOrder order)
	//	{
	//		final Boolean allowed = (order instanceof Cart) ? Boolean.TRUE : Boolean.FALSE;
	//		ctx.setAttribute("de.hybris.platform.promotions.jalo.cachingAllowed", allowed);
	//	}

	//Sonar fix
	/*
	 * private Boolean isCachingAllowed(final SessionContext ctx) { final Boolean allowed = (Boolean)
	 * ctx.getAttribute("de.hybris.platform.promotions.jalo.cachingAllowed"); return (((allowed == null) || (allowed ==
	 * Boolean.FALSE)) ? Boolean.FALSE : Boolean.TRUE); }
	 */
	/**
	 * Populate Free Gift Data
	 *
	 * @param allFreeGiftInfoMap
	 * @return
	 */
	private List<String> populateFreeBieData(final Map<String, Product> allFreeGiftInfoMap)
	{
		final List<String> populateFreeBieData = new ArrayList<>();
		for (final Map.Entry<String, Product> entry : allFreeGiftInfoMap.entrySet())
		{
			populateFreeBieData.add(entry.getKey());
		}
		return populateFreeBieData;
	}

	/**
	 * @Description: OTB Method.Method Called when Products are removed from Cart for the Free Gift was added
	 * @param:SessionContext ctx
	 * @return: boolean
	 */
	@Override
	public boolean undo(final SessionContext ctx)
	{
		try
		{
			final AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
			//setCachingAllowed(ctx, order);

			if (log.isDebugEnabled())
			{
				log.debug("(" + getPK() + ") undo: Undoing add free gift from order with " + order.getAllEntries().size()
						+ " order entries");
			}

			for (final AbstractOrderEntry aoe : order.getEntries())
			{
				Map<String, Product> freeBieInfoMap = null;
				if (MapUtils.isNotEmpty(getAllFreeGiftInfoMap(ctx)))
				{
					freeBieInfoMap = new ConcurrentHashMap<String, Product>(getAllFreeGiftInfoMap(ctx)); //Added for TISPT-154
				}
				//final Map<String, Product> freeBieInfoMap = new ConcurrentHashMap<String, Product>(getAllFreeGiftInfoMap(ctx));

				if (null != getFreeProduct(ctx))
				{
					if ((!(aoe.isGiveAway(ctx).booleanValue())) || (!(aoe.getProduct(ctx).equals(getFreeProduct(ctx))))
							|| (aoe.getQuantity(ctx).longValue() < 1L))
					{
						continue;
					}
					final long remainingQuantityAfterUndo = aoe.getQuantity(ctx).longValue();
					if (remainingQuantityAfterUndo >= 1L)
					{
						if (log.isDebugEnabled())
						{
							log.debug("(" + getPK()
									+ ") undo: Line item has the same or less quantity than the offer.  Removing whole order entry.");
						}
						order.removeEntry(aoe);
					}
					else
					{
						if (log.isDebugEnabled())
						{
							log.debug("("
									+ getPK()
									+ ") undo: Line item has a greater quantity than the offer.  Removing the offer quantity and resetting giveaway flag.");
						}
						aoe.setQuantity(ctx, remainingQuantityAfterUndo);
						aoe.setGiveAway(ctx, false);
						try
						{
							aoe.recalculate();
						}
						catch (final JaloPriceFactoryException jpe)
						{
							log.error("unable to calculate the entry: " + jpe.getMessage());
						}

					}

					final PromotionResult pr = getPromotionResult(ctx);
					if (log.isDebugEnabled())
					{
						log.debug("PromotionResult in UNDO: " + pr);
						log.debug("PromotionResult Consumed Entries in UNDO: " + pr.getConsumedEntries(ctx));
					}

					final List<PromotionOrderEntryConsumed> consumeList = new ArrayList<PromotionOrderEntryConsumed>();
					for (final PromotionOrderEntryConsumed poec : (Collection<PromotionOrderEntryConsumed>) pr.getConsumedEntries(ctx))
					{
						if (log.isDebugEnabled())
						{
							log.debug("PromotionOrderEntryConsumed in UNDO: " + poec);
							log.debug("PromotionOrderEntryConsumed code in UNDO: " + poec.getCode(ctx));
							log.debug("code for: " + poec.getCode(ctx));
							log.debug("");
						}
						if (poec.getCode(ctx) != null && poec.getCode(ctx).equals(getGuid(ctx)))
						{
							consumeList.add(poec);
							//pr.removeConsumedEntry(ctx, poec);
						}

					}

					if (CollectionUtils.isNotEmpty(consumeList))
					{
						final Iterator iter = consumeList.iterator();

						while (iter.hasNext())
						{
							pr.removeConsumedEntry(ctx, (PromotionOrderEntryConsumed) iter.next());
						}
					}

					break;
				}
				//For Multiple Freebie Implementation
				else if (MapUtils.isNotEmpty(freeBieInfoMap))
				{
					final int checkCount = freeBieInfoMap.size();
					int validateCount = 0;
					for (final Map.Entry<String, Product> entry : freeBieInfoMap.entrySet())
					{

						if ((!(aoe.isGiveAway(ctx).booleanValue())) || (!(aoe.getProduct(ctx).equals(entry.getValue())))
								|| (aoe.getQuantity(ctx).longValue() < 1L))
						{
							continue;
						}
						final long remainingQuantityAfterUndo = aoe.getQuantity(ctx).longValue();
						if (remainingQuantityAfterUndo >= 1L)
						{
							validateCount = validateCount + 1;
							order.removeEntry(aoe);
						}
						else
						{
							validateCount = validateCount + 1;
							aoe.setQuantity(ctx, remainingQuantityAfterUndo);
							aoe.setGiveAway(ctx, false);
							try
							{
								aoe.recalculate();
							}
							catch (final JaloPriceFactoryException jpe)
							{
								log.error("unable to calculate the entry: " + jpe.getMessage());
							}
						}
						break;
					}


					//Newly Added Code
					if (checkCount == validateCount)
					{
						final PromotionResult pr = getPromotionResult(ctx);
						final List<PromotionOrderEntryConsumed> consumeList = new ArrayList<PromotionOrderEntryConsumed>();

						if (log.isDebugEnabled())
						{
							log.debug("PromotionResult in UNDO: " + pr);
							log.debug("PromotionResult Consumed Entries in UNDO: " + pr.getConsumedEntries(ctx));
						}

						for (final PromotionOrderEntryConsumed poec : (Collection<PromotionOrderEntryConsumed>) pr
								.getConsumedEntries(ctx))
						{
							if (log.isDebugEnabled())
							{
								log.debug("PromotionOrderEntryConsumed in UNDO: " + poec);
								log.debug("PromotionOrderEntryConsumed code in UNDO: " + poec.getCode(ctx));
								log.debug("code for: " + poec.getCode(ctx));
								log.debug("");
							}
							if (poec.getCode(ctx) != null && poec.getCode(ctx).equals(getGuid(ctx)))
							{
								consumeList.add(poec);
								//pr.removeConsumedEntry(ctx, poec);
							}
						}

						if (CollectionUtils.isNotEmpty(consumeList))
						{
							final Iterator iter = consumeList.iterator();

							while (iter.hasNext())
							{
								pr.removeConsumedEntry(ctx, (PromotionOrderEntryConsumed) iter.next());
							}
						}
					}


				}
			}

			setMarkedApplied(ctx, false);

			if (log.isDebugEnabled())
			{
				log.debug("(" + getPK() + ") undo: Free gift removed from order which now has " + order.getAllEntries().size()
						+ " order entries");
			}
		}
		catch (final Exception exception)
		{
			log.error("Error during undo:" + exception);
		}

		return true;
	}

	/**
	 * @Description:OTB Method
	 * @param:SessionContext ctx
	 * @return: boolean
	 */
	@Override
	public boolean isAppliedToOrder(final SessionContext ctx)
	{
		final AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);

		for (final AbstractOrderEntry aoe : order.getEntries())
		{
			if ((aoe.isGiveAway(ctx).booleanValue()) && (aoe.getProduct(ctx).equals(getFreeProduct(ctx)))
					&& (aoe.getQuantity(ctx).longValue() >= 1L))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @Description:OTB Method
	 * @param:SessionContext ctx
	 * @return: double
	 */
	@Override
	public double getValue(final SessionContext ctx)
	{
		return 0.0D;
	}

	/**
	 * @Description:OTB Method
	 * @param:SessionContext ctx
	 * @param:Map values
	 * @return: void
	 */
	@Override
	protected void deepCloneAttributes(final SessionContext ctx, final Map values)
	{
		super.deepCloneAttributes(ctx, values);
	}

	protected ModelService getModelService()
	{
		return Registry.getApplicationContext().getBean("modelService", ModelService.class);
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

}