/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.PincodeServiceabilityDataModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPincodeServiceDao;
import com.tisl.mpl.marketplacecommerceservices.service.JuspayWebHookService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPincodeService;
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.pojo.FallbackOrderPojo;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * @author TCS
 *
 */
public class DefaultMplPincodeService implements MplPincodeService
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultMplPincodeService.class);

	private MplPincodeServiceDao mplPincodeServiceDao;
	private JuspayWebHookService juspayWebHookService;

	@Autowired
	private ConfigurationService configurationService;




	/**
	 * Keeps record of Cron Last run time
	 *
	 * @param jobCode
	 * @return MplConfigurationModel
	 */
	@Override
	public Map<String, Date> getConfigurationData(final String jobCode)
	{
		final Map<String, Date> dataMap = new HashedMap();
		try
		{
			if (StringUtils.isNotEmpty(jobCode))
			{
				final MplConfigurationModel configModel = juspayWebHookService.getCronDetails(jobCode);
				if (null != configModel && null != configModel.getMplConfigDate())
				{
					dataMap.put(jobCode, configModel.getMplConfigDate());
				}
			}
		}
		catch (final ModelNotFoundException exception)
		{
			LOG.error(exception.getMessage());
			throw exception;
		}
		catch (final Exception exception)
		{
			LOG.error(exception.getMessage());
			throw exception;
		}

		return dataMap;
	}


	/**
	 * Fetch data to invalidate
	 *
	 * @param date
	 * @param jobLastRunDate
	 */
	@Override
	public List<PincodeServiceabilityDataModel> getPincodeData(final Date jobLastRunDate, final Date date)
	{
		return mplPincodeServiceDao.getPincodeData(jobLastRunDate, date);
	}

	/**
	 * Fetch data to invalidate
	 *
	 */
	@Override
	public List<PincodeServiceabilityDataModel> fetchData()
	{
		return mplPincodeServiceDao.getPincodeData(null, new Date());
	}



	/**
	 * Save the Cron Details
	 *
	 * @param startTime
	 * @param code
	 */
	@Override
	public void saveCronData(final Date startTime, final String code)
	{
		juspayWebHookService.saveCronDetails(startTime, code);
	}




	/**
	 * @return the mplPincodeServiceDao
	 */
	public MplPincodeServiceDao getMplPincodeServiceDao()
	{
		return mplPincodeServiceDao;
	}

	/**
	 * @param mplPincodeServiceDao
	 *           the mplPincodeServiceDao to set
	 */
	public void setMplPincodeServiceDao(final MplPincodeServiceDao mplPincodeServiceDao)
	{
		this.mplPincodeServiceDao = mplPincodeServiceDao;
	}




	/**
	 * @return the juspayWebHookService
	 */
	public JuspayWebHookService getJuspayWebHookService()
	{
		return juspayWebHookService;
	}




	/**
	 * @param juspayWebHookService
	 *           the juspayWebHookService to set
	 */
	public void setJuspayWebHookService(final JuspayWebHookService juspayWebHookService)
	{
		this.juspayWebHookService = juspayWebHookService;
	}



	/**
	 * The date range between which Orders in queue needs to be fetched
	 *
	 * @param lastRunDate
	 * @param sysDate
	 */
	@Override
	public void fetchOrderData(final Date lastRunDate, final Date sysDate)
	{
		final List<OrderModel> omsFallBackOrderList = mplPincodeServiceDao.fetchOrderList(lastRunDate, sysDate);
		if (CollectionUtils.isNotEmpty(omsFallBackOrderList))
		{
			//final List<OrderModel> omsFallBckOrdrFinalList = validateData(omsFallBackOrderList);
			LOG.debug("Populating the .csv data");
			final List<FallbackOrderPojo> pojoList = populateData(omsFallBackOrderList);
			if (CollectionUtils.isNotEmpty(pojoList))
			{
				generateCSVData(pojoList);
			}
		}
	}



	/**
	 * Generate .csv with data
	 *
	 * @param pojoList
	 */
	private void generateCSVData(final List<FallbackOrderPojo> pojoList)
	{
		FileWriter fileWriter = null;
		String datePrefix = MarketplacecommerceservicesConstants.EMPTYSPACE;
		if (null != GenericUtilityMethods.convertSysDateToString(new Date()))
		{
			datePrefix = GenericUtilityMethods.convertSysDateToString(new Date());
		}

		final File rootFolder = new File(configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.FALLBACK_REPORT_LOCATION),
				MarketplacecommerceservicesConstants.FALLBACK_REPORT_NAME
						+ datePrefix
						+ configurationService.getConfiguration().getString(
								MarketplacecommerceservicesConstants.FALLBACK_REPORT_EXTENSION, ".csv"));
		try
		{
			fileWriter = new FileWriter(rootFolder);
			fileWriter.append(MarketplacecommerceservicesConstants.FALLBACK_ORDER_HEADER_REPORT);
			fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_NEW_LINE_SEPARATOR);

			for (final FallbackOrderPojo data : pojoList)
			{
				fileWriter.append(data.getOrderId());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getUser());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getOrderDate());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getTransactionId());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getOrderStatus());

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
	 * Exclude Orders which are not in Payment Successful Status
	 *
	 * @param omsFallBackOrderList
	 * @return omsFallBackOrderList
	 */
	//	private List<OrderModel> validateData(final List<OrderModel> omsFallBackOrderList)
	//	{
	//		final List<OrderModel> omsFallBckOrdrFinalList = new ArrayList<OrderModel>();
	//		for (final OrderModel order : omsFallBackOrderList)
	//		{
	//			if (null != order.getStatus() && order.getStatus().toString().equalsIgnoreCase(OrderStatus.PAYMENT_SUCCESSFUL.getCode()))
	//			{
	//				omsFallBckOrdrFinalList.add(order);
	//			}
	//		}
	//		return omsFallBckOrdrFinalList;
	//	}


	/**
	 * Populate Pojo Data
	 *
	 * @param omsFallBckOrdrFinalList
	 * @return dataList
	 */
	private List<FallbackOrderPojo> populateData(final List<OrderModel> omsFallBckOrdrFinalList)
	{
		final List<FallbackOrderPojo> dataList = new ArrayList<FallbackOrderPojo>();
		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		for (final OrderModel order : omsFallBckOrdrFinalList)
		{
			final FallbackOrderPojo pojo = new FallbackOrderPojo();
			final CustomerModel customer = (CustomerModel) order.getUser();
			String transactionIdData = MarketplacecommerceservicesConstants.EMPTYSPACE;

			transactionIdData = getTransactionIdData(order);
			if (StringUtils.isNotEmpty(transactionIdData))
			{
				pojo.setTransactionId(transactionIdData);
			}
			if (null != order.getStatus())
			{
				pojo.setOrderStatus(order.getStatus().getCode());
			}
			pojo.setOrderId(order.getCode());
			pojo.setUser(customer.getOriginalUid());
			pojo.setOrderDate(formatter.format(order.getCreationtime()));
			dataList.add(pojo);
		}
		return dataList;
	}


	/**
	 * Returns Transaction ID details
	 *
	 * @param orderData
	 * @return transactionIdData
	 */
	private String getTransactionIdData(final OrderModel orderData)
	{
		String transactionIdData = MarketplacecommerceservicesConstants.EMPTYSPACE;
		if (CollectionUtils.isNotEmpty(orderData.getChildOrders()))
		{
			for (final OrderModel order : orderData.getChildOrders())
			{
				if (CollectionUtils.isNotEmpty(order.getEntries()))
				{
					for (final AbstractOrderEntryModel entry : order.getEntries())
					{
						if (StringUtils.isNotEmpty(entry.getTransactionID()))
						{
							transactionIdData = transactionIdData + entry.getTransactionID()
									+ MarketplacecommerceservicesConstants.CONCTASTRING;
						}
					}
				}
			}
		}

		if (StringUtils.isNotEmpty(transactionIdData))
		{
			transactionIdData = transactionIdData.substring(0, transactionIdData.length() - 1);
		}
		return transactionIdData;
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