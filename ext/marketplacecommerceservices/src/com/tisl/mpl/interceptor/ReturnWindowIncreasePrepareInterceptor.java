/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.core.model.ReturnWindowAuditModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.businesscontentimport.BusinessContentImportUtility;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.ReturnWindowIncreaseModel;
import com.tisl.mpl.returnwindowincrease.dao.ReturnWindowIncreaseDao;
import com.tisl.mpl.returnwindowincrease.service.ReturnWindowIncreaseService;


/**
 * @author TCS
 *
 */
public class ReturnWindowIncreasePrepareInterceptor implements PrepareInterceptor
{


	@Resource
	BusinessContentImportUtility businessContentImportUtil;
	private static final Logger LOG = Logger.getLogger(ReturnWindowIncreasePrepareInterceptor.class); //critical SONAR FIX
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private MediaService mediaService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private UserService userService;
	@Autowired
	private ReturnWindowIncreaseService returnWindowIncreaseService;
	@Autowired
	private ReturnWindowIncreaseDao returnWindowIncreaseDao;


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
	 * @Description This method will execute the content creation and run the cron job before uploading csv
	 */
	@Override
	public void onPrepare(final Object o, final InterceptorContext its) throws InterceptorException
	{
		if (o instanceof ReturnWindowIncreaseModel)
		{
			try
			{

				//calling the uploadCSVContent with the input stream and the real file name from Media Model
				LOG.debug("Entered Into Return Window Increase Interceptor");
				List<ConsignmentModel> updatedConsignmentList = null;


				final ReturnWindowIncreaseModel returnWindowIncreaseModel = (ReturnWindowIncreaseModel) o;
				final List<ReturnWindowAuditModel> returnWindowAuditModelList = new ArrayList<ReturnWindowAuditModel>(0);
				if (StringUtils.isEmpty(returnWindowIncreaseModel.getCsvFile().getURL()))
				{
					throw new InterceptorException("Please upload a CSV file");
				}
				else if (!returnWindowIncreaseModel.getCsvFile().getMime().equals("text/csv"))
				{
					throw new InterceptorException("Please upload a valid CSV file. MIME type is not text/csv. Current MINE type is "
							+ returnWindowIncreaseModel.getCsvFile().getMime());
				}
				LOG.debug("Input Stream starting");

				final InputStream inputStream = mediaService.getStreamFromMedia(returnWindowIncreaseModel.getCsvFile());

				final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				String transactionId;
				final Date systime = new Date();
				List<String> transactionIdList;
				transactionIdList = new ArrayList<String>(0);
				Map<String, ReturnWindowAuditModel> map;
				map = new LinkedHashMap<String, ReturnWindowAuditModel>();
				ReturnWindowAuditModel obj;
				final EmployeeModel emp = (EmployeeModel) userService.getCurrentUser();
				LOG.debug("Before Loop start");

				int ctr = 1;
				while ((transactionId = reader.readLine()) != null)
				{
					obj = new ReturnWindowAuditModel();
					obj.setTransactionId(transactionId);
					obj.setStatus(MarketplacecommerceservicesConstants.PENDING);
					obj.setUser(emp);
					obj.setBatchts(systime);
					transactionIdList.add(transactionId);
					map.put(transactionId, obj);
					++ctr;
				}
				reader.close();
				LOG.debug("Reader closed and File Read Complete");


				LOG.debug("After Loop start" + ctr);
				if (map.size() > 1000
						|| map.size() > Integer.parseInt(configurationService.getConfiguration().getString(
								MarketplacecommerceservicesConstants.RETURNWINDOWBATCH)))

				{

					throw new InterceptorException("Please upload a CSV file with less than "
							+ Integer.parseInt(configurationService.getConfiguration().getString(
									MarketplacecommerceservicesConstants.RETURNWINDOWBATCH)) + " entries."
							+ returnWindowIncreaseModel.getCsvFile().getMime());
				}
				else
				{
					updatedConsignmentList = returnWindowIncreaseService.getConsignment(transactionIdList);
					LOG.debug("Consignment fetched from DB");
					if (null != updatedConsignmentList)
					{

						for (int i = 0; i < updatedConsignmentList.size(); i++)
						{
							try
							{
								updatedConsignmentList.get(i).setDeliveryDate(systime);

								if (StringUtils.isNotEmpty(updatedConsignmentList.get(i).getCode()))
								{
									LOG.debug("Consignment Updating systime...  " + updatedConsignmentList.get(i).getCode());
									map.get(updatedConsignmentList.get(i).getCode()).setStatus(
											MarketplacecommerceservicesConstants.PROCESSED);
								}

								LOG.debug("Consignment status set as processed");
							}
							catch (final Exception ex)
							{
								LOG.debug("Failed to update systime. index: " + i);
							}
						}

						for (final Map.Entry<String, ReturnWindowAuditModel> entry : map.entrySet())
						{
							if (!entry.getValue().getStatus().equals(MarketplacecommerceservicesConstants.PROCESSED))
							{
								entry.getValue().setStatus(MarketplacecommerceservicesConstants.NOTFOUND);
								returnWindowAuditModelList.add(entry.getValue());
								LOG.debug("Consignment not found in DB and set status as Not Found in Audit Model");
							}
							else
							{
								LOG.debug("Saving returnWindowAuditModelList");
								returnWindowAuditModelList.add(entry.getValue());
							}

						}
						try
						{

							LOG.debug("Saving updatedConsignmentList");
							modelService.saveAll(updatedConsignmentList);
						}
						catch (final Exception exp)
						{
							LOG.debug("Not able to update ConsignmentList");
						}
						try
						{
							modelService.saveAll(returnWindowAuditModelList);
						}
						catch (final Exception exp)
						{
							LOG.debug("Not able to update auditModelList");
						}
					}
					LOG.debug("Exiting From Return Window Increase Interceptor");

				}
			}
			catch (final Exception e)
			{
				//e.printStackTrace();
				LOG.error("onPrepare Return Window Increase exception ");
				throw new InterceptorException("exception has occured...." + e.getMessage());
			}

		}

	}
	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.interceptor.LoadInterceptor#onLoad(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */

}