/**
 *
 */
package com.tisl.mpl.integration.job;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.List;

import org.apache.log4j.Logger;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.FetchSalesOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.DefaultFetchMerchantMasterServiceImpl;
import com.tisl.mpl.model.MerchantMasterTableModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class MerchantMaster extends AbstractJobPerformable<CronJobModel>
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MerchantMaster.class.getName());

	/**
	 * @Description :
	 * @param: cronoModel
	 * @return : PerformResult
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel
	 * )
	 */
	@Override
	public PerformResult perform(final CronJobModel cronModel)
	{
		try
		{
			populateMasterData();
		}
		catch (final EtailBusinessExceptions exception)
		{
			ExceptionUtil.etailBusinessExceptionHandler(exception, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions exception)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(exception);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final Exception exception)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exception));
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected DefaultFetchMerchantMasterServiceImpl getFetchMerchantMasterTableService()
	{
		return Registry.getApplicationContext().getBean("fetchMerchantMasterServiceImpl",
				DefaultFetchMerchantMasterServiceImpl.class);
	}

	// method to receive data from database and generate xml
	private void populateMasterData()
	{
		// fetching master data from database
		final List<MerchantMasterTableModel> masterData = getFetchMerchantMasterTableService().fetchDetails();
		if (null != masterData && !masterData.isEmpty())
		{
			getMerchantMasterXMLUtility().generateMasterData(masterData);
		}

	}


	protected FetchSalesOrderService getFetchSalesOrderService()
	{
		return Registry.getApplicationContext().getBean("fetchSalesOrderServiceImpl", FetchSalesOrderService.class);
	}

	protected MerchantMasterXMLUtility getMerchantMasterXMLUtility()
	{
		return Registry.getApplicationContext().getBean("merchantMasterXMLUtility", MerchantMasterXMLUtility.class);
	}

	protected SalesOrderXMLUtility getSalesOrderXMLUtility()
	{
		return Registry.getApplicationContext().getBean("salesOrderXMLUtility", SalesOrderXMLUtility.class);
	}
}
