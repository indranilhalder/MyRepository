/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.core.hmc;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.Registry;
import de.hybris.platform.hmc.AbstractEditorMenuChip;
import de.hybris.platform.hmc.AbstractExplorerMenuTreeNodeChip;
import de.hybris.platform.hmc.EditorTabChip;
import de.hybris.platform.hmc.extension.HMCExtension;
import de.hybris.platform.hmc.extension.MenuEntrySlotEntry;
import de.hybris.platform.hmc.generic.ClipChip;
import de.hybris.platform.hmc.generic.ToolbarActionChip;
import de.hybris.platform.hmc.util.action.ActionResult;
import de.hybris.platform.hmc.webchips.Chip;
import de.hybris.platform.hmc.webchips.DisplayState;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.OrderPromotion;
import de.hybris.platform.promotions.jalo.ProductPromotion;
import de.hybris.platform.promotions.jalo.PromotionPriceRow;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.voucher.jalo.Voucher;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.BuyABFreePrecentageDiscount;
import com.tisl.mpl.jalo.BuyAPercentageDiscount;
import com.tisl.mpl.jalo.BuyAandBPrecentageDiscount;
import com.tisl.mpl.jalo.BuyAandBgetC;
import com.tisl.mpl.jalo.BuyXItemsofproductAgetproductBforfree;
import com.tisl.mpl.jalo.CustomProductBOGOFPromotion;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.jalo.EtailSellerSpecificRestriction;
import com.tisl.mpl.jalo.ManufacturesRestriction;
import com.tisl.mpl.jalo.SellerMaster;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.marketplacecommerceservices.service.PromoXMLGenerationService;
import com.tisl.mpl.marketplacecommerceservices.service.PromotionSendMailService;
import com.tisl.mpl.promotion.service.SellerBasedPromotionService;
import com.tisl.mpl.promotion.service.UpdatePromotionalPriceService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * Provides necessary meta information about the marketplacecore hmc extension.
 *
 *
 * @version ExtGen v4.1
 */
public class MarketplaceCoreHMCExtension extends HMCExtension
{
	/** Edit the local|project.properties to change logging behavior (properties log4j.*). */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(MarketplaceCoreHMCExtension.class.getName());

	/** Path to the resource bundles. */
	public static final String RESOURCE_PATH = "com.tisl.mpl.core.hmc.locales";
	@Autowired
	private UpdatePromotionalPriceService promotionalPriceService;


	/**
	 * @see HMCExtension#getTreeNodeChips(de.hybris.platform.hmc.webchips.DisplayState,
	 *      de.hybris.platform.hmc.webchips.Chip)
	 */
	@Override
	public List<AbstractExplorerMenuTreeNodeChip> getTreeNodeChips(final DisplayState displayState, final Chip parent)
	{
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see HMCExtension#getMenuEntrySlotEntries(de.hybris.platform.hmc.webchips.DisplayState,
	 *      de.hybris.platform.hmc.webchips.Chip)
	 */
	@Override
	public List<MenuEntrySlotEntry> getMenuEntrySlotEntries(final DisplayState displayState, final Chip parent)
	{
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see HMCExtension#getSectionChips(de.hybris.platform.hmc.webchips.DisplayState,
	 *      de.hybris.platform.hmc.generic.ClipChip)
	 */
	@Override
	public List<ClipChip> getSectionChips(final DisplayState displayState, final ClipChip parent)
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<EditorTabChip> getEditorTabChips(final DisplayState displayState, final AbstractEditorMenuChip parent)
	{
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see HMCExtension#getToolbarActionChips(de.hybris.platform.hmc.webchips.DisplayState,
	 *      de.hybris.platform.hmc.webchips.Chip)
	 */
	@Override
	public List<ToolbarActionChip> getToolbarActionChips(final DisplayState displayState, final Chip parent)
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	public ResourceBundle getLocalizeResourceBundle(final Locale locale)
	{
		return null;
	}

	@Override
	public String getResourcePath()
	{
		return RESOURCE_PATH;
	}

	/**
	 * @Description: For Display of Info Pop up while creating Promotions
	 * @param itemType
	 * @param displayState
	 * @param initialValues
	 * @return ActionResult
	 */
	@Override
	public ActionResult beforeCreate(final ComposedType itemType, final DisplayState displayState, final Map initialValues)
	{
		LOG.debug("Inside beforeCreate in MarketplaceCoreHMCExtension");
		boolean errorFlag = false;

		if (null != itemType
				&& null != itemType.getCode()
				&& (itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.BUYAALONGBGETSHIPPINGFREE)
						|| itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.BUYAANDBPERCENTAGEDISCOUNT)
						|| itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.BUYAANDBGETC)
						|| itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.BUYABFREEPERCENTAGEDISCOUNT)
						|| itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.BOGO)
						|| itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.BUYXOFAGETBFREEANDDISCOUNT)
						|| itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.BUYAPERCENTAGEDISCOUNT)
						|| itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.CARTDISCOUNTPROMO)
						|| itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.CARTFREEBIEPROMO)
						|| itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.ABCASHBACKPROMO)
						|| itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.ACASHBACKPROMO)
						|| itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.CARTCASHBACKPROMO) || itemType.getCode()
						.equalsIgnoreCase(MarketplaceCoreConstants.BUYAABOVEXGETPERCENTAGEORAMOUNTOFF)))
		{
			if (null != initialValues && null != initialValues.get(MarketplaceCoreConstants.PROMOCODE))
			{
				final List<AbstractPromotionModel> promo = getSellerBasedPromotionService().fetchPromotionDetails(
						initialValues.get(MarketplaceCoreConstants.PROMOCODE).toString());
				if (null != promo && !promo.isEmpty() && promo.size() > 0)
				{
					errorFlag = true;
					final String errorMessage = Localization.getLocalizedString("promotion.creation.duplicate.promocode.message")
							+ "\n" + MarketplaceCoreConstants.SPACE + MarketplaceCoreConstants.PROMOTION
							+ promo.get(0).getPromotionType() + MarketplaceCoreConstants.SPACE + MarketplaceCoreConstants.PROMOTION_CODE
							+ promo.get(0).getCode();
					return new ActionResult(ActionResult.FAILED, errorMessage, false);
				}
				else
				{
					errorFlag = false;
				}

			}
			else
			{
				errorFlag = true;
				LOG.debug("*************** Identifier data not provided ***************************");
				return new ActionResult(ActionResult.FAILED, Localization.getLocalizedString("promotion.add.indentifier.message"),
						false);
			}
		}


		if (!errorFlag
				&& null != itemType
				&& null != itemType.getCode()
				&& (itemType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.BUYAPERCENTAGEDISCOUNT)
						|| itemType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.BUYxAGETBFREE)
						|| itemType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.CASHBACKPROMO) || itemType
						.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.CUSTOMBOGO)))
		{
			final ArrayList<Product> productList = (ArrayList) initialValues.get(MarketplacecommerceservicesConstants.PROMO_PRODUCT);
			final ArrayList<Category> categoryList = (ArrayList) initialValues
					.get(MarketplacecommerceservicesConstants.PROMO_CATEGORIES);

			if ((null != productList && productList.size() > 0) && (null != categoryList && categoryList.size() > 0))
			{
				LOG.debug("*************** Data for both product and Category Added ***************************");
				//displayState.addInfoMessage(Localization.getLocalizedString("promotion.creation.info.message"));
				return new ActionResult(ActionResult.FAILED, Localization.getLocalizedString("promotion.creation.info.message"),
						false);
			}
		}

		//For EMI Validation : The Interest data must always be below 100
		if (null != itemType && null != itemType.getCode() && itemType.getCode().equalsIgnoreCase("EMITermRow"))
		{
			final Double interestRate = Double.valueOf(initialValues.get("interestRate").toString());
			if (interestRate.doubleValue() > 100)
			{
				return new ActionResult(ActionResult.FAILED, "Interest Rate must to below 100", false);
			}
		}

		// Blocked for TISUAT-4831
		//Checking if No Seller Restriction is added : Blocked for
		//		if (!errorFlag && null != itemType && null != itemType.getCode()
		//				&& (itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.BUYXOFAGETBFREEANDDISCOUNT)
		//						|| itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.BUYABFREEPERCENTAGEDISCOUNT)
		//						|| itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.CARTFREEBIEPROMO)
		//						|| itemType.getCode().equalsIgnoreCase(MarketplaceCoreConstants.BUYAANDBGETC)))
		//		{
		//			final boolean isSellerRestPresent = validateFreebieData(initialValues);
		//			if (!isSellerRestPresent)
		//			{
		//				LOG.debug("*************** No Seller Restriction added ***************************" + "Method: beforeCreate");
		//				displayState.addInfoMessage(Localization.getLocalizedString("promotion.add.sellerrestriction.message"));
		//			}
		//		}

		return new ActionResult(ActionResult.OK, false);
	}



	/**
	 * @Description: The Method is invoked whenever a HMC modification is done
	 * @param: item
	 * @param: displayState
	 * @param: currentValues
	 * @param: initialValues
	 * @return: ActionResult
	 */
	@Override
	public ActionResult afterSave(final Item item, final DisplayState displayState, final Map currentValues,
			final Map initialValues, final ActionResult actionResult)
	{
		LOG.debug("Inside aftersave >>>>>>>>>");
		boolean errorCheck = false;
		try
		{
			if (null != item && item instanceof BuyAPercentageDiscount)
			{
				LOG.debug("******** Special price check for BuyAPercentageDiscount:" + item.getAttribute("title"));
				errorCheck = poulatePromoPriceData(item);
				if (errorCheck)
				{
					return new ActionResult(ActionResult.FAILED, false);
				}

			}



			if (null != item
					&& (item instanceof CustomProductBOGOFPromotion || item instanceof BuyAandBgetC || item instanceof BuyAandBPrecentageDiscount))
			{
				if (checkIfMultipleSellerRestrAdded(item))
				{
					return new ActionResult(ActionResult.FAILED, Localization.getLocalizedString("promotion.multipleSeller"), false,
							true);
				}

			}
			if (null != item && ((item instanceof ProductPromotion) || (item instanceof OrderPromotion)))
			{
				getPromotionSendMailService().sendMail(item);

				checkForMsgModify(item, currentValues, initialValues);
			}

			//Saving data into VoucherStatusNotificationModel while saving voucher
			if (item instanceof Voucher)
			{
				final VoucherModel voucher = (VoucherModel) getModelService().get((Voucher) item);
				getNotificationService().saveToVoucherStatusNotification(voucher);
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

		return new ActionResult(ActionResult.OK, false);
	}

	/**
	 * Check for Message Modification
	 *
	 * @param item
	 * @param currentValues
	 * @param initialValues
	 */
	private void checkForMsgModify(final Item item, final Map currentValues, final Map initialValues)
	{
		if (MapUtils.isNotEmpty(currentValues) && currentValues.containsKey("messageFired"))
		{
			LOG.debug("***Fired Message Has been modified****");
			if (item instanceof BuyXItemsofproductAgetproductBforfree || item instanceof BuyABFreePrecentageDiscount)
			{
				LOG.debug("***Modifying Promotion Fired Mesage ****");
				getSellerBasedPromotionService().modifyFiredMessage(initialValues.get(MarketplaceCoreConstants.PROMOCODE).toString());
			}
		}

	}

	/**
	 * @Description: To populate data for Promotion Price Modification
	 * @param item
	 * @return: void
	 */
	@SuppressWarnings("boxing")
	private boolean poulatePromoPriceData(final Item item)
	{
		boolean errorFlag = false;
		Double price = 0.0D;
		boolean isPercentage = false;
		boolean isEnabled = false;
		Long quantity = 0L;
		List<Product> productList = null;
		List<Category> categoryList = null;
		Date startDate = null;
		Date endDate = null;
		Integer priority = Integer.valueOf(0);
		final List<String> sellerList = new ArrayList<String>();
		final List<String> brandList = new ArrayList<String>();
		try
		{
			if (null != item)
			{
				//Bug Fix
				final BuyAPercentageDiscount buyAPerDiscountPromotion = (BuyAPercentageDiscount) item;

				for (final AbstractPromotionRestriction res : buyAPerDiscountPromotion.getRestrictions())
				{
					if (res instanceof EtailSellerSpecificRestriction)
					{

						final List<SellerMaster> sellerMasterList = ((EtailSellerSpecificRestriction) res).getSellerMasterList();
						for (final SellerMaster seller : sellerMasterList)
						{
							sellerList.add(seller.getId());
						}
					}
					if (res instanceof ManufacturesRestriction)
					{
						final ManufacturesRestriction brandRestriction = (ManufacturesRestriction) res;
						final List<Category> brandRestrictions = new ArrayList<Category>(brandRestriction.getManufacturers());
						for (final Category code : brandRestrictions)
						{
							brandList.add(code.getCode());
						}
					}
				}
				//Bug Fix ends

				if (null != item.getAttribute("priority"))
				{
					priority = (Integer) item.getAttribute("priority");
				}

				if (null != item.getAttribute("percentageOrAmount"))
				{
					isPercentage = (boolean) item.getAttribute("percentageOrAmount");
				}
				if (null != item.getAttribute("products"))
				{
					productList = new ArrayList<Product>((Collection<Product>) (item.getAttribute("products")));
				}

				if (null != item.getAttribute("categories"))
				{
					categoryList = new ArrayList<Category>((Collection<Category>) item.getAttribute("categories"));
				}

				if (null != item.getAttribute("startDate") && null != item.getAttribute("endDate"))
				{
					startDate = (Date) item.getAttribute("startDate");
					endDate = (Date) item.getAttribute("endDate");
				}

				if (null != item.getAttribute("enabled"))
				{
					isEnabled = (boolean) item.getAttribute("enabled");
				}

				if (null != item.getAttribute("quantity"))
				{
					quantity = (Long) item.getAttribute("quantity");
				}

				price = getDiscountPrice(isPercentage, item);

				if ((null != productList && !productList.isEmpty()) && null != startDate && null != endDate && isEnabled
						&& quantity == 1)
				{
					LOG.debug("******** Special price check for product list:" + productList + " *** percentage discount:"
							+ isPercentage);

					if (isPercentage)
					{
						getUpdatePromotionalPriceService().updatePromotionalPrice(productList, null, price, startDate, endDate, true,
								priority, sellerList, brandList);
					}
					else
					{
						getUpdatePromotionalPriceService().updatePromotionalPrice(productList, null, price, startDate, endDate, false,
								priority, sellerList, brandList);
					}

				}
				else if ((null != categoryList && !categoryList.isEmpty()) && null != startDate && null != endDate && isEnabled
						&& quantity == 1)
				{
					LOG.debug("******** Special price check for product list in category:" + productList + " *** percentage discount:"
							+ isPercentage);
					if (isPercentage)
					{
						getUpdatePromotionalPriceService().updatePromotionalPrice(null, categoryList, price, startDate, endDate, true,
								priority, sellerList, brandList);
					}
					else
					{
						getUpdatePromotionalPriceService().updatePromotionalPrice(null, categoryList, price, startDate, endDate, false,
								priority, sellerList, brandList);
					}
				}
				else if ((null != categoryList && !categoryList.isEmpty()) || ((null != productList && !productList.isEmpty()))
						&& !isEnabled && quantity == 1)
				{
					LOG.debug("******** Special price check disabling promotion, productlist impacted:" + productList
							+ " *** categoryList:" + categoryList);
					getUpdatePromotionalPriceService().disablePromotionalPrice(productList, categoryList, isEnabled, priority,
							brandList, quantity);
				}
				else if ((null != categoryList && !categoryList.isEmpty()) || ((null != productList && !productList.isEmpty()))
						&& quantity > 1) // If Qauntity is increased from 1 to Multiple //Fix for TISPRD-383
				{
					LOG.debug("******** Special price check disabling promotion, productlist impacted:" + productList
							+ " *** categoryList:" + categoryList);
					getUpdatePromotionalPriceService().disablePromotionalPrice(productList, categoryList, isEnabled, priority,
							brandList, quantity);
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			errorFlag = true;
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			errorFlag = true;
			throw e;
		}
		catch (final Exception e)
		{
			errorFlag = true;
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return errorFlag;
	}



	/**
	 * @Description: To populate Discount Price
	 * @param isPercentage
	 * @param initialValues
	 * @return price
	 */
	private Double getDiscountPrice(final boolean isPercentage, final Item item)
	{
		Double price = 0.0D;
		try
		{
			if (!isPercentage)
			{
				price = setDiscountPrice(item, false);
			}
			else if (null != item.getAttribute("discountPrices"))
			{
				price = setDiscountPrice(item, true);
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
		return price;
	}

	/**
	 * @Description: To Set Discount Price
	 * @param initialValues
	 * @return price
	 */
	private Double setDiscountPrice(final Item item, final boolean flag)
	{
		Double price = 0.0D;
		try
		{
			if (flag)
			{
				price = Double.valueOf(item.getAttribute("percentageDiscount").toString());
			}
			else
			{
				final List<PromotionPriceRow> priceRowData = new ArrayList<PromotionPriceRow>(
						(Collection<PromotionPriceRow>) item.getAttribute("discountPrices"));
				for (final PromotionPriceRow priceRow : priceRowData)
				{
					if (null != priceRow.getAttribute("price"))
					{
						price = Double.valueOf(priceRow.getAttribute("price").toString());
					}
				}
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

		return price;
	}

	/**
	 * @Description: To check if seller already exists
	 * @param item
	 * @return boolean
	 */
	private boolean checkIfMultipleSellerRestrAdded(final Item item)
	{
		boolean isSellerExists = false;

		if (item instanceof ProductPromotion)
		{
			final ProductPromotion productBOGOFPromotion = (ProductPromotion) item;
			final List<AbstractPromotionRestriction> promoRestrictionList = new ArrayList<AbstractPromotionRestriction>(
					productBOGOFPromotion.getRestrictions());

			for (final AbstractPromotionRestriction restriction : promoRestrictionList)
			{
				if (restriction instanceof EtailSellerSpecificRestriction)
				{
					final EtailSellerSpecificRestriction etailSellerSpecificRestriction = (EtailSellerSpecificRestriction) restriction;
					if (etailSellerSpecificRestriction.getSellerMasterList().size() > 1)
					{
						isSellerExists = true;
					}
					break;
				}
			}
		}
		return isSellerExists;
	}

	protected UpdatePromotionalPriceService getUpdatePromotionalPriceService()
	{
		return Registry.getApplicationContext().getBean("mplUpdatePromotionPriceService", UpdatePromotionalPriceService.class);
	}

	protected PromoXMLGenerationService getPromoXMLGenerationService()
	{
		return Registry.getApplicationContext().getBean("promoXMLGenerationService", PromoXMLGenerationService.class);
	}

	protected SellerBasedPromotionService getSellerBasedPromotionService()
	{
		return Registry.getApplicationContext().getBean("sellerBasedPromotionService", SellerBasedPromotionService.class);
	}

	protected PromotionSendMailService getPromotionSendMailService()
	{
		return Registry.getApplicationContext().getBean("promotionSendMailService", PromotionSendMailService.class);
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

	protected NotificationService getNotificationService()
	{
		return Registry.getApplicationContext().getBean("notificationService", NotificationService.class);
	}

	protected ModelService getModelService()
	{
		return Registry.getApplicationContext().getBean("modelService", ModelService.class);
	}
}
