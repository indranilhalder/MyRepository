/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.jobs.PromotionCreationJob;
import com.tisl.mpl.marketplacecommerceservices.service.CampaignPromoDataService;
import com.tisl.mpl.marketplacecommerceservices.service.CampaignPromoSubService;
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

	private CampaignPromoSubService campaignPromoSubService;


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
				fileWriter.append(data.getIdentifier());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getTitle());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getPromotionGrp());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getDescription());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getEnabled());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getPriority());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getChannel());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getProducts());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getCategories());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getExcludedProducts());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getCatMinAmnt());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getQuantity());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getMaxDiscount());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getIsPercentage());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getPercentage());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getDiscountPrices());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getGiftProducts());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getStartDate());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getEndDate());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getRestrictions());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getFiredMessage());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getCouldFireMessage());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getSecProducts());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getSecCategories());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getThreshTotals());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getIsTship());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getIsSShip());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getDiscountType());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getDeliveryMode());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getFreecount());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getUrl());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

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

				campaignData = campaignPromoSubService.getPromoCampaignDetails(promotion);
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



	/**
	 * @return the campaignPromoSubService
	 */
	public CampaignPromoSubService getCampaignPromoSubService()
	{
		return campaignPromoSubService;
	}



	/**
	 * @param campaignPromoSubService
	 *           the campaignPromoSubService to set
	 */
	public void setCampaignPromoSubService(final CampaignPromoSubService campaignPromoSubService)
	{
		this.campaignPromoSubService = campaignPromoSubService;
	}




}