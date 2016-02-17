/**
 *
 */
package com.tisl.mpl.promotion.helper;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.jalo.EtailSellerSpecificRestriction;
import com.tisl.mpl.jalo.SellerMaster;
import com.tisl.mpl.marketplacecommerceservices.daos.BulkPromotionCreationDao;
import com.tisl.mpl.model.SellerInformationModel;
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
				promoGrpModel = getBulkPromotionCreationDao()
						.fetchPromotionGroup(MarketplacecommerceservicesConstants.PROMOTION_GROUP_DEFAULT);
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
		if (null != order && null != order.getEntries())
		{
			for (final AbstractOrderEntry entry : order.getEntries())
			{
				totalPrice = totalPrice + entry.getTotalPrice().doubleValue();
			}
		}

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

	protected SellerBasedPromotionService getSellerBasedPromotionService()
	{
		return Registry.getApplicationContext().getBean("sellerBasedPromotionService", SellerBasedPromotionService.class);
	}


	protected BulkPromotionCreationDao getBulkPromotionCreationDao()
	{
		return Registry.getApplicationContext().getBean("bulkPromotionCreationDao", BulkPromotionCreationDao.class);
	}
}
