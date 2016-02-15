/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.localization.Localization;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.jobs.PromotionCreationJob;
import com.tisl.mpl.marketplacecommerceservices.service.CampaignPromoDataService;
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
import com.tisl.mpl.model.EtailSellerSpecificRestrictionModel;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.pojo.CampaignData;
import com.tisl.mpl.promotion.dao.SellerBasedPromotionDao;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * @author TCS
 *
 */
public class DefaultCampaignPromoDataService implements CampaignPromoDataService
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PromotionCreationJob.class.getName());

	private ModelService modelService;

	private SellerBasedPromotionDao sellerBasedPromotionDao;

	@Autowired
	private CampaignData campaignData;

	@Autowired
	private ConfigurationService configurationService;


	/**
	 *
	 * The Method is used to generate the .csv file data for the Campaign Team
	 *
	 */
	@Override
	public void generateData()
	{
		final List<AbstractPromotionModel> promotionList = sellerBasedPromotionDao.getPromoDetails();
		List<CampaignData> campaignDataList = new ArrayList<CampaignData>();
		if (CollectionUtils.isNotEmpty(promotionList))
		{
			LOG.debug("The Required Promotion is Fetched");
			campaignDataList = populateCampaignDataList(promotionList);

			if (CollectionUtils.isNotEmpty(campaignDataList))
			{
				generateCSV(campaignDataList);
			}
		}
	}



	/**
	 * @param campaignDataList
	 */
	private void generateCSV(final List<CampaignData> campaignDataList)
	{
		FileWriter fileWriter = null;

		String datePrefix = MarketplacecommerceservicesConstants.EMPTYSPACE;
		if (null != GenericUtilityMethods.convertSysDateToString(new Date()))
		{
			datePrefix = GenericUtilityMethods.convertSysDateToString(new Date());
		}

		final File rootFolder = new File(
				configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_LOCATION,
						MarketplacecommerceservicesConstants.CAMPAIGN_FILE_PATH),
				MarketplacecommerceservicesConstants.CAMPAIGN_FILE_NAME + datePrefix
						+ configurationService.getConfiguration().getString("cronjob.campaign.extension", ".csv"));

		try
		{
			fileWriter = new FileWriter(rootFolder);
			fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_HEADER);

			fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_NEW_LINE_SEPARATOR);

			for (final CampaignData data : campaignDataList)
			{
				fileWriter.append(data.getOfferid());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getOffername());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getOfferactive());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getOffertype());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getOfferchannel());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getOfferstartdate());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getOfferenddate());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getUrl());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getCreationdate());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getModifieddate());

				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_NEW_LINE_SEPARATOR);
			}

		}
		catch (final IOException exception)
		{
			LOG.error("IO Exception", exception);
		}
		catch (final Exception exception)
		{
			LOG.error(exception.getMessage());
		}

		finally
		{
			try
			{
				fileWriter.flush();
				fileWriter.close();
			}
			catch (final IOException exception)
			{
				LOG.error("Error while flushing/closing fileWriter !!!" + exception.getMessage());
			}
		}

	}



	/**
	 * Populate Campaign Data List
	 *
	 * @param promotionList
	 * @return campaignDataList
	 */
	private List<CampaignData> populateCampaignDataList(final List<AbstractPromotionModel> promotionList)
	{
		final List<CampaignData> campaignDataList = new ArrayList<CampaignData>();
		for (final AbstractPromotionModel promotion : promotionList)
		{
			final boolean isValid = isPromoValid(promotion);
			if (isValid)
			{
				CampaignData campaignData = new CampaignData();

				campaignData = populateCampaignData(promotion);
				if (null != campaignData)
				{
					campaignDataList.add(campaignData);
				}
			}

		}
		return campaignDataList;
	}





	/**
	 * Check if Promotion is Valid
	 *
	 * @param promotion
	 * @return boolean
	 */
	private boolean isPromoValid(final AbstractPromotionModel promotion)
	{
		final Date date = new Date();
		if ((promotion.getEndDate().after(date) || promotion.getEndDate().equals(date))
				&& (promotion.getStartDate().before(date) || promotion.getStartDate().equals(date)))
		{
			return true;
		}
		return false;
	}




	/**
	 * Populate Campaign Data
	 *
	 * @param promotion
	 *
	 * @return CampaignData
	 */
	private CampaignData populateCampaignData(final AbstractPromotionModel promotion)
	{
		CampaignData campaignData = new CampaignData();

		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		campaignData = defaultData(campaignData);
		if (null != promotion && null != promotion.getCreationtime())
		{
			campaignData.setCreationdate(formatter.format(promotion.getCreationtime()));
		}

		if (null != promotion && null != promotion.getModifiedtime())
		{
			campaignData.setModifieddate(formatter.format(promotion.getModifiedtime()));
		}

		if (null != promotion && null != promotion.getCode())
		{
			campaignData.setOfferid(promotion.getCode());
			campaignData.setOfferactive(MarketplacecommerceservicesConstants.TRUE_UPPER);
		}

		if (null != promotion && null != promotion.getTitle())
		{
			campaignData.setOffername(promotion.getTitle());
		}

		if (null != promotion && null != promotion.getStartDate())
		{
			campaignData.setOfferstartdate(formatter.format(promotion.getStartDate()));
		}

		if (null != promotion && null != promotion.getEndDate())
		{
			campaignData.setOfferenddate(formatter.format(promotion.getEndDate()));
		}

		campaignData.setOffertype(populateOfferType(promotion));
		campaignData.setOfferchannel(populateOfferChannel(promotion));
		campaignData.setUrl(populateOfferURL(promotion));

		return campaignData;
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
	 * The Method populates the Offer Type
	 *
	 * @param promotion
	 * @return offerType
	 */
	private String populateOfferType(final AbstractPromotionModel promotion)
	{
		String offerType = MarketplacecommerceservicesConstants.EMPTY;

		if (promotion instanceof BuyAPercentageDiscountModel)
		{
			offerType = Localization.getLocalizedString("type.BuyAPercentageDiscount.name");
		}
		else if (promotion instanceof BuyAandBPrecentageDiscountModel)
		{
			offerType = Localization.getLocalizedString("type.BuyAandBPrecentageDiscount.name");
		}
		else if (promotion instanceof BuyAAboveXGetPercentageOrAmountOffModel)
		{
			offerType = Localization.getLocalizedString("type.BuyAAboveXGetPercentageOrAmountOff.name");
		}
		else if (promotion instanceof CartOrderThresholdDiscountPromotionModel)
		{
			offerType = Localization.getLocalizedString("type.CartOrderThresholdDiscountPromotion.name");
		}
		else if (promotion instanceof BuyAandBgetCModel)
		{
			offerType = Localization.getLocalizedString("type.BuyAandBgetC.name");
		}
		else if (promotion instanceof BuyXItemsofproductAgetproductBforfreeModel)
		{
			offerType = Localization.getLocalizedString("type.BuyXItemsofproductAgetproductBforfree.name");
		}
		else if (promotion instanceof BuyABFreePrecentageDiscountModel)
		{
			offerType = Localization.getLocalizedString("type.BuyABFreePrecentageDiscount.name");
		}
		else if (promotion instanceof BuyAGetPromotionOnShippingChargesModel)
		{
			offerType = Localization.getLocalizedString("type.BuyAGetPromotionOnShippingCharges.name");
		}
		else if (promotion instanceof BuyAandBGetPromotionOnShippingChargesModel)
		{
			offerType = Localization.getLocalizedString("type.BuyAandBGetPromotionOnShippingCharges.name");
		}
		else if (promotion instanceof BuyAboveXGetPromotionOnShippingChargesModel)
		{
			offerType = Localization.getLocalizedString("type.BuyAboveXGetPromotionOnShippingCharges.name");
		}
		else if (promotion instanceof BuyAGetPrecentageDiscountCashbackModel)
		{
			offerType = Localization.getLocalizedString("type.BuyAGetPrecentageDiscountCashback.name");
		}
		else if (promotion instanceof BuyAandBGetPrecentageDiscountCashbackModel)
		{
			offerType = Localization.getLocalizedString("type.BuyAandBGetPrecentageDiscountCashback.name");
		}
		else if (promotion instanceof CartOrderThresholdDiscountCashbackModel)
		{
			offerType = Localization.getLocalizedString("type.CartOrderThresholdDiscountCashback.name");
		}
		else if (promotion instanceof CustomProductBOGOFPromotionModel)
		{
			offerType = Localization.getLocalizedString("type.CustomProductBOGOFPromotion.name");
		}
		return offerType;
	}



	/**
	 * @param data
	 * @return data
	 */
	private CampaignData defaultData(final CampaignData data)
	{
		data.setOfferid(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setOffername(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setOfferactive(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setOffertype(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setOfferchannel(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setOfferstartdate(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setOfferenddate(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setUrl(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setCreationdate(MarketplacecommerceservicesConstants.EMPTYSPACE);
		data.setModifieddate(MarketplacecommerceservicesConstants.EMPTYSPACE);

		return data;
	}



	/**
	 *
	 * Getter Setter Generation
	 */



	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the sellerBasedPromotionDao
	 */
	public SellerBasedPromotionDao getSellerBasedPromotionDao()
	{
		return sellerBasedPromotionDao;
	}

	/**
	 * @param sellerBasedPromotionDao
	 *           the sellerBasedPromotionDao to set
	 */
	public void setSellerBasedPromotionDao(final SellerBasedPromotionDao sellerBasedPromotionDao)
	{
		this.sellerBasedPromotionDao = sellerBasedPromotionDao;
	}






	/**
	 * @return the campaignData
	 */
	public CampaignData getCampaignData()
	{
		return campaignData;
	}






	/**
	 * @param campaignData
	 *           the campaignData to set
	 */
	public void setCampaignData(final CampaignData campaignData)
	{
		this.campaignData = campaignData;
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




}