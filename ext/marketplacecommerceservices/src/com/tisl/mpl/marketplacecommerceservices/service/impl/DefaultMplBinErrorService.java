/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

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
import com.tisl.mpl.core.model.BinErrorModel;
import com.tisl.mpl.marketplacecommerceservices.jobs.PromotionCreationJob;
import com.tisl.mpl.marketplacecommerceservices.service.MplBinErrorService;
import com.tisl.mpl.pojo.MplBinErrorData;
import com.tisl.mpl.promotion.dao.MplBinErrorDao;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * @author TCS
 *
 */
public class DefaultMplBinErrorService implements MplBinErrorService
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PromotionCreationJob.class.getName());

	private ModelService modelService;
	private MplBinErrorDao mplBinErrorDao;

	@Autowired
	private ConfigurationService configurationService;



	/**
	 *
	 * The Method is used to generate the .csv file data for the Bin Error
	 *
	 */
	@Override
	public void generateData()
	{
		final List<BinErrorModel> binErrorList = mplBinErrorDao.getBinErrorDetails();
		if (CollectionUtils.isNotEmpty(binErrorList))
		{
			LOG.debug("The Required Bin is Fetched");
			final List<MplBinErrorData> binDataList = populateDataList(binErrorList);


			if (CollectionUtils.isNotEmpty(binDataList))
			{
				generateCSV(binDataList, binErrorList);
			}
		}
	}



	/**
	 * @param binErrorList
	 * @return binErrorList
	 */
	private List<MplBinErrorData> populateDataList(final List<BinErrorModel> binErrorList)
	{
		final List<MplBinErrorData> binDataList = new ArrayList<MplBinErrorData>();

		for (final BinErrorModel binError : binErrorList)
		{
			final MplBinErrorData data = new MplBinErrorData();
			data.setBin(binError.getBin());
			data.setCustomerId(binError.getCustomerId());
			data.setPaymentMode(binError.getPaymentMode());
			data.setTime(binError.getCreationtime());
			data.setTypeOfError(binError.getTypeOfError().getCode());
			binDataList.add(data);
		}
		return binDataList;
	}



	/**
	 * @param binDataList
	 * @param binErrorList
	 */
	private void generateCSV(final List<MplBinErrorData> binDataList, final List<BinErrorModel> binErrorList)
	{
		FileWriter fileWriter = null;

		String datePrefix = MarketplacecommerceservicesConstants.EMPTYSPACE;
		if (null != GenericUtilityMethods.convertSysDateToString(new Date()))
		{
			datePrefix = GenericUtilityMethods.convertSysDateToString(new Date());
		}

		final File rootFolderOfBinError = new File(configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.BIN_ERROR_FILE_LOCATION,
				MarketplacecommerceservicesConstants.BIN_ERROR_FILE_PATH), MarketplacecommerceservicesConstants.BIN_ERROR_FILE_NAME
				+ datePrefix + configurationService.getConfiguration().getString("cronjob.campaign.extension", ".csv"));

		try
		{
			fileWriter = new FileWriter(rootFolderOfBinError);
			fileWriter.append(MarketplacecommerceservicesConstants.BIN_ERROR_HEADER);

			fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_NEW_LINE_SEPARATOR);

			for (final MplBinErrorData data : binDataList)
			{
				fileWriter.append(data.getBin());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getCustomerId());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getPaymentMode());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getTime().toGMTString());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getTypeOfError());
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


		if (rootFolderOfBinError.exists())
		{
			LOG.error("**************Removing all Data post Report Generation********************");
			modelService.removeAll(binErrorList);
			LOG.error("**************Removing all Data post Report Generation Succcessful********************");
		}

	}



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
	 * @return the mplBinErrorDao
	 */
	public MplBinErrorDao getMplBinErrorDao()
	{
		return mplBinErrorDao;
	}



	/**
	 * @param mplBinErrorDao
	 *           the mplBinErrorDao to set
	 */
	public void setMplBinErrorDao(final MplBinErrorDao mplBinErrorDao)
	{
		this.mplBinErrorDao = mplBinErrorDao;
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