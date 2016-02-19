/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.category.model.CategoryModel;
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
import com.tisl.mpl.model.CartOrderThresholdDiscountPromotionModel;
import com.tisl.mpl.model.DeliveryModePromotionRestrictionModel;
import com.tisl.mpl.model.EtailExcludeSellerSpecificRestrictionModel;
import com.tisl.mpl.model.EtailSellerSpecificRestrictionModel;
import com.tisl.mpl.model.ExcludeManufacturersRestrictionModel;
import com.tisl.mpl.model.ManufacturersRestrictionModel;
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

		return campaignData;
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
		final CampaignData data = campaignData;
		final CartOrderThresholdDiscountPromotionModel oModel = (CartOrderThresholdDiscountPromotionModel) promotion;
		data.setIdentifier(promotion.getCode());
		data.setPromotionGrp(promotion.getPromotionGroup().getIdentifier());
		data.setPriority(promotion.getPriority().toString());
		data.setStartDate(formatter.format(promotion.getStartDate()));
		data.setEndDate(formatter.format(promotion.getEndDate()));
		data.setUrl(populateOfferURL(promotion));

		if (StringUtils.isNotEmpty(promotion.getTitle()))
		{
			data.setTitle(promotion.getTitle());
		}

		if (StringUtils.isNotEmpty(promotion.getDescription()))
		{
			data.setDescription(promotion.getDescription());
		}

		if (null != promotion.getEnabled())
		{
			data.setEnabled(promotion.getEnabled().toString());
		}

		if (CollectionUtils.isNotEmpty(promotion.getChannel()))
		{
			data.setChannel(populateOfferChannel(promotion));
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

		if (CollectionUtils.isNotEmpty(oModel.getRestrictions()))
		{
			data.setRestrictions(getRestrictionData(oModel.getRestrictions()));
		}

		if (StringUtils.isNotEmpty(oModel.getMessageFired()))
		{
			data.setFiredMessage(oModel.getMessageFired());
		}

		if (StringUtils.isNotEmpty(oModel.getMessageCouldHaveFired()))
		{
			data.setCouldFireMessage(oModel.getMessageCouldHaveFired());
		}


		if (CollectionUtils.isNotEmpty(oModel.getThresholdTotals()))
		{
			data.setThreshTotals(getDiscount(oModel.getThresholdTotals()));
		}


		return data;
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
