/**
 *
 */
package com.tisl.mpl.promotion.helper;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.jalo.EtailLimitedStockRestriction;
import com.tisl.mpl.jalo.EtailSellerSpecificRestriction;
import com.tisl.mpl.jalo.SellerMaster;
import com.tisl.mpl.marketplacecommerceservices.daos.BulkPromotionCreationDao;
import com.tisl.mpl.marketplacecommerceservices.service.impl.ExtStockLevelPromotionCheckServiceImpl;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.pojo.MplLimitedOfferData;
import com.tisl.mpl.promotion.service.SellerBasedPromotionService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * @author TCS
 * @Description : Promotion Helper Class
 *
 */
@SuppressWarnings(
{ "unused", "PMD" })
public class MplPromotionHelper
{

	private final static Logger LOG = Logger.getLogger(MplPromotionHelper.class.getName());
	@Autowired
	private ModelService modelService;
	@Autowired
	private CartService cartService;




	/**
	 * @Description : Validates if Seller Restriction Exist
	 * @param restrictionList
	 * @return isSellerRestricted
	 */
	public boolean checkRestrictionData(final List<AbstractPromotionRestriction> restrictionList)
	{
		boolean isSellerRestricted = false;

		isSellerRestricted = getDefaultPromotionsManager().isSellerRestrExists(restrictionList);

		return isSellerRestricted;

	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

	/*
	 * protected CartService getCartService() { return Registry.getApplicationContext().getBean("cartService",
	 * CartService.class); }
	 */

	/**
	 * @Description : The Method checks if Multiple Seller added in Seller Restriction
	 * @param restrictionList
	 * @return isMultipleSeller
	 */
	public boolean checkMultipleSeller(final List<AbstractPromotionRestriction> restrictionList)
	{
		boolean isMultipleSeller = false;

		final boolean flag = checkRestrictionData(restrictionList);
		if (flag)
		{
			for (final AbstractPromotionRestriction restriction : restrictionList)
			{
				//Deeply nested if..then statements are hard to read
				//				if (restriction instanceof EtailSellerSpecificRestriction)
				//				{
				//					final EtailSellerSpecificRestriction etailSellerSpecificRestriction = (EtailSellerSpecificRestriction) restriction;
				//					if (null != etailSellerSpecificRestriction.getSellerMasterList()
				//							&& etailSellerSpecificRestriction.getSellerMasterList().size() > 1)
				//					{
				//						isMultipleSeller = true;
				//						break;
				//					}
				//				}
				if (restriction instanceof EtailSellerSpecificRestriction
						&& null != ((EtailSellerSpecificRestriction) restriction).getSellerMasterList()
						&& ((EtailSellerSpecificRestriction) restriction).getSellerMasterList().size() > 1)
				{
					isMultipleSeller = true;
					break;
				}
			}
		}

		return isMultipleSeller;
	}

	/**
	 * @Description : Verify Channel Data Please do not use this
	 * @param listOfChannel
	 * @return flag
	 */
	private boolean checkChannel(@SuppressWarnings("deprecation") final List<EnumerationValue> listOfChannel,
			final AbstractOrder order)
	{
		//final boolean flag = false;
		//final CartModel cartModel = getCartService().getSessionCart();
		//flag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cartModel.getChannel());
		return false;
	}

	/**
	 * @Description : Populating Multiple Seller Data
	 * @param validProductUssidMap
	 * @param productSellerDetails
	 * @param ctx
	 * @return multiSellerValidUSSIDMap
	 */
	public Map<String, Map<String, AbstractOrderEntry>> populateMultiSellerData(
			final Map<String, AbstractOrderEntry> validProductUssidMap, final Map<AbstractOrderEntry, String> productSellerDetails,
			final SessionContext ctx)
	{
		final Map<String, Map<String, AbstractOrderEntry>> multiSellerValidUSSIDMap = new HashMap<String, Map<String, AbstractOrderEntry>>();
		for (final Map.Entry<AbstractOrderEntry, String> sellerDetail : productSellerDetails.entrySet())
		{
			for (final Map.Entry<String, AbstractOrderEntry> validUSSID : validProductUssidMap.entrySet())
			{
				try
				{
					if (sellerDetail.getKey().getAttribute(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID).toString()
							.equalsIgnoreCase(validUSSID.getKey().toString()))
					{
						if (multiSellerValidUSSIDMap.isEmpty())
						{
							final Map<String, AbstractOrderEntry> validProductDetails = new HashMap<String, AbstractOrderEntry>();
							validProductDetails.put(validUSSID.getKey().toString(), sellerDetail.getKey());
							multiSellerValidUSSIDMap.put(sellerDetail.getValue().toString(), validProductDetails);
						}
						else if (!multiSellerValidUSSIDMap.isEmpty())
						{
							boolean dataAdded = false;
							for (final Map.Entry<String, Map<String, AbstractOrderEntry>> multiSellerData : multiSellerValidUSSIDMap
									.entrySet())
							{
								if (multiSellerData.getKey().toString().equalsIgnoreCase(sellerDetail.getValue().toString()))
								{
									dataAdded = true;
									final Map<String, AbstractOrderEntry> validProductDetails = multiSellerData.getValue();
									validProductDetails.put(validUSSID.getKey().toString(), sellerDetail.getKey());
									multiSellerData.setValue(validProductDetails);
									break;
								}

								if (!dataAdded)
								{
									final Map<String, AbstractOrderEntry> validProductDetails = new HashMap<String, AbstractOrderEntry>();
									validProductDetails.put(validUSSID.getKey().toString(), sellerDetail.getKey());
									multiSellerValidUSSIDMap.put(sellerDetail.getValue().toString(), validProductDetails);
								}
							}
						}
					}
				}
				catch (JaloInvalidParameterException | JaloSecurityException exception)
				{
					LOG.error(exception.getMessage());
				}
			}
		}
		return multiSellerValidUSSIDMap;
	}


	/**
	 * @Description:Returns Seller Names/NA for Message Localization
	 * @param ctx
	 * @return sellerData
	 */
	public String sellerDataForMsg(final SessionContext ctx, final List<AbstractPromotionRestriction> restrictionList)
	{
		String sellerData = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			if (null != restrictionList && !restrictionList.isEmpty())
			{
				for (final AbstractPromotionRestriction restriction : restrictionList)
				{
					if (restriction instanceof EtailSellerSpecificRestriction)
					{
						final EtailSellerSpecificRestriction sellerRestriction = (EtailSellerSpecificRestriction) restriction;
						if (null != sellerRestriction.getSellerMasterList() && !sellerRestriction.getSellerMasterList().isEmpty())
						{
							for (final SellerMaster seller : sellerRestriction.getSellerMasterList())
							{
								if (null != seller.getAttribute(ctx, MarketplacecommerceservicesConstants.SELLERMASTER_NAME))
								{
									sellerData = sellerData
											+ seller.getAttribute(ctx, MarketplacecommerceservicesConstants.SELLERMASTER_NAME).toString()
											+ MarketplacecommerceservicesConstants.SPACE;
								}
							}
						}
					}
				}
			}

			if (sellerData.trim().equalsIgnoreCase(MarketplacecommerceservicesConstants.EMPTY))
			{
				sellerData = MarketplacecommerceservicesConstants.NOT_APPLICABLE;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
		}
		return sellerData;
	}

	/**
	 * @Description : Split The valid USSID map based on Seller Details
	 * @param paramSessionContext
	 * @param restrictionList
	 * @param validProductUssidMap
	 * @return productSellerDetails
	 */
	public Map<AbstractOrderEntry, String> populateSellerSpecificData(final SessionContext paramSessionContext,
			final List<AbstractPromotionRestriction> restrictionList, final Map<String, AbstractOrderEntry> validProductUssidMap)
	{
		final Map<AbstractOrderEntry, String> productSellerDetails = new HashMap<AbstractOrderEntry, String>();
		String sellerId = MarketplacecommerceservicesConstants.EMPTYSTRING;

		for (final Map.Entry<String, AbstractOrderEntry> validUSSID : validProductUssidMap.entrySet())
		{
			sellerId = getDefaultPromotionsManager().getSellerID(paramSessionContext, restrictionList, validUSSID.getValue());
			if (StringUtils.isNotEmpty(sellerId))
			{
				productSellerDetails.put(validUSSID.getValue(), sellerId);
			}
		}
		return productSellerDetails;
	}

	/**
	 * @Decsription : Returns Associated Products Data
	 * @param cart
	 * @param validProductUssidMap
	 * @param skuFreebie
	 * @return associatedDataMap
	 */
	public Map<String, List<String>> getAssociatedData(final AbstractOrder cart,
			final Map<String, AbstractOrderEntry> validProductUssidMap, final String skuFreebie)
	{
		final Map<String, List<String>> associatedDataMap = new HashMap<String, List<String>>();
		Map<String, AbstractOrderEntry> associatedDataMapToEntry = new HashMap<String, AbstractOrderEntry>();

		if (null != validProductUssidMap && !validProductUssidMap.isEmpty())
		{
			for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
			{
				associatedDataMapToEntry = returnAssociatedUSSIDDetails(mapEntry.getKey(), validProductUssidMap);
				if (null != associatedDataMapToEntry && !associatedDataMapToEntry.isEmpty())
				{
					final List<String> ussidList = new ArrayList<String>();
					for (final Map.Entry<String, AbstractOrderEntry> entry : associatedDataMapToEntry.entrySet())
					{
						ussidList.add(entry.getKey());
					}

					if (!StringUtils.isEmpty(skuFreebie))
					{
						ussidList.add(skuFreebie);
					}
					associatedDataMap.put(mapEntry.getKey(), ussidList);
				}
			}

			if (!StringUtils.isEmpty(skuFreebie))
			{
				associatedDataMap.put(skuFreebie, new ArrayList<String>(validProductUssidMap.keySet()));
			}
		}
		return associatedDataMap;
	}

	/**
	 * @Description : Method remove a particular USSID details from Available Data
	 * @param ussid
	 * @param validProductUssidMap
	 * @return associatedDataMapToEntry
	 */
	private Map<String, AbstractOrderEntry> returnAssociatedUSSIDDetails(final String ussid,
			final Map<String, AbstractOrderEntry> validProductUssidMap)
	{
		final Map<String, AbstractOrderEntry> associatedDataMapToEntry = new HashMap<String, AbstractOrderEntry>();
		for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
		{
			if (!mapEntry.getKey().toString().equalsIgnoreCase(ussid))
			{
				associatedDataMapToEntry.put(mapEntry.getKey(), mapEntry.getValue());
			}
		}
		return associatedDataMapToEntry;
	}

	/**
	 * @param arg0
	 * @Description : Returns Valid count of Products in Order
	 * @param cart
	 * @param validProductUssidMap
	 * @param excludedProductList
	 * @return count
	 */
	public int returnValidProductCount(final SessionContext arg0, final AbstractOrder cart,
			final Map<String, AbstractOrderEntry> validProductUssidMap,
			@SuppressWarnings("deprecation") final List<Product> excludedProductList)
	{
		int count = 0;
		{
			try
			{
				if (null != cart)
				{
					for (final AbstractOrderEntry entry : cart.getEntries()) // For localization Purpose
					{
						@SuppressWarnings("deprecation")
						final Product orderProduct = entry.getProduct();

						if (!excludedProductList.contains(orderProduct))
						{
							count = count + 1;
						}
					}
				}
			}
			catch (final JaloInvalidParameterException exception)
			{
				LOG.error(exception.getMessage());
			}
			catch (final Exception exception)
			{
				LOG.error(exception.getMessage());
			}
		}
		return count;
	}

	/**
	 * @Description : Returns Valid Count of USSIDs for A and B Discount Promotions
	 * @param validProductAUssidMap
	 * @param validProductBUssidMap
	 * @return count
	 */
	//	public int getEligibleCountForABDiscountPromo(final Map<String, AbstractOrderEntry> validProductAUssidMap,
	//			final Map<String, AbstractOrderEntry> validProductBUssidMap)
	//	{
	//		int count = 0;
	//		int countUSSIDinA = 0;
	//		int countUSSIDinB = 0;
	//
	//		for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductAUssidMap.entrySet())
	//		{
	//			final AbstractOrderEntry entry = mapEntry.getValue();
	//			countUSSIDinA = countUSSIDinA + entry.getQuantity().intValue();
	//		}
	//
	//		for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductBUssidMap.entrySet())
	//		{
	//			final AbstractOrderEntry entry = mapEntry.getValue();
	//			countUSSIDinB = countUSSIDinB + entry.getQuantity().intValue();
	//		}
	//
	//		if (countUSSIDinA == countUSSIDinB)
	//		{
	//			count = countUSSIDinA;
	//		}
	//		else if (countUSSIDinA > countUSSIDinB)
	//		{
	//			count = countUSSIDinB;
	//		}
	//		else if (countUSSIDinB > countUSSIDinA)
	//		{
	//			count = countUSSIDinA;
	//		}
	//		return count;
	//	}

	/**
	 * @Description : Validate entry to check whether its a freebie
	 * @param entry
	 * @return flag
	 */
	public boolean validateEntryForFreebie(final AbstractOrderEntry entry)
	{
		boolean flag = false;
		if (null != entry && null != entry.isGiveAway() && entry.isGiveAway().booleanValue())
		{
			flag = true;
		}

		return flag;
	}

	/**
	 * The Method is used to fetch Promotion Group Details
	 *
	 * @param promoGroup
	 * @return promoGrpModels
	 */
	public PromotionGroupModel fetchPromotionGroupDetails(final String promoGroup)
	{
		PromotionGroupModel promoGrpModel = modelService.create(PromotionGroupModel.class);
		if (StringUtils.isNotEmpty(promoGroup))
		{
			promoGrpModel = getBulkPromotionCreationDao().fetchPromotionGroup(promoGroup);
			if (null != promoGrpModel)
			{
				return promoGrpModel;
			}
			else
			{
				promoGrpModel = getBulkPromotionCreationDao().fetchPromotionGroup(
						MarketplacecommerceservicesConstants.PROMOTION_GROUP_DEFAULT);
			}
		}
		return promoGrpModel;
	}

	/**
	 * The Method is used to calculate the total cart value post discount
	 *
	 * @param order
	 * @return totalPrice
	 */
	public double getTotalPrice(final AbstractOrder order)
	{
		double totalPrice = 0;
		final List<AbstractOrderEntry> entries = (null != order) ? order.getEntries() : new ArrayList<AbstractOrderEntry>();

		//		if (CollectionUtils.isNotEmpty(entries))
		//		{
		for (final AbstractOrderEntry entry : entries)
		{
			totalPrice = totalPrice + entry.getTotalPrice().doubleValue();
		}
		//}

		return totalPrice;
	}


	/**
	 * @Description: Verify Seller Data
	 * @param paramSessionContext
	 * @param restrictionList
	 * @param entry
	 * @return boolean
	 */
	public boolean checkSellerData(final SessionContext paramSessionContext,
			final List<AbstractPromotionRestriction> restrictionList, final AbstractOrderEntry entry)
	{
		boolean flag = false;
		String ussid = MarketplacecommerceservicesConstants.EMPTY;
		List<SellerInformationModel> productSellerData = null;
		try
		{
			if (null != entry && null != entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID))
			{
				ussid = entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID).toString();
				final CatalogVersionModel oModel = getDefaultPromotionsManager().catalogData();
				productSellerData = getSellerBasedPromotionService().fetchSellerInformation(ussid, oModel);
				flag = GenericUtilityMethods.checkSellerData(restrictionList, productSellerData);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
		}
		return flag;
	}



	/**
	 * @param promotion
	 * @return flag
	 */
	public String checkCartPromoPriority(final OrderPromotionModel promotion)
	{
		String promoCode = MarketplacecommerceservicesConstants.EMPTY;
		final List<AbstractPromotionModel> promoDetailsList = getSellerBasedPromotionService().getPromoDetails();
		if (CollectionUtils.isNotEmpty(promoDetailsList))
		{
			for (final AbstractPromotionModel promo : promoDetailsList)
			{
				if (promo instanceof OrderPromotionModel && promotion.getPriority().intValue() == promo.getPriority().intValue()
						&& !StringUtils.equals(promotion.getCode(), promo.getCode()))
				{
					promoCode = promo.getCode();
					break;
				}
			}
		}
		return promoCode;
	}


	/**
	 * The Method checks if The entry is BOGO Free Product
	 *
	 * Added for TISPRO-318
	 *
	 * @param entry
	 * @return flag
	 */
	public boolean checkIfBOGOProduct(final AbstractOrderEntry entry)
	{
		boolean flag = false;
		if ((entry.getTotalPrice().doubleValue() / 0.01) == entry.getQuantity().doubleValue())
		{
			flag = true;
		}

		return flag;
	}


	protected SellerBasedPromotionService getSellerBasedPromotionService()
	{
		return Registry.getApplicationContext().getBean("sellerBasedPromotionService", SellerBasedPromotionService.class);
	}


	protected BulkPromotionCreationDao getBulkPromotionCreationDao()
	{
		return Registry.getApplicationContext().getBean("bulkPromotionCreationDao", BulkPromotionCreationDao.class);
	}



	//commented for PR-15 starts here
	/**
	 * Populate Seller Specific Data for Cart Promotions
	 *
	 * TPR-715
	 *
	 * @param arg0
	 * @param order
	 * @param restrictionList
	 * @return validProductUssidMap
	 */
	/*
	 * public Map<String, AbstractOrderEntry> getCartSellerEligibleProducts(final SessionContext arg0, final
	 * AbstractOrder order, final List<AbstractPromotionRestriction> restrictionList) { //CR Changes final Map<String,
	 * AbstractOrderEntry> validProductUssidMap = new ConcurrentHashMap<String, AbstractOrderEntry>();
	 * 
	 * final List<AbstractOrderEntry> entries = (null != order) ? order.getEntries() : new
	 * ArrayList<AbstractOrderEntry>();
	 * 
	 * // if (CollectionUtils.isNotEmpty(entries)) // { boolean isFreebie = false; boolean isofValidSeller = false;
	 * String selectedUSSID = MarketplacecommerceservicesConstants.EMPTYSPACE;
	 * 
	 * 
	 * for (final AbstractOrderEntry entry : entries) { isFreebie = validateEntryForFreebie(entry); if (!isFreebie) {
	 * isofValidSeller = getDefaultPromotionsManager().checkSellerData(arg0, restrictionList, entry); if
	 * (isofValidSeller) { try { selectedUSSID = (String) entry.getAttribute(arg0,
	 * MarketplacecommerceservicesConstants.SELECTEDUSSID); } catch (JaloInvalidParameterException |
	 * JaloSecurityException e) { LOG.error(e); } validProductUssidMap.put(selectedUSSID, entry); } } } //}
	 * 
	 * return validProductUssidMap; }
	 */
	//commented for PR-15 ends here


	// PR-15 starts here

	/**
	 * Populate Seller Specific Data for Cart Promotions
	 *
	 * TPR-715
	 *
	 * @param arg0
	 * @param order
	 * @param restrictionList
	 * @return validProductUssidMap
	 * @throws JaloSecurityException
	 * @throws JaloInvalidParameterException
	 */
	@SuppressWarnings("deprecation")
	public Map<String, AbstractOrderEntry> getCartSellerEligibleProducts(final SessionContext arg0, final AbstractOrder order,
			final List<AbstractPromotionRestriction> restrictionList, final List<Product> allowedProductList)
			throws JaloInvalidParameterException, JaloSecurityException
	{

		final Map<String, AbstractOrderEntry> validProductUssidMap = new ConcurrentHashMap<String, AbstractOrderEntry>();
		final List<AbstractOrderEntry> entries = (null != order) ? order.getEntries() : new ArrayList<AbstractOrderEntry>();

		boolean isFreebie = false;
		boolean isofValidSeller = false;
		String selectedUSSID = MarketplacecommerceservicesConstants.EMPTYSPACE;


		for (final AbstractOrderEntry entry : entries)
		{

			isFreebie = validateEntryForFreebie(entry);
			if (!isFreebie)
			{

				//final String fulfillmentType = (String) entry.getAttribute(arg0, "fulfillmentType"); // Blocked for TISPRDT-7819

				if (CollectionUtils.isNotEmpty(allowedProductList) && allowedProductList.contains(entry.getProduct()))
				{
					if (CollectionUtils.isEmpty(restrictionList))
					{
						selectedUSSID = entry.getAttribute(arg0, MarketplacecommerceservicesConstants.SELECTEDUSSID).toString();
						validProductUssidMap.put(selectedUSSID, entry);
					}
					else
					{
						isofValidSeller = getDefaultPromotionsManager().checkSellerData(arg0, restrictionList, entry);
						if (isofValidSeller)
						{
							try
							{
								selectedUSSID = (String) entry.getAttribute(arg0, MarketplacecommerceservicesConstants.SELECTEDUSSID);
							}
							catch (JaloInvalidParameterException | JaloSecurityException e)
							{
								LOG.error(e);
							}
							validProductUssidMap.put(selectedUSSID, entry);
						}
					}

				}
			}
		}


		return validProductUssidMap;
	}

	@SuppressWarnings("deprecation")
	public Map<String, AbstractOrderEntry> getCartTshipSellerEligibleProducts(final SessionContext arg0,
			final AbstractOrder order, final List<AbstractPromotionRestriction> restrictionList,
			final List<Product> allowedProductList) throws JaloInvalidParameterException, JaloSecurityException
	{

		final Map<String, AbstractOrderEntry> validProductUssidMap = new ConcurrentHashMap<String, AbstractOrderEntry>();
		final List<AbstractOrderEntry> entries = (null != order) ? order.getEntries() : new ArrayList<AbstractOrderEntry>();

		boolean isFreebie = false;
		boolean isofValidSeller = false;
		String selectedUSSID = MarketplacecommerceservicesConstants.EMPTYSPACE;


		for (final AbstractOrderEntry entry : entries)
		{

			isFreebie = validateEntryForFreebie(entry);
			if (!isFreebie)
			{

				final String fulfillmentType = (String) entry.getAttribute(arg0, "fulfillmentType");
				if (fulfillmentType.equalsIgnoreCase("TSHIP") && CollectionUtils.isNotEmpty(allowedProductList)
						&& allowedProductList.contains(entry.getProduct()))
				{
					if (CollectionUtils.isEmpty(restrictionList))
					{
						selectedUSSID = entry.getAttribute(arg0, MarketplacecommerceservicesConstants.SELECTEDUSSID).toString();
						validProductUssidMap.put(selectedUSSID, entry);
					}
					else
					{
						isofValidSeller = getDefaultPromotionsManager().checkSellerData(arg0, restrictionList, entry);
						if (isofValidSeller)
						{
							try
							{
								selectedUSSID = (String) entry.getAttribute(arg0, MarketplacecommerceservicesConstants.SELECTEDUSSID);
							}
							catch (JaloInvalidParameterException | JaloSecurityException e)
							{
								LOG.error(e);
							}
							validProductUssidMap.put(selectedUSSID, entry);
						}
					}

				}
			}
		}


		return validProductUssidMap;
	}











	// PR-15 ends here



	/**
	 * Populate Exclude Seller Specific Data for Cart Promotions
	 *
	 * TPR-715
	 *
	 * @param arg0
	 * @param order
	 * @param restrictionList
	 * @return validProductUssidMap
	 */
	//	public Map<String, AbstractOrderEntry> getCartSellerInEligibleProducts(final SessionContext arg0, final AbstractOrder order,
	//			final List<AbstractPromotionRestriction> restrictionList)
	//	{
	//		//CR Changes
	//		final Map<String, AbstractOrderEntry> validProductUssidMap = new ConcurrentHashMap<String, AbstractOrderEntry>();
	//
	//		final List<AbstractOrderEntry> entries = (null != order) ? order.getEntries() : new ArrayList<AbstractOrderEntry>();
	//
	//		//		if (null != order && CollectionUtils.isNotEmpty(order.getEntries()))
	//		//		{
	//		boolean isFreebie = false;
	//		boolean isofValidSeller = false;
	//		String selectedUSSID = MarketplacecommerceservicesConstants.EMPTYSPACE;
	//
	//		for (final AbstractOrderEntry entry : entries)
	//		{
	//			isFreebie = validateEntryForFreebie(entry);
	//			if (!isFreebie)
	//			{
	//				isofValidSeller = getDefaultPromotionsManager().isProductExcludedForSeller(arg0, restrictionList, entry);
	//				if (!isofValidSeller)
	//				{
	//					try
	//					{
	//						selectedUSSID = (String) entry.getAttribute(arg0, MarketplacecommerceservicesConstants.SELECTEDUSSID);
	//					}
	//					catch (JaloInvalidParameterException | JaloSecurityException e)
	//					{
	//						LOG.error(e);
	//					}
	//					validProductUssidMap.put(selectedUSSID, entry);
	//				}
	//			}
	//		}
	//		//}
	//
	//		return validProductUssidMap;
	//	}

	/**
	 * Return Product Data based on Seller Data for Cart Shipping Promotion
	 *
	 *
	 * TPR-715
	 *
	 * @param deliveryModeDetailsList
	 * @param validUssidMap
	 * @return validProdQCountMap
	 */
	public Map<String, Integer> getvalidProdQCForOrderShippingPromotion(final List<DeliveryMode> deliveryModeDetailsList,
			final Map<String, AbstractOrderEntry> validUssidMap)
	{
		final Map<String, Integer> validProdQCountMap = new HashMap<String, Integer>();
		final CartModel cartModel = cartService.getSessionCart();

		final List<String> deliveryModeCodeList = new ArrayList<String>();
		for (final DeliveryMode deliveryMode : deliveryModeDetailsList)
		{
			deliveryModeCodeList.add(deliveryMode.getCode());
		}
		try
		{
			if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
			{
				for (final AbstractOrderEntryModel oModel : cartModel.getEntries())
				{
					for (final Map.Entry<String, AbstractOrderEntry> mapentry : validUssidMap.entrySet())
					{
						if (null != mapentry.getValue() && null != mapentry.getValue().getEntryNumber()
								&& mapentry.getValue().getEntryNumber() == oModel.getEntryNumber() && oModel.getMplDeliveryMode() != null)
						{
							final String selectedDeliveryMode = oModel.getMplDeliveryMode().getDeliveryMode().getCode();
							if (deliveryModeCodeList.contains(selectedDeliveryMode))
							{
								validProdQCountMap.put(oModel.getSelectedUSSID(), Integer.valueOf(oModel.getQuantity().intValue()));
							}
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
		return validProdQCountMap;
	}

	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * Calculates Delivery Charge at Entry Level
	 *
	 * @param deliveryVal
	 * @param entry
	 * @return deliveryChargeForEntry
	 */
	public double getDeliveryEntryCharge(final double deliveryVal, final AbstractOrderEntryModel entry)
	{
		double deliveryChargeForEntry = 0;

		//		if (null != entry && (entry.getQuantity().longValue() > 0 && StringUtils.isNotEmpty(entry.getBogoFreeItmCount())))
		//		{
		//			deliveryChargeForEntry = deliveryVal
		//					* (entry.getQuantity().longValue() - Long.valueOf(entry.getBogoFreeItmCount()).longValue());
		//		}
		if (null != entry && (entry.getQuantity().longValue() > 0 && entry.getFreeCount().longValue() > 0))
		{
			deliveryChargeForEntry = deliveryVal * (entry.getQuantity().longValue() - entry.getFreeCount().longValue());
		}
		else
		{
			deliveryChargeForEntry = deliveryVal * (entry.getQuantity().longValue());
		}

		return deliveryChargeForEntry;
	}

	/**
	 *
	 * @param restrictionList
	 * @return boolean
	 */
	public boolean validateForStockRestriction(final List<AbstractPromotionRestriction> restrictionList)
	{
		for (final AbstractPromotionRestriction restriction : restrictionList)
		{
			if (restriction instanceof EtailLimitedStockRestriction)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @param key
	 * @param count
	 * @param validProductList
	 * @return int
	 */
	public int getFreeGiftCount(final String key, final int count, final Map<String, Integer> validProductList)
	{

		LOG.debug("ValidProductList" + validProductList);
		int giftCount = 0;
		int quantity = 0;
		List<SellerInformationModel> productSellerData = null;
		List<SellerInformationModel> validSellerData = null;
		try
		{
			if (null != key && MapUtils.isNotEmpty(validProductList) && count > 0)
			{
				final CatalogVersionModel oModel = getDefaultPromotionsManager().catalogData();
				productSellerData = getSellerBasedPromotionService().fetchSellerInformation(key, oModel);

				if (null != productSellerData && !productSellerData.isEmpty())
				{
					for (final SellerInformationModel sellerData : productSellerData)
					{
						for (final Map.Entry<String, Integer> entry : validProductList.entrySet())
						{
							validSellerData = getSellerBasedPromotionService().fetchSellerInformation(entry.getKey(), oModel);
							for (final SellerInformationModel data : validSellerData)
							{
								if (sellerData.getSellerID().equalsIgnoreCase(data.getSellerID()))
								{
									quantity = quantity + (entry.getValue().intValue());
								}
							}

						}
					}
				}

				//Newly Added Code
				if (quantity > 0)
				{
					giftCount = quantity / count;
				}
			}

		}
		catch (final ModelNotFoundException exception)
		{
			giftCount = 0;
			LOG.error(exception.getMessage());
		}
		catch (final Exception exception)
		{
			giftCount = 0;
			LOG.error(exception.getMessage());
		}

		return giftCount;

	}

	/**
	 * For Limited Stock Restriction
	 *
	 * @param validProductUssidMap
	 * @param restrictionList
	 * @param promoCode
	 * @return isExhausted
	 */
	public boolean isPromoStockExhausted(final Map<String, AbstractOrderEntry> validProductUssidMap,
			final List<AbstractPromotionRestriction> restrictionList, final String promoCode)
	{
		boolean isExhausted = false;
		boolean sellerFlag = false;

		sellerFlag = getDefaultPromotionsManager().isSellerRestrExists(restrictionList);

		if (sellerFlag)
		{
			isExhausted = checkStockData(validProductUssidMap, true, promoCode, restrictionList);
		}
		else
		{
			isExhausted = checkStockData(validProductUssidMap, false, promoCode, restrictionList);
		}

		return isExhausted;
	}

	/**
	 * For Limited Stock Restriction
	 *
	 * @param validProductUssidMap
	 * @param flag
	 * @param promoCode
	 * @param restrictionList
	 * @return isExhausted
	 */
	private boolean checkStockData(final Map<String, AbstractOrderEntry> validProductUssidMap, final boolean flag,
			final String promoCode, final List<AbstractPromotionRestriction> restrictionList)
	{
		boolean isExhausted = true;
		final StringBuilder ussidIds = new StringBuilder();
		final StringBuilder productCodes = new StringBuilder();
		Map<String, Integer> stockCountMap = new HashMap<String, Integer>();
		//boolean stockFlag = true;

		final Map<String, Boolean> dataMap = new HashMap<String, Boolean>();

		final int restrictedCount = getDefaultPromotionsManager().getStockRestrictionVal(restrictionList);

		for (final Map.Entry<String, AbstractOrderEntry> entry : validProductUssidMap.entrySet())
		{
			ussidIds.append(MarketplacecommerceservicesConstants.INVERTED_COMMA + entry.getKey()
					+ MarketplacecommerceservicesConstants.INVERTED_COMMA);
			ussidIds.append(",");
			productCodes.append(MarketplacecommerceservicesConstants.INVERTED_COMMA + entry.getValue().getProduct().getCode()
					+ MarketplacecommerceservicesConstants.INVERTED_COMMA);
			productCodes.append(",");
		}

		if (flag)
		{
			stockCountMap = getStockService().getCumulativeStockMap(ussidIds.toString().substring(0, ussidIds.lastIndexOf(",")),
					promoCode, true);
			for (final Map.Entry<String, AbstractOrderEntry> entry : validProductUssidMap.entrySet())
			{
				if (MapUtils.isNotEmpty(stockCountMap) && null != stockCountMap.get(entry.getKey())
						&& stockCountMap.get(entry.getKey()).intValue() >= restrictedCount)
				{
					dataMap.put(entry.getKey(), Boolean.FALSE);
				}
				else
				{
					dataMap.put(entry.getKey(), Boolean.TRUE);
				}
			}
		}
		else
		{
			stockCountMap = getStockService().getCumulativeStockMap(
					productCodes.toString().substring(0, productCodes.lastIndexOf(",")), promoCode, false);
			for (final Map.Entry<String, AbstractOrderEntry> entry : validProductUssidMap.entrySet())
			{
				if (MapUtils.isNotEmpty(stockCountMap) && null != entry.getValue()
						&& StringUtils.isNotEmpty(entry.getValue().getProduct().getCode())
						&& null != stockCountMap.get(entry.getValue().getProduct().getCode())
						&& stockCountMap.get(entry.getValue().getProduct().getCode()).intValue() >= restrictedCount)
				{
					dataMap.put(entry.getKey(), Boolean.FALSE);
				}
				else
				{
					dataMap.put(entry.getKey(), Boolean.TRUE);
				}
			}
		}



		if (MapUtils.isEmpty(dataMap))
		{
			isExhausted = false;
		}
		else
		{
			for (final Entry<String, Boolean> data : dataMap.entrySet())
			{
				if (data.getValue().booleanValue())
				{
					isExhausted = false;
				}
			}
		}

		return isExhausted;
	}

	protected ExtStockLevelPromotionCheckServiceImpl getStockService()
	{
		return Registry.getApplicationContext().getBean("stockPromoCheckService", ExtStockLevelPromotionCheckServiceImpl.class);
	}

	/**
	 * The Method is used to get Limited Offer Information to Offer Classes
	 *
	 * @param restrictionList
	 * @param promoCode
	 * @param order
	 * @return boolean
	 */
	public MplLimitedOfferData checkCustomerRedeemCount(final List<AbstractPromotionRestriction> restrictionList,
			final String promoCode, final AbstractOrder order, final int promoQualifyingCount)
	{
		final MplLimitedOfferData data = new MplLimitedOfferData();
		try
		{
			int count = 0;
			int usedUpCount = 0;
			int offerCount = 0;
			String orginalUid = MarketplacecommerceservicesConstants.EMPTY;

			//final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) modelService.get(order);

			orginalUid = getorginalUid((AbstractOrderModel) modelService.get(order));

			usedUpCount = getStockService().getCummulativeOrderCount(promoCode, orginalUid);

			count = getStockCustomerRedeemCount(restrictionList);
			if (count == 0)
			{
				data.setActualCustomerCount(0);
				data.setExhausted(isExhausted(usedUpCount, promoQualifyingCount, restrictionList));
			}
			else if (StringUtils.isNotEmpty(orginalUid) && count > 0)
			{

				if (usedUpCount > 0)
				{
					offerCount = usedUpCount / promoQualifyingCount;
					if (offerCount >= count)
					{
						data.setActualCustomerCount(0);
						data.setExhausted(true);
					}
					else
					{
						final int countData = (offerCount - count) < 0 ? ((offerCount - count) * (-1)) : (offerCount - count);
						data.setActualCustomerCount(countData);
						data.setExhausted(false);
					}
				}
				else
				{

					final int usedUpCountNow = getStockService().getCummulativeOrderCount(promoCode,
							MarketplacecommerceservicesConstants.EMPTY);

					data.setActualCustomerCount(count);
					data.setExhausted(isExhausted(usedUpCountNow, promoQualifyingCount, restrictionList));
				}

			}
			else if (StringUtils.isEmpty(orginalUid) && count > 0)
			{

				final int usedUpCountNow = getStockService().getCummulativeOrderCount(promoCode,
						MarketplacecommerceservicesConstants.EMPTY);

				data.setActualCustomerCount(count);
				data.setExhausted(isExhausted(usedUpCountNow, promoQualifyingCount, restrictionList));
			}
		}
		catch (final Exception exception)
		{
			LOG.error("Error during Limited Offer Data Class Generation");
		}
		return data;
	}

	/**
	 * @param usedUpCount
	 * @param promoQualifyingCount
	 * @param restrictionList
	 * @return flag
	 */
	//	private boolean isExhaustedforCus(final int usedUpCount, final int promoQualifyingCount,
	//			final List<AbstractPromotionRestriction> restrictionList)
	//	{
	//
	//		boolean flag = false;
	//		try
	//		{
	//			if (usedUpCount > 0)
	//			{
	//				final int testCount = usedUpCount / promoQualifyingCount;
	//				if (testCount >= getStockCustomerRedeemCount(restrictionList))
	//				{
	//					flag = true;
	//				}
	//			}
	//		}
	//		catch (final Exception exception)
	//		{
	//			LOG.error("Error during data generation|| Limited Offer ");
	//		}
	//
	//		return flag;
	//
	//	}

	/**
	 * The Method Returns Original UID for Limited Offer Evaluation
	 *
	 * @param abstractOrderModel
	 * @return orginalUid
	 */
	private String getorginalUid(final AbstractOrderModel abstractOrderModel)
	{
		String orginalUid = MarketplacecommerceservicesConstants.EMPTY;

		if (null != abstractOrderModel && null != abstractOrderModel.getUser())
		{
			final CustomerModel customer = (CustomerModel) abstractOrderModel.getUser();
			if (null != customer && null != customer.getOriginalUid())
			{
				orginalUid = customer.getOriginalUid();
			}
		}
		return orginalUid;
	}

	/**
	 * @param promoQualifyingCount
	 * @param usedUpCount
	 * @param restrictionList
	 * @return flag
	 */
	private boolean isExhausted(final int usedUpCount, final int promoQualifyingCount,
			final List<AbstractPromotionRestriction> restrictionList)
	{
		boolean flag = false;
		try
		{
			if (usedUpCount > 0)
			{
				final int testCount = usedUpCount / promoQualifyingCount;
				if (testCount >= getDefaultPromotionsManager().getStockRestrictionVal(restrictionList))
				{
					flag = true;
				}
			}
		}
		catch (final Exception exception)
		{
			LOG.error("Error during data generation|| Limited Offer ");
		}

		return flag;
	}

	public int getStockCustomerRedeemCount(final List<AbstractPromotionRestriction> restrictionList)
	{
		int count = 0;
		for (final AbstractPromotionRestriction restriction : restrictionList)
		{
			if (restriction instanceof EtailLimitedStockRestriction)
			{
				final EtailLimitedStockRestriction data = (EtailLimitedStockRestriction) restriction;
				if (StringUtils.isNotEmpty(data.getCusRedeemCount()))
				{
					try
					{
						count = Integer.parseInt(data.getCusRedeemCount()); // For TPR-4925
					}
					catch (final Exception exception)
					{
						count = 0;
					}

				}
				else
				{
					count = 0;
				}

			}
		}
		return count;
	}

	/**
	 * @param validProductList
	 * @param qualifyingCount
	 * @param restrictionList
	 * @return boolean
	 */
	//	public boolean validateCount(final Map<String, Integer> validProductList, final int qualifyingCount,
	//			final List<AbstractPromotionRestriction> restrictionList)
	//	{
	//		int count = 0;
	//		if (MapUtils.isNotEmpty(validProductList) && qualifyingCount > 0 && validateForStockRestriction(restrictionList))
	//		{
	//			for (final Entry<String, Integer> data : validProductList.entrySet())
	//			{
	//				if (null != data.getValue())
	//				{
	//					count += data.getValue().intValue();
	//				}
	//
	//			}
	//
	//			if (!(count % qualifyingCount == 0))
	//			{
	//				return false;
	//			}
	//		}
	//		return true;
	//	}

	/**
	 *
	 * Get Offer Total Order Count for Buy A Above Promotion
	 *
	 * @param restrictionList
	 * @param promoCode
	 * @param order
	 * @return data
	 */
	public MplLimitedOfferData checkCustomerOfferCount(final List<AbstractPromotionRestriction> restrictionList,
			final String promoCode, final AbstractOrder order)
	{
		final MplLimitedOfferData data = new MplLimitedOfferData();
		String orginalUid = MarketplacecommerceservicesConstants.EMPTY;
		String guid = StringUtils.EMPTY;
		guid = getGUID(order);

		final int totalOrderPlaced = getStockService().getTotalOfferOrderCount(promoCode,
				MarketplacecommerceservicesConstants.EMPTY, guid);
		final int totalOfferCount = getDefaultPromotionsManager().getStockRestrictionVal(restrictionList);
		final int maxCustomerOfferCount = getStockCustomerRedeemCount(restrictionList);



		if (totalOrderPlaced < totalOfferCount)
		{
			orginalUid = getOriginalUID(order);
			if (StringUtils.isNotEmpty(orginalUid))
			{
				final int offerReceiveCount = getStockService().getTotalOfferOrderCount(promoCode, orginalUid, guid);//TISHS-143
				if (offerReceiveCount == 0)
				{
					data.setExhausted(false);
				}
				else if (maxCustomerOfferCount > 0 && offerReceiveCount >= maxCustomerOfferCount)
				{
					data.setExhausted(true);
				}
			}
			else
			{
				data.setExhausted(false);
			}
		}
		else
		{
			data.setExhausted(true);
		}
		return data;
	}

	/**
	 * Get Original UID Details
	 *
	 * @param order
	 * @return orginalUid
	 */
	private String getOriginalUID(final AbstractOrder order)
	{
		String orginalUid = MarketplacecommerceservicesConstants.EMPTY;

		final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) modelService.get(order);
		if (null != abstractOrderModel && null != abstractOrderModel.getUser())
		{
			final CustomerModel customer = (CustomerModel) abstractOrderModel.getUser();
			if (null != customer && null != customer.getOriginalUid())
			{
				orginalUid = customer.getOriginalUid();
			}
		}

		return orginalUid;
	}

	/**
	 * Buy A and B Discount Promotion Exhausted Check for Limited Offer Restriction With or Without Customer Restriction
	 *
	 * @param promoCode
	 * @param restrictionList
	 * @param cart
	 * @return flag
	 */
	public boolean isBuyAandBPromoExhausted(final String promoCode, final List<AbstractPromotionRestriction> restrictionList,
			final AbstractOrder cart)
	{
		boolean flag = false;
		int offerCount = 0;
		int usedUpCount = 0;

		try
		{
			usedUpCount = getStockService().getCummulativeOrderCount(promoCode, MarketplacecommerceservicesConstants.EMPTY);
			if (usedUpCount > 0)
			{
				offerCount = usedUpCount / 2;
				if (offerCount >= getDefaultPromotionsManager().getStockRestrictionVal(restrictionList))
				{
					flag = true;
					LOG.debug("Offer" + promoCode + "is exhausted");
				}
			}


			if (!flag)
			{
				final String originalUid = getOriginalUID(cart);
				if (StringUtils.isNotEmpty(originalUid))
				{
					final int cusUsedUpCount = getStockService().getCummulativeOrderCount(promoCode, originalUid);
					if (cusUsedUpCount > 0)
					{
						offerCount = cusUsedUpCount / 2;
						final int perCusCount = getStockCustomerRedeemCount(restrictionList);

						if (perCusCount > 0 && offerCount >= perCusCount)
						{
							flag = true;
							LOG.debug("Offer" + promoCode + "is exhausted");
						}
					}
				}
			}


		}
		catch (final Exception exception)
		{
			LOG.error("Error during Buy A and B Promtion Invalidation Check " + exception);
		}


		return flag;
	}

	/**
	 * Buy A and B Discount Promotion Exhausted Check for Limited Offer Restriction
	 *
	 * With or Without Customer Restriction
	 *
	 * @param restrictionList
	 * @param cart
	 * @param promoCode
	 * @return eligibleCount
	 */
	public int getCustomerRedeemCountForBuyABPromo(final List<AbstractPromotionRestriction> restrictionList,
			final AbstractOrder cart, final String promoCode)
	{
		int eligibleCount = 0;

		final int cusConfiguredCount = getStockCustomerRedeemCount(restrictionList);

		if (cusConfiguredCount > 0)
		{
			final String originalUid = getOriginalUID(cart);

			if (StringUtils.isNotEmpty(originalUid))
			{
				final int cusUsedUpCount = getStockService().getCummulativeOrderCount(promoCode, originalUid);

				if (cusUsedUpCount > 0)
				{
					eligibleCount = cusUsedUpCount / 2;
				}
			}

		}

		return eligibleCount;
	}


	/**
	 * Car-158
	 *
	 * @Description: Category Data corresponding to a Product for Promotion Intercepter
	 * @param productdata
	 * @return productCategoryData
	 */

	public List<CategoryModel> getcategoryData(final ProductModel productdata)
	{
		List<CategoryModel> productCategoryData = null;
		//final List<CategoryModel> superCategoryData = null;
		//final CatalogVersionModel oCatalogVersionModel = catalogData();

		HashSet<CategoryModel> superCategoryData = null;
		List<CategoryModel> superCategoryList = null;

		if (null != productdata)
		{
			productCategoryData = new ArrayList<>(productdata.getSupercategories());

			if (CollectionUtils.isNotEmpty(productCategoryData))
			{
				superCategoryList = new ArrayList<CategoryModel>();
				superCategoryData = (HashSet<CategoryModel>) getAllSupercategories(productCategoryData);
				if (CollectionUtils.isNotEmpty(superCategoryData))
				{
					final List<CategoryModel> dataList = new ArrayList<CategoryModel>(superCategoryData);
					superCategoryList.addAll(dataList);
				}
				superCategoryList.addAll(productCategoryData);
			}
		}
		return superCategoryList;
	}

	public List<CategoryModel> getAllCategories(final List<CategoryModel> categories)
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		try
		{
			for (final CategoryModel category : categories)
			{
				//				final CategoryModel oModel = categoryService.getCategoryForCode(getDefaultPromotionsManager().catalogData(),
				//						category.getCode());Car-158
				if (null != category)
				{
					categoryList.add(category);
					final Collection<CategoryModel> subCategoryList = categoryService.getAllSubcategoriesForCategory(category);
					if (CollectionUtils.isNotEmpty(subCategoryList))
					{
						categoryList.addAll(populateSubCategoryData(subCategoryList));
					}
				}
			}
		}
		catch (final Exception exception)
		{
			LOG.error(exception.getMessage());
		}
		return categoryList;
	}

	public List<CategoryModel> populateSubCategoryData(final Collection<CategoryModel> subCategoryList)
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		for (final CategoryModel category : subCategoryList)
		{
			if (!(category instanceof ClassificationClassModel))
			{
				categoryList.add(category);
			}
		}

		return categoryList;
	}

	/**
	 * car-158 Get All Category Tree Structure
	 *
	 * @param categories
	 * @return result
	 */
	public Collection<CategoryModel> getAllSupercategories(final Collection<CategoryModel> categories)
	{
		Collection<CategoryModel> result = null;
		Collection<CategoryModel> currentLevel = new ArrayList<CategoryModel>();
		for (final CategoryModel categoryModel : categories)
		{
			final List<CategoryModel> superCategories = categoryModel.getSupercategories();
			if (superCategories != null)
			{
				currentLevel.addAll(superCategories);
			}
		}

		while (!CollectionUtils.isEmpty(currentLevel))
		{
			for (final Iterator iterator = currentLevel.iterator(); iterator.hasNext();)
			{
				final CategoryModel categoryModel = (CategoryModel) iterator.next();
				if (result == null)
				{
					result = new HashSet<CategoryModel>();
				}
				if (!result.add(categoryModel))
				{
					// avoid cycles by removing all which are already found
					iterator.remove();
				}
			}

			if (currentLevel.isEmpty())
			{
				break;
			}
			final Collection<CategoryModel> nextLevel = getAllSupercategories(currentLevel);
			currentLevel = nextLevel;
		}

		return result == null ? Collections.EMPTY_LIST : result;
	}

	@Autowired
	private CategoryService categoryService;

	/**
	 * Get GUID
	 *
	 * @param order
	 * @return guid
	 */
	private String getGUID(final AbstractOrder order)
	{
		final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) modelService.get(order);
		return abstractOrderModel.getGuid();
	}

}