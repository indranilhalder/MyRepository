/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionPriceRowModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.localization.Localization;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.CampaignPromoSubService;
import com.tisl.mpl.model.BuyAAboveXGetPercentageOrAmountOffModel;
import com.tisl.mpl.model.BuyABFreePrecentageDiscountModel;
import com.tisl.mpl.model.BuyAGetPrecentageDiscountCashbackModel;
import com.tisl.mpl.model.BuyAGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAPercentageDiscountModel;
import com.tisl.mpl.model.BuyAandBGetPrecentageDiscountCashbackModel;
import com.tisl.mpl.model.BuyAandBGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAandBPrecentageDiscountModel;
import com.tisl.mpl.model.BuyAandBgetCModel;
import com.tisl.mpl.model.BuyAboveXGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyXItemsofproductAgetproductBforfreeModel;
import com.tisl.mpl.model.CartOrderThresholdDiscountCashbackModel;
import com.tisl.mpl.model.CartOrderThresholdDiscountPromotionModel;
import com.tisl.mpl.model.CustomProductBOGOFPromotionModel;
import com.tisl.mpl.model.DeliveryModePromotionRestrictionModel;
import com.tisl.mpl.model.EtailExcludeSellerSpecificRestrictionModel;
import com.tisl.mpl.model.EtailSellerSpecificRestrictionModel;
import com.tisl.mpl.model.ExcludeManufacturersRestrictionModel;
import com.tisl.mpl.model.ManufacturersRestrictionModel;
import com.tisl.mpl.model.PaymentModeSpecificPromotionRestrictionModel;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.pojo.CampaignData;



/**
 * @author TCS
 *
 */
public class DefaultCampaignPromoSubService implements CampaignPromoSubService
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DefaultCampaignPromoSubService.class.getName());

	final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	@Autowired
	private ConfigurationService configurationService;

	/**
	 * @Description : Get Promotion Campaign Details
	 *
	 * @param: promotion
	 * @return: CampaignData
	 */
	@Override
	public CampaignData getPromoCampaignDetails(final AbstractPromotionModel promotion)
	{
		CampaignData campaignData = new CampaignData();

		if (promotion instanceof OrderPromotionModel)
		{
			campaignData = getOrderCampaignData(promotion);
		}
		else if (promotion instanceof ProductPromotionModel)
		{
			campaignData = getProductCampaignData(promotion);
		}

		return campaignData;
	}

	/**
	 * Campaign Data for Product Promotions
	 *
	 * @param promotion
	 * @return CampaignData
	 */
	private CampaignData getProductCampaignData(final AbstractPromotionModel promotion)
	{
		CampaignData campaignData = new CampaignData();
		campaignData = setDefaultData(campaignData);

		if (promotion instanceof BuyAPercentageDiscountModel)
		{
			campaignData = getBuyAPercentageDisData(promotion, campaignData);
		}
		else if (promotion instanceof BuyAandBPrecentageDiscountModel)
		{
			campaignData = getBuyABDiscountData(promotion, campaignData);
		}
		else if (promotion instanceof BuyAandBgetCModel)
		{
			campaignData = getBuyABGetCData(promotion, campaignData);
		}
		else if (promotion instanceof BuyXItemsofproductAgetproductBforfreeModel)
		{
			campaignData = getBuyABfreeData(promotion, campaignData);
		}
		else if (promotion instanceof BuyABFreePrecentageDiscountModel)
		{
			campaignData = getBuyABFreePlusDis(promotion, campaignData);
		}
		else if (promotion instanceof BuyAAboveXGetPercentageOrAmountOffModel)
		{
			campaignData = getProductThreshData(promotion, campaignData);
		}
		else if (promotion instanceof BuyAGetPrecentageDiscountCashbackModel)
		{
			campaignData = getBuyACashBackData(promotion, campaignData);
		}
		else if (promotion instanceof BuyAandBGetPrecentageDiscountCashbackModel)
		{
			campaignData = getBuyABCashBackData(promotion, campaignData);
		}
		else if (promotion instanceof CustomProductBOGOFPromotionModel)
		{
			campaignData = getBOGOData(promotion, campaignData);
		}
		else if (promotion instanceof BuyAGetPromotionOnShippingChargesModel)
		{
			campaignData = buyAShipChrgsPromo(promotion, campaignData);
		}
		else if (promotion instanceof BuyAandBGetPromotionOnShippingChargesModel)
		{
			campaignData = buyABShipChrgsPromo(promotion, campaignData);
		}


		return campaignData;
	}




	/**
	 * Campaign Data for Cart Promotions
	 *
	 * @param promotion
	 * @return CampaignData
	 */
	private CampaignData getOrderCampaignData(final AbstractPromotionModel promotion)
	{
		CampaignData campaignData = new CampaignData();
		campaignData = setDefaultData(campaignData);

		if (promotion instanceof CartOrderThresholdDiscountPromotionModel)
		{
			campaignData = getCartDiscountPromoData(promotion, campaignData);
		}
		else if (promotion instanceof CartOrderThresholdDiscountCashbackModel)
		{
			campaignData = getCartDiscountCashBackData(promotion, campaignData);
		}
		else if (promotion instanceof BuyAboveXGetPromotionOnShippingChargesModel)
		{
			campaignData = getCartShipPromoData(promotion, campaignData);
		}

		return campaignData;
	}



	/**
	 * Campaign Data for : BuyAandBGetPromotionOnShippingChargesModel
	 *
	 * @param promotion
	 * @param campaignData
	 * @return data
	 */
	private CampaignData buyABShipChrgsPromo(final AbstractPromotionModel promotion, final CampaignData campaignData)
	{
		CampaignData data = campaignData;
		data = populateDefaultCampData(promotion, campaignData);
		final BuyAandBGetPromotionOnShippingChargesModel oModel = (BuyAandBGetPromotionOnShippingChargesModel) promotion;


		if (CollectionUtils.isNotEmpty(oModel.getProducts()))
		{
			data.setProducts(populateProducts(oModel.getProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getCategories()))
		{
			data.setCategories(populateCategories(oModel.getCategories()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getExcludedProducts()))
		{
			data.setExcludedProducts(populateProducts(oModel.getExcludedProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getSecondProducts()))
		{
			data.setSecProducts(populateProducts(oModel.getSecondProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getSecondCategories()))
		{
			data.setSecCategories(populateCategories(oModel.getSecondCategories()));
		}

		if (null != oModel.getDiscTypesOnShippingCharges() && null != oModel.getDiscTypesOnShippingCharges())
		{
			data.setDiscountType(oModel.getDiscTypesOnShippingCharges().getCode());
		}

		if (null != oModel.getPercentageDiscount() && oModel.getPercentageDiscount().doubleValue() > 0)
		{
			data.setPercentage(oModel.getPercentageDiscount().toString());
		}

		if (CollectionUtils.isNotEmpty(oModel.getDiscountPrices()))
		{
			data.setDiscountPrices(getDiscount(oModel.getDiscountPrices()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageFired()))
		{
			data.setFiredMessage(replaceCommaWithSpace(oModel.getMessageFired()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageCouldHaveFired()))
		{
			data.setCouldFireMessage(replaceCommaWithSpace(oModel.getMessageCouldHaveFired()));
		}

		if (null != oModel.getTShip() && oModel.getTShip().booleanValue())
		{
			data.setIsTship(MarketplacecommerceservicesConstants.TRUE);
		}

		if (null != oModel.getSShip() && oModel.getSShip().booleanValue())
		{
			data.setIsSShip(MarketplacecommerceservicesConstants.TRUE);
		}

		return data;
	}


	/**
	 * Campaign Data for : BuyAGetPromotionOnShippingChargesModel
	 *
	 * @param promotion
	 * @param campaignData
	 * @return data
	 */
	private CampaignData buyAShipChrgsPromo(final AbstractPromotionModel promotion, final CampaignData campaignData)
	{
		CampaignData data = campaignData;
		data = populateDefaultCampData(promotion, campaignData);
		final BuyAGetPromotionOnShippingChargesModel oModel = (BuyAGetPromotionOnShippingChargesModel) promotion;

		if (CollectionUtils.isNotEmpty(oModel.getProducts()))
		{
			data.setProducts(populateProducts(oModel.getProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getCategories()))
		{
			data.setCategories(populateCategories(oModel.getCategories()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getExcludedProducts()))
		{
			data.setExcludedProducts(populateProducts(oModel.getExcludedProducts()));
		}

		if (null != oModel.getQuantity() && oModel.getQuantity().longValue() > 0)
		{
			data.setQuantity(oModel.getQuantity().toString());
		}

		if (null != oModel.getDiscTypesOnShippingCharges() && null != oModel.getDiscTypesOnShippingCharges())
		{
			data.setDiscountType(oModel.getDiscTypesOnShippingCharges().getCode());
		}

		if (null != oModel.getPercentageDiscount() && oModel.getPercentageDiscount().doubleValue() > 0)
		{
			data.setPercentage(oModel.getPercentageDiscount().toString());
		}

		if (CollectionUtils.isNotEmpty(oModel.getDiscountPrices()))
		{
			data.setDiscountPrices(getDiscount(oModel.getDiscountPrices()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageFired()))
		{
			data.setFiredMessage(replaceCommaWithSpace(oModel.getMessageFired()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageCouldHaveFired()))
		{
			data.setCouldFireMessage(replaceCommaWithSpace(oModel.getMessageCouldHaveFired()));
		}

		if (null != oModel.getTShip() && oModel.getTShip().booleanValue())
		{
			data.setIsTship(MarketplacecommerceservicesConstants.TRUE);
		}

		if (null != oModel.getSShip() && oModel.getSShip().booleanValue())
		{
			data.setIsSShip(MarketplacecommerceservicesConstants.TRUE);
		}


		return data;
	}


	/**
	 * For Campaign Data For : CustomProductBOGOFPromotionModel
	 *
	 * @param promotion
	 * @param campaignData
	 * @return data
	 */
	private CampaignData getBOGOData(final AbstractPromotionModel promotion, final CampaignData campaignData)
	{
		final CustomProductBOGOFPromotionModel oModel = (CustomProductBOGOFPromotionModel) promotion;
		CampaignData data = campaignData;
		data = populateDefaultCampData(promotion, campaignData);

		if (CollectionUtils.isNotEmpty(oModel.getProducts()))
		{
			data.setProducts(populateProducts(oModel.getProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getCategories()))
		{
			data.setCategories(populateCategories(oModel.getCategories()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getExcludedProducts()))
		{
			data.setExcludedProducts(populateProducts(oModel.getExcludedProducts()));
		}

		if (null != oModel.getMinimumAmount() && oModel.getMinimumAmount().doubleValue() > 0)
		{
			data.setCatMinAmnt(oModel.getMinimumAmount().toString());
		}

		if (StringUtils.isNotEmpty(oModel.getMessageFired()))
		{
			data.setFiredMessage(replaceCommaWithSpace(oModel.getMessageFired()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageCouldHaveFired()))
		{
			data.setCouldFireMessage(replaceCommaWithSpace(oModel.getMessageCouldHaveFired()));
		}

		if (null != oModel.getQualifyingCount() && oModel.getQualifyingCount().longValue() > 0)
		{
			data.setQuantity(oModel.getQualifyingCount().toString());
		}

		if (null != oModel.getFreeCount() && oModel.getFreeCount().longValue() > 0)
		{
			data.setFreecount(oModel.getFreeCount().toString());
		}


		return data;
	}


	/**
	 * Campaign Data For : BuyAandBGetPrecentageDiscountCashbackModel
	 *
	 * @param promotion
	 * @param campaignData
	 * @return data
	 */
	private CampaignData getBuyABCashBackData(final AbstractPromotionModel promotion, final CampaignData campaignData)
	{
		CampaignData data = campaignData;
		data = populateDefaultCampData(promotion, campaignData);
		final BuyAandBGetPrecentageDiscountCashbackModel oModel = (BuyAandBGetPrecentageDiscountCashbackModel) promotion;

		if (CollectionUtils.isNotEmpty(oModel.getProducts()))
		{
			data.setProducts(populateProducts(oModel.getProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getCategories()))
		{
			data.setCategories(populateCategories(oModel.getCategories()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getSecondProducts()))
		{
			data.setSecProducts(populateProducts(oModel.getSecondProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getSecondCategories()))
		{
			data.setSecCategories(populateCategories(oModel.getSecondCategories()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getExcludedProducts()))
		{
			data.setExcludedProducts(populateProducts(oModel.getExcludedProducts()));
		}

		if (null != oModel.getMinimumAmount() && oModel.getMinimumAmount().doubleValue() > 0)
		{
			data.setCatMinAmnt(oModel.getMinimumAmount().toString());
		}

		if (null != oModel.getPercentageOrAmount())
		{
			data.setIsPercentage(oModel.getPercentageOrAmount().toString());
		}

		if (null != oModel.getPercentageDiscount() && oModel.getPercentageDiscount().doubleValue() > 0)
		{
			data.setPercentage(oModel.getPercentageDiscount().toString());
		}

		if (CollectionUtils.isNotEmpty(oModel.getDiscountPrices()))
		{
			data.setDiscountPrices(getDiscount(oModel.getDiscountPrices()));
		}

		if (null != oModel.getMaxDiscount() && oModel.getMaxDiscount().doubleValue() > 0)
		{
			data.setMaxDiscount(oModel.getMaxDiscount().toString());
		}

		if (StringUtils.isNotEmpty(oModel.getMessageFired()))
		{
			data.setFiredMessage(replaceCommaWithSpace(oModel.getMessageFired()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageCouldHaveFired()))
		{
			data.setCouldFireMessage(replaceCommaWithSpace(oModel.getMessageCouldHaveFired()));
		}

		return data;
	}


	/**
	 * For Campaign Data : BuyAGetPrecentageDiscountCashbackModel
	 *
	 *
	 * @param promotion
	 * @param campaignData
	 * @return data
	 */
	private CampaignData getBuyACashBackData(final AbstractPromotionModel promotion, final CampaignData campaignData)
	{
		CampaignData data = campaignData;
		data = populateDefaultCampData(promotion, campaignData);
		final BuyAGetPrecentageDiscountCashbackModel oModel = (BuyAGetPrecentageDiscountCashbackModel) promotion;

		if (CollectionUtils.isNotEmpty(oModel.getProducts()))
		{
			data.setProducts(populateProducts(oModel.getProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getCategories()))
		{
			data.setCategories(populateCategories(oModel.getCategories()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getExcludedProducts()))
		{
			data.setExcludedProducts(populateProducts(oModel.getExcludedProducts()));
		}

		if (null != oModel.getQuantity() && oModel.getQuantity().longValue() > 0)
		{
			data.setQuantity(oModel.getQuantity().toString());
		}

		if (null != oModel.getMinimumAmount() && oModel.getMinimumAmount().doubleValue() > 0)
		{
			data.setCatMinAmnt(oModel.getMinimumAmount().toString());
		}

		if (null != oModel.getPercentageOrAmount())
		{
			data.setIsPercentage(oModel.getPercentageOrAmount().toString());
		}

		if (null != oModel.getPercentageDiscount() && oModel.getPercentageDiscount().doubleValue() > 0)
		{
			data.setPercentage(oModel.getPercentageDiscount().toString());
		}

		if (CollectionUtils.isNotEmpty(oModel.getDiscountPrices()))
		{
			data.setDiscountPrices(getDiscount(oModel.getDiscountPrices()));
		}

		if (null != oModel.getMaxDiscount() && oModel.getMaxDiscount().doubleValue() > 0)
		{
			data.setMaxDiscount(oModel.getMaxDiscount().toString());
		}

		if (StringUtils.isNotEmpty(oModel.getMessageFired()))
		{
			data.setFiredMessage(replaceCommaWithSpace(oModel.getMessageFired()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageCouldHaveFired()))
		{
			data.setCouldFireMessage(replaceCommaWithSpace(oModel.getMessageCouldHaveFired()));
		}

		return data;
	}


	/**
	 * Campaign Data For : BuyAAboveXGetPercentageOrAmountOffModel
	 *
	 * @param promotion
	 * @param campaignData
	 * @return data
	 */
	private CampaignData getProductThreshData(final AbstractPromotionModel promotion, final CampaignData campaignData)
	{
		CampaignData data = campaignData;
		final BuyAAboveXGetPercentageOrAmountOffModel oModel = (BuyAAboveXGetPercentageOrAmountOffModel) promotion;
		data = populateDefaultCampData(promotion, campaignData);

		if (CollectionUtils.isNotEmpty(oModel.getProducts()))
		{
			data.setProducts(populateProducts(oModel.getProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getCategories()))
		{
			data.setCategories(populateCategories(oModel.getCategories()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getExcludedProducts()))
		{
			data.setExcludedProducts(populateProducts(oModel.getExcludedProducts()));
		}

		if (null != oModel.getMinimumAmount() && oModel.getMinimumAmount().doubleValue() > 0)
		{
			data.setCatMinAmnt(oModel.getMinimumAmount().toString());
		}

		if (null != oModel.getPercentageOrAmount())
		{
			data.setIsPercentage(oModel.getPercentageOrAmount().toString());
		}

		if (null != oModel.getPercentageDiscount() && oModel.getPercentageDiscount().doubleValue() > 0)
		{
			data.setPercentage(oModel.getPercentageDiscount().toString());
		}

		if (CollectionUtils.isNotEmpty(oModel.getDiscountPrices()))
		{
			data.setDiscountPrices(getDiscount(oModel.getDiscountPrices()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageFired()))
		{
			data.setFiredMessage(replaceCommaWithSpace(oModel.getMessageFired()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageCouldHaveFired()))
		{
			data.setCouldFireMessage(replaceCommaWithSpace(oModel.getMessageCouldHaveFired()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getThresholdTotals()))
		{
			data.setThreshTotals(getDiscount(oModel.getThresholdTotals()));
		}


		return data;
	}


	/**
	 * Campaign Data for : BuyABFreePrecentageDiscountModel
	 *
	 * @param promotion
	 * @param campaignData
	 * @return data
	 */
	private CampaignData getBuyABFreePlusDis(final AbstractPromotionModel promotion, final CampaignData campaignData)
	{
		CampaignData data = campaignData;
		final BuyABFreePrecentageDiscountModel oModel = (BuyABFreePrecentageDiscountModel) promotion;
		data = populateDefaultCampData(promotion, campaignData);

		if (CollectionUtils.isNotEmpty(oModel.getProducts()))
		{
			data.setProducts(populateProducts(oModel.getProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getCategories()))
		{
			data.setCategories(populateCategories(oModel.getCategories()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getGiftProducts()))
		{
			data.setProducts(populateProducts(oModel.getGiftProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getExcludedProducts()))
		{
			data.setExcludedProducts(populateProducts(oModel.getExcludedProducts()));
		}

		if (null != oModel.getMinimumAmount() && oModel.getMinimumAmount().doubleValue() > 0)
		{
			data.setCatMinAmnt(oModel.getMinimumAmount().toString());
		}

		if (StringUtils.isNotEmpty(oModel.getMessageFired()))
		{
			data.setFiredMessage(replaceCommaWithSpace(oModel.getMessageFired()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageCouldHaveFired()))
		{
			data.setCouldFireMessage(replaceCommaWithSpace(oModel.getMessageCouldHaveFired()));
		}

		if (null != oModel.getQuantity() && oModel.getQuantity().longValue() > 0)
		{
			data.setQuantity(oModel.getQuantity().toString());
		}

		return data;
	}



	/**
	 * Campaign Data for : BuyXItemsofproductAgetproductBforfreeModel
	 *
	 * @param promotion
	 * @param campaignData
	 * @return data
	 */
	private CampaignData getBuyABfreeData(final AbstractPromotionModel promotion, final CampaignData campaignData)
	{
		CampaignData data = campaignData;
		final BuyXItemsofproductAgetproductBforfreeModel oModel = (BuyXItemsofproductAgetproductBforfreeModel) promotion;
		data = populateDefaultCampData(promotion, campaignData);

		if (CollectionUtils.isNotEmpty(oModel.getProducts()))
		{
			data.setProducts(populateProducts(oModel.getProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getCategories()))
		{
			data.setCategories(populateCategories(oModel.getCategories()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getGiftProducts()))
		{
			data.setProducts(populateProducts(oModel.getGiftProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getExcludedProducts()))
		{
			data.setExcludedProducts(populateProducts(oModel.getExcludedProducts()));
		}

		if (null != oModel.getMinimumAmount() && oModel.getMinimumAmount().doubleValue() > 0)
		{
			data.setCatMinAmnt(oModel.getMinimumAmount().toString());
		}

		if (StringUtils.isNotEmpty(oModel.getMessageFired()))
		{
			data.setFiredMessage(replaceCommaWithSpace(oModel.getMessageFired()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageCouldHaveFired()))
		{
			data.setCouldFireMessage(replaceCommaWithSpace(oModel.getMessageCouldHaveFired()));
		}

		if (null != oModel.getQualifyingCount() && oModel.getQualifyingCount().longValue() > 0)
		{
			data.setQuantity(oModel.getQualifyingCount().toString());
		}

		return data;
	}


	/**
	 * Campaign Data for : BuyAandBgetCModel
	 *
	 * @param promotion
	 * @param campaignData
	 * @return data
	 */
	private CampaignData getBuyABGetCData(final AbstractPromotionModel promotion, final CampaignData campaignData)
	{
		CampaignData data = campaignData;
		final BuyAandBgetCModel oModel = (BuyAandBgetCModel) promotion;
		data = populateDefaultCampData(promotion, campaignData);

		if (CollectionUtils.isNotEmpty(oModel.getProducts()))
		{
			data.setProducts(populateProducts(oModel.getProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getCategories()))
		{
			data.setCategories(populateCategories(oModel.getCategories()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getSecondProducts()))
		{
			data.setSecProducts(populateProducts(oModel.getSecondProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getSecondCategories()))
		{
			data.setSecCategories(populateCategories(oModel.getSecondCategories()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getGiftProducts()))
		{
			data.setProducts(populateProducts(oModel.getGiftProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getExcludedProducts()))
		{
			data.setExcludedProducts(populateProducts(oModel.getExcludedProducts()));
		}

		if (null != oModel.getMinimumAmount() && oModel.getMinimumAmount().doubleValue() > 0)
		{
			data.setCatMinAmnt(oModel.getMinimumAmount().toString());
		}

		if (StringUtils.isNotEmpty(oModel.getMessageFired()))
		{
			data.setFiredMessage(replaceCommaWithSpace(oModel.getMessageFired()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageCouldHaveFired()))
		{
			data.setCouldFireMessage(replaceCommaWithSpace(oModel.getMessageCouldHaveFired()));
		}


		return data;
	}



	/**
	 * Campaign Data For : BuyAandBPrecentageDiscountModel
	 *
	 * @param promotion
	 * @param campaignData
	 * @return data
	 */
	private CampaignData getBuyABDiscountData(final AbstractPromotionModel promotion, final CampaignData campaignData)
	{
		CampaignData data = campaignData;
		final BuyAandBPrecentageDiscountModel oModel = (BuyAandBPrecentageDiscountModel) promotion;
		data = populateDefaultCampData(promotion, campaignData);


		if (CollectionUtils.isNotEmpty(oModel.getProducts()))
		{
			data.setProducts(populateProducts(oModel.getProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getCategories()))
		{
			data.setCategories(populateCategories(oModel.getCategories()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getSecondProducts()))
		{
			data.setSecProducts(populateProducts(oModel.getSecondProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getSecondCategories()))
		{
			data.setSecCategories(populateCategories(oModel.getSecondCategories()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getExcludedProducts()))
		{
			data.setExcludedProducts(populateProducts(oModel.getExcludedProducts()));
		}

		if (null != oModel.getMinimumAmount() && oModel.getMinimumAmount().doubleValue() > 0)
		{
			data.setCatMinAmnt(oModel.getMinimumAmount().toString());
		}

		if (null != oModel.getPercentageOrAmount())
		{
			data.setIsPercentage(oModel.getPercentageOrAmount().toString());
		}

		if (null != oModel.getPercentageDiscount() && oModel.getPercentageDiscount().doubleValue() > 0)
		{
			data.setPercentage(oModel.getPercentageDiscount().toString());
		}

		if (CollectionUtils.isNotEmpty(oModel.getDiscountPrices()))
		{
			data.setDiscountPrices(getDiscount(oModel.getDiscountPrices()));
		}

		if (null != oModel.getMaxDiscount() && oModel.getMaxDiscount().doubleValue() > 0)
		{
			data.setMaxDiscount(oModel.getMaxDiscount().toString());
		}

		if (StringUtils.isNotEmpty(oModel.getMessageFired()))
		{
			data.setFiredMessage(replaceCommaWithSpace(oModel.getMessageFired()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageCouldHaveFired()))
		{
			data.setCouldFireMessage(replaceCommaWithSpace(oModel.getMessageCouldHaveFired()));
		}

		return data;
	}


	/**
	 * Campaign Data For Model : BuyAPercentageDiscountModel
	 *
	 * @param promotion
	 * @param campaignData
	 * @return data
	 */
	private CampaignData getBuyAPercentageDisData(final AbstractPromotionModel promotion, final CampaignData campaignData)
	{
		CampaignData data = campaignData;
		final BuyAPercentageDiscountModel oModel = (BuyAPercentageDiscountModel) promotion;
		data = populateDefaultCampData(promotion, campaignData);

		if (CollectionUtils.isNotEmpty(oModel.getProducts()))
		{
			data.setProducts(populateProducts(oModel.getProducts()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getCategories()))
		{
			data.setCategories(populateCategories(oModel.getCategories()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getExcludedProducts()))
		{
			data.setExcludedProducts(populateProducts(oModel.getExcludedProducts()));
		}

		if (null != oModel.getQuantity() && oModel.getQuantity().longValue() > 0)
		{
			data.setQuantity(oModel.getQuantity().toString());
		}

		if (null != oModel.getMinimumAmount() && oModel.getMinimumAmount().doubleValue() > 0)
		{
			data.setCatMinAmnt(oModel.getMinimumAmount().toString());
		}

		if (null != oModel.getPercentageOrAmount())
		{
			data.setIsPercentage(oModel.getPercentageOrAmount().toString());
		}

		if (null != oModel.getPercentageDiscount() && oModel.getPercentageDiscount().doubleValue() > 0)
		{
			data.setPercentage(oModel.getPercentageDiscount().toString());
		}

		if (CollectionUtils.isNotEmpty(oModel.getDiscountPrices()))
		{
			data.setDiscountPrices(getDiscount(oModel.getDiscountPrices()));
		}

		if (null != oModel.getMaxDiscountVal() && oModel.getMaxDiscountVal().doubleValue() > 0)
		{
			data.setMaxDiscount(oModel.getMaxDiscountVal().toString());
		}

		if (StringUtils.isNotEmpty(oModel.getMessageFired()))
		{
			data.setFiredMessage(replaceCommaWithSpace(oModel.getMessageFired()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageCouldHaveFired()))
		{
			data.setCouldFireMessage(replaceCommaWithSpace(oModel.getMessageCouldHaveFired()));
		}

		return data;
	}




	/**
	 * Campaign Data For Model : BuyAboveXGetPromotionOnShippingChargesModel
	 *
	 * @param promotion
	 * @param campaignData
	 * @return data
	 */
	private CampaignData getCartShipPromoData(final AbstractPromotionModel promotion, final CampaignData campaignData)
	{
		CampaignData data = campaignData;
		final BuyAboveXGetPromotionOnShippingChargesModel oModel = (BuyAboveXGetPromotionOnShippingChargesModel) promotion;

		data = populateDefaultCampData(promotion, campaignData);

		if (null != oModel.getDiscTypesOnShippingCharges() && null != oModel.getDiscTypesOnShippingCharges())
		{
			data.setDiscountType(oModel.getDiscTypesOnShippingCharges().getCode());
		}

		if (null != oModel.getPercentageDiscount() && oModel.getPercentageDiscount().doubleValue() > 0)
		{
			data.setPercentage(oModel.getPercentageDiscount().toString());
		}

		if (CollectionUtils.isNotEmpty(oModel.getDiscountPrices()))
		{
			data.setDiscountPrices(getDiscount(oModel.getDiscountPrices()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageFired()))
		{
			data.setFiredMessage(replaceCommaWithSpace(oModel.getMessageFired()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageCouldHaveFired()))
		{
			data.setCouldFireMessage(replaceCommaWithSpace(oModel.getMessageCouldHaveFired()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getThresholdTotals()))
		{
			data.setThreshTotals(getDiscount(oModel.getThresholdTotals()));
		}

		if (null != oModel.getTShip() && oModel.getTShip().booleanValue())
		{
			data.setIsTship(MarketplacecommerceservicesConstants.TRUE);
		}

		if (null != oModel.getSShip() && oModel.getSShip().booleanValue())
		{
			data.setIsSShip(MarketplacecommerceservicesConstants.TRUE);
		}

		if (CollectionUtils.isNotEmpty(oModel.getDeliveryModeDetailsList()))
		{
			data.setDeliveryMode(getDeliveryMode(oModel.getDeliveryModeDetailsList()));
		}

		return data;
	}



	/**
	 * Campaign Data For Model : CartOrderThresholdDiscountCashbackModel
	 *
	 * @param promotion
	 * @param campaignData
	 * @return data
	 */
	private CampaignData getCartDiscountCashBackData(final AbstractPromotionModel promotion, final CampaignData campaignData)
	{
		CampaignData data = campaignData;
		final CartOrderThresholdDiscountCashbackModel oModel = (CartOrderThresholdDiscountCashbackModel) promotion;

		data = populateDefaultCampData(promotion, campaignData);
		if (null != oModel.getPercentageOrAmount())
		{
			data.setIsPercentage(oModel.getPercentageOrAmount().toString());
		}

		if (null != oModel.getPercentageDiscount() && oModel.getPercentageDiscount().doubleValue() > 0)
		{
			data.setPercentage(oModel.getPercentageDiscount().toString());
		}

		if (CollectionUtils.isNotEmpty(oModel.getDiscountPrices()))
		{
			data.setDiscountPrices(getDiscount(oModel.getDiscountPrices()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageFired()))
		{
			data.setFiredMessage(replaceCommaWithSpace(oModel.getMessageFired()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageCouldHaveFired()))
		{
			data.setCouldFireMessage(replaceCommaWithSpace(oModel.getMessageCouldHaveFired()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getThresholdTotals()))
		{
			data.setThreshTotals(getDiscount(oModel.getThresholdTotals()));
		}

		return data;
	}

	/**
	 * Campaign Data For Model : CartOrderThresholdDiscountPromotionModel
	 *
	 * @param promotion
	 * @param campaignData
	 * @return CampaignData
	 */
	private CampaignData getCartDiscountPromoData(final AbstractPromotionModel promotion, final CampaignData campaignData)
	{
		CampaignData data = campaignData;
		final CartOrderThresholdDiscountPromotionModel oModel = (CartOrderThresholdDiscountPromotionModel) promotion;
		data = populateDefaultCampData(promotion, campaignData);

		if (null != oModel.getPercentageOrAmount())
		{
			data.setIsPercentage(oModel.getPercentageOrAmount().toString());
		}

		if (null != oModel.getPercentageDiscount() && oModel.getPercentageDiscount().doubleValue() > 0)
		{
			data.setPercentage(oModel.getPercentageDiscount().toString());
		}

		if (CollectionUtils.isNotEmpty(oModel.getDiscountPrices()))
		{
			data.setDiscountPrices(getDiscount(oModel.getDiscountPrices()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageFired()))
		{
			data.setFiredMessage(replaceCommaWithSpace(oModel.getMessageFired()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageCouldHaveFired()))
		{
			data.setCouldFireMessage(replaceCommaWithSpace(oModel.getMessageCouldHaveFired()));
		}

		if (CollectionUtils.isNotEmpty(oModel.getThresholdTotals()))
		{
			data.setThreshTotals(getDiscount(oModel.getThresholdTotals()));
		}


		return data;
	}






	/**
	 * Get Delivery Details Data
	 *
	 *
	 * @param deliveryModeDetailsList
	 * @return deliverydata
	 */
	private String getDeliveryMode(final List<DeliveryModeModel> deliveryModeDetailsList)
	{
		String deliverydata = MarketplacecommerceservicesConstants.EMPTYSPACE;

		for (int i = 0; i < deliveryModeDetailsList.size(); i++)
		{
			if ((i != (deliveryModeDetailsList.size() - 1)))
			{
				deliverydata = deliverydata + (deliveryModeDetailsList.get(i).getCode())
						+ MarketplacecommerceservicesConstants.CAMPAIGN_MULTIDATA_SEPERATOR;
			}
			else
			{
				deliverydata = deliverydata + (deliveryModeDetailsList.get(i).getCode());
			}
		}
		return deliverydata;
	}



	/**
	 * Populate Restriction Data
	 *
	 * @param restrictions
	 * @return restrictiondata
	 */
	private String getRestrictionData(final Collection<AbstractPromotionRestrictionModel> restrictions)
	{
		String restrictiondata = MarketplacecommerceservicesConstants.EMPTYSPACE;

		final List<AbstractPromotionRestrictionModel> restrictionList = new ArrayList<AbstractPromotionRestrictionModel>(
				restrictions);

		for (final AbstractPromotionRestrictionModel oModel : restrictionList)
		{
			if (oModel instanceof EtailSellerSpecificRestrictionModel)
			{
				restrictiondata = restrictiondata + MarketplacecommerceservicesConstants.SINGLE_SPACE
						+ Localization.getLocalizedString("type.EtailSellerSpecificRestriction.name")
						+ MarketplacecommerceservicesConstants.SINGLE_SPACE;
			}
			else if (oModel instanceof EtailExcludeSellerSpecificRestrictionModel)
			{
				restrictiondata = restrictiondata + MarketplacecommerceservicesConstants.SINGLE_SPACE
						+ Localization.getLocalizedString("type.EtailExcludeSellerSpecificRestriction.name")
						+ MarketplacecommerceservicesConstants.SINGLE_SPACE;
			}
			else if (oModel instanceof DeliveryModePromotionRestrictionModel)
			{
				restrictiondata = restrictiondata + MarketplacecommerceservicesConstants.SINGLE_SPACE
						+ Localization.getLocalizedString("type.DeliveryModePromotionRestriction.name")
						+ MarketplacecommerceservicesConstants.SINGLE_SPACE;
			}
			else if (oModel instanceof ManufacturersRestrictionModel)
			{
				restrictiondata = restrictiondata + MarketplacecommerceservicesConstants.SINGLE_SPACE
						+ Localization.getLocalizedString("type.ManufacturersRestriction.name")
						+ MarketplacecommerceservicesConstants.SINGLE_SPACE;
			}
			else if (oModel instanceof ExcludeManufacturersRestrictionModel)
			{
				restrictiondata = restrictiondata + MarketplacecommerceservicesConstants.SINGLE_SPACE
						+ Localization.getLocalizedString("type.ExcludeManufacturersRestriction.name")
						+ MarketplacecommerceservicesConstants.SINGLE_SPACE;
			}
			else if (oModel instanceof PaymentModeSpecificPromotionRestrictionModel)
			{
				restrictiondata = restrictiondata + MarketplacecommerceservicesConstants.SINGLE_SPACE
						+ Localization.getLocalizedString("type.PaymentModeSpecificPromotionRestriction.name")
						+ MarketplacecommerceservicesConstants.SINGLE_SPACE;
			}

		}


		return restrictiondata;
	}

	/**
	 * The Method populates the Offer Channel
	 *
	 * @param promotion
	 * @return offerChannel
	 */
	private String populateOfferChannel(final AbstractPromotionModel promotion)
	{
		String offerChannel = MarketplacecommerceservicesConstants.EMPTY;
		if (CollectionUtils.isNotEmpty(promotion.getChannel()))
		{
			for (int i = 0; i < promotion.getChannel().size(); i++)
			{
				if ((i != (promotion.getChannel().size() - 1)))
				{
					offerChannel = offerChannel + (promotion.getChannel().get(i).getCode().toUpperCase())
							+ MarketplacecommerceservicesConstants.CAMPAIGN_MULTIDATA_SEPERATOR;
				}
				else
				{
					offerChannel = offerChannel + (promotion.getChannel().get(i).getCode().toUpperCase());
				}
			}
		}
		else
		{
			offerChannel = MarketplacecommerceservicesConstants.CAMPAIGN_CHANNEL;
		}
		return offerChannel;
	}


	/**
	 * Returns Discount Data
	 *
	 * @param collection
	 * @return discount
	 */
	private String getDiscount(final Collection<PromotionPriceRowModel> collection)
	{
		String discount = MarketplacecommerceservicesConstants.EMPTY;
		for (final PromotionPriceRowModel oModel : collection)
		{
			if (null != oModel.getPrice() && oModel.getPrice().doubleValue() > 0)
			{
				discount = oModel.getPrice().toString();
			}
		}
		return discount;
	}


	/**
	 * The Method is used to populate URL Data
	 *
	 * @param promotion
	 * @return url
	 */
	private String populateOfferURL(final AbstractPromotionModel promotion)
	{
		String url = MarketplacecommerceservicesConstants.EMPTY;

		List<String> urlDataList = new ArrayList<String>();
		if (null != promotion)
		{
			urlDataList = populateOfferURLData(promotion);
			if (CollectionUtils.isNotEmpty(urlDataList))
			{
				url = formURLData(urlDataList, promotion);
			}
			else
			{
				final StringBuilder promoURL = new StringBuilder();
				promoURL.append(configurationService.getConfiguration().getString("campaign.website.environment",
						MarketplacecommerceservicesConstants.CAMPAIGN_WEBSITE));
				promoURL.append(MarketplacecommerceservicesConstants.CAMPAIGN_URL_OFFER_IDENTIFIER);
				promoURL.append(MarketplacecommerceservicesConstants.CAMPAIGN_URL_ALL);

				promoURL.append('/');
				promoURL.append(MarketplacecommerceservicesConstants.CAMPAIGN_URL_OFFER_ID_URL);
				promoURL.append(promotion.getCode());

				url = promoURL.toString();
			}
		}



		return url;
	}



	/**
	 * Form URL DATA
	 *
	 * @param urlDataList
	 * @param promotion
	 * @return url
	 */
	private String formURLData(final List<String> urlDataList, final AbstractPromotionModel promotion)
	{
		String url = MarketplacecommerceservicesConstants.EMPTY;
		if (CollectionUtils.isNotEmpty(urlDataList))
		{
			for (int i = 0; i < urlDataList.size(); i++)
			{
				if ((i != (urlDataList.size() - 1)))
				{
					final StringBuilder promoURL = new StringBuilder();
					promoURL.append(configurationService.getConfiguration().getString("campaign.website.environment",
							MarketplacecommerceservicesConstants.CAMPAIGN_WEBSITE));
					promoURL.append(MarketplacecommerceservicesConstants.CAMPAIGN_URL_OFFER_IDENTIFIER);
					promoURL.append(urlDataList.get(i));
					promoURL.append('/');
					promoURL.append(MarketplacecommerceservicesConstants.CAMPAIGN_URL_OFFER_ID_URL);
					promoURL.append(promotion.getCode());

					url = url + promoURL.toString() + MarketplacecommerceservicesConstants.CAMPAIGN_MULTIDATA_SEPERATOR;
				}
				else
				{
					final StringBuilder promoURL = new StringBuilder();
					promoURL.append(configurationService.getConfiguration().getString("campaign.website.environment",
							MarketplacecommerceservicesConstants.CAMPAIGN_WEBSITE));
					promoURL.append(MarketplacecommerceservicesConstants.CAMPAIGN_URL_OFFER_IDENTIFIER);
					promoURL.append(urlDataList.get(i));
					promoURL.append('/');
					promoURL.append(MarketplacecommerceservicesConstants.CAMPAIGN_URL_OFFER_ID_URL);
					promoURL.append(promotion.getCode());

					url = url + promoURL.toString();
				}

			}
		}
		return url;
	}



	/**
	 * The Method is used to populate URL Data
	 *
	 * @param promotion
	 * @return urlDataList
	 */
	private List<String> populateOfferURLData(final AbstractPromotionModel promotion)
	{

		final List<String> urlDataList = new ArrayList<String>();
		if (promotion instanceof ProductPromotionModel)
		{
			final ProductPromotionModel productPromotion = (ProductPromotionModel) promotion;
			if (CollectionUtils.isNotEmpty(productPromotion.getCategories()))
			{
				for (final CategoryModel category : productPromotion.getCategories())
				{
					urlDataList.add(category.getCode());
				}
			}
			else if (CollectionUtils.isNotEmpty(promotion.getRestrictions()))
			{
				for (final AbstractPromotionRestrictionModel restriction : promotion.getRestrictions())
				{
					if (restriction instanceof EtailSellerSpecificRestrictionModel)
					{
						for (final SellerMasterModel seller : ((EtailSellerSpecificRestrictionModel) restriction).getSellerMasterList())
						{
							urlDataList.add(seller.getId());
						}

						break;
					}
				}
			}
		}

		return urlDataList;
	}


	/**
	 * Populate Product Data
	 *
	 * @param products
	 * @return productData
	 */
	private String populateProducts(final Collection<ProductModel> products)
	{
		String productData = MarketplacecommerceservicesConstants.EMPTY;
		final List<ProductModel> productList = new ArrayList<ProductModel>(products);
		for (int i = 0; i < productList.size(); i++)
		{
			if ((i != (productList.size() - 1)))
			{
				productData = productData + (productList.get(i).getCode())
						+ MarketplacecommerceservicesConstants.CAMPAIGN_MULTIDATA_SEPERATOR;
			}
			else
			{
				productData = productData + (productList.get(i).getCode().toUpperCase());
			}
		}

		return productData;
	}

	/**
	 * Populate Category Data
	 *
	 * @param categories
	 * @return categoryData
	 */
	private String populateCategories(final Collection<CategoryModel> categories)
	{
		String categoryData = MarketplacecommerceservicesConstants.EMPTY;
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>(categories);
		for (int i = 0; i < categoryList.size(); i++)
		{
			if ((i != (categoryList.size() - 1)))
			{
				categoryData = categoryData + (categoryList.get(i).getCode())
						+ MarketplacecommerceservicesConstants.CAMPAIGN_MULTIDATA_SEPERATOR;
			}
			else
			{
				categoryData = categoryData + (categoryList.get(i).getCode().toUpperCase());
			}
		}

		return categoryData;
	}


	private CampaignData populateDefaultCampData(final AbstractPromotionModel promotion, final CampaignData campaignData)
	{
		final CampaignData data = campaignData;

		data.setIdentifier(promotion.getCode());
		data.setPromotionGrp(promotion.getPromotionGroup().getIdentifier());
		data.setPriority(promotion.getPriority().toString());
		data.setStartDate(formatter.format(promotion.getStartDate()));
		data.setEndDate(formatter.format(promotion.getEndDate()));
		data.setUrl(populateOfferURL(promotion));
		data.setChannel(populateOfferChannel(promotion));

		if (StringUtils.isNotEmpty(promotion.getTitle()))
		{
			data.setTitle(promotion.getTitle());
		}

		if (StringUtils.isNotEmpty(promotion.getDescription()))
		{
			data.setDescription(replaceCommaWithSpace(promotion.getDescription()));
		}

		if (null != promotion.getEnabled())
		{
			data.setEnabled(promotion.getEnabled().toString());
		}

		if (CollectionUtils.isNotEmpty(promotion.getRestrictions()))
		{
			data.setRestrictions(getRestrictionData(promotion.getRestrictions()));
		}

		return data;
	}

	/**
	 * Set Default Values
	 *
	 * @param data
	 * @return data
	 */
	private CampaignData setDefaultData(final CampaignData data)
	{
		data.setIdentifier(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setTitle(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setPromotionGrp(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setDescription(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setEnabled(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setPriority(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setChannel(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setUrl(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setProducts(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setCategories(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setExcludedProducts(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setCatMinAmnt(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setQuantity(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setMaxDiscount(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setIsPercentage(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setPercentage(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setDiscountPrices(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setGiftProducts(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setStartDate(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setEndDate(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setRestrictions(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setFiredMessage(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setCouldFireMessage(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setSecProducts(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setSecCategories(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setThreshTotals(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setIsTship(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setIsSShip(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setDiscountType(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setDeliveryMode(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setFreecount(MarketplacecommerceservicesConstants.EMPTYSPACE);


		return data;
	}

	/**
	 * The Method populates the Offer Channel
	 *
	 * @param promotion
	 * @return offerChannelList
	 */
	//	private List<String> populateOfferChannel(final AbstractPromotionModel promotion)
	//	{
	//		final List<String> offerChannelList = new ArrayList<String>();
	//		final List<EnumerationValueModel> salesAppEnumList = mplEnumerationHelper
	//				.getEnumerationValuesForCode(SalesApplication._TYPECODE);
	//
	//		if (CollectionUtils.isNotEmpty(promotion.getChannel()))
	//		{
	//			final List<SalesApplication> channelList = promotion.getChannel();
	//			for (final SalesApplication channel : channelList)
	//			{
	//				offerChannelList.add(channel.getCode().toUpperCase());
	//			}
	//		}
	//		else
	//		{
	//			for (final EnumerationValueModel enumerationValueModel : salesAppEnumList)
	//			{
	//				if (enumerationValueModel != null)
	//				{
	//					offerChannelList.add(enumerationValueModel.getCode().toUpperCase());
	//				}
	//			}
	//		}
	//		return offerChannelList;
	//	}


	/**
	 * The Method replace all commas with spaces from a given string
	 *
	 * @param paramToReplace
	 * @return String
	 */

	private String replaceCommaWithSpace(final String paramString)
	{
		return paramString.replaceAll(",", "");
	}



	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the formatter
	 */
	public SimpleDateFormat getFormatter()
	{
		return formatter;
	}
}
