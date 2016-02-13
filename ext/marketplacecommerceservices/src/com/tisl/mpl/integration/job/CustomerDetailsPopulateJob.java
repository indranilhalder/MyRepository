/**
 *
 */
package com.tisl.mpl.integration.job;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.FetchCustomerDetailsService;
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class CustomerDetailsPopulateJob extends AbstractJobPerformable<CronJobModel>
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CustomerDetailsPopulateJob.class.getName());

	/**
	 * @Description : Fetch Customer Creation and Update Records
	 * @param: oModels
	 * @return : PerformResult
	 */
	@Override
	public PerformResult perform(final CronJobModel oModel)
	{
		try
		{
			final MplConfigurationModel configModel = getFetchCustomerDetailsService().getCronDetails(oModel.getCode());
			if (null != configModel && null != configModel.getMplConfigDate())
			{
				LOG.debug("CRON START DATE" + configModel.getMplConfigDate());
				specificCustomerDetails(configModel.getMplConfigDate(), oModel.getStartTime());
			}
			else
			{
				LOG.debug("*********1st call to CRM JOB*************");
				populateCustomerDetails();
			}
			saveCronData(oModel);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * @De
	 * @param mplConfigDate
	 * @param startTime
	 */
	private void specificCustomerDetails(final Date mplConfigDate, final Date startTime)
	{
		final List<CustomerModel> customerData = getFetchCustomerDetailsService().specificCustomerDetails(mplConfigDate, startTime);
		if (null != customerData && !customerData.isEmpty())
		{
			LOG.debug("******customerData not empty or null******");
			getCustomerXMLUtlity().generateCustomerXMlData(customerData);
		}

	}

	/**
	 * @Description : Fetch Customer Details (Method executed when cron runs for the first time)
	 * @return: void
	 */
	private void populateCustomerDetails()
	{
		final List<CustomerModel> customerData = getFetchCustomerDetailsService().fetchCustomerDetails();

		if (null != customerData && !customerData.isEmpty())
		{
			LOG.debug("customerData not empty or null");
			getCustomerXMLUtlity().generateCustomerXMlData(customerData);
		}
	}

	/**
	 * @Description : Save Cron Job Details
	 * @param oModel
	 */
	private void saveCronData(final CronJobModel oModel)
	{
		if (null != oModel && null != oModel.getStartTime() && null != oModel.getCode())
		{
			getFetchCustomerDetailsService().saveCronDetails(oModel.getStartTime(), oModel.getCode());
		}

	}

	protected FetchCustomerDetailsService getFetchCustomerDetailsService()
	{
		return Registry.getApplicationContext().getBean("fetchCustomerDetailsServiceImpl", FetchCustomerDetailsService.class);
	}

	protected CustomerXMLUtlity getCustomerXMLUtlity()
	{
		return Registry.getApplicationContext().getBean("customerXMLUtlity", CustomerXMLUtlity.class);
	}

}
