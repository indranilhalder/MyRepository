/**
 *
 */
package com.tisl.mpl.buyBoxDataImportJob;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.buyBoxDataImport.BuyBoxImportUtility;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.FetchSalesOrderService;
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class BuyBoxDataExtractJob extends AbstractJobPerformable<CronJobModel>
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BuyBoxDataExtractJob.class.getName());
	@Resource
	private ModelService modelService;

	final Date startDate = new Date();
	@Resource
	BuyBoxImportUtility buyBoxImportUtil;

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
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		LOG.info("Entering BuyBox extract Job Starting.....");
		final MplConfigurationModel configModel = getFetchSalesOrderService().getCronDetails(cronJob.getCode());


		try
		{
			{
				configModel.setMplConfigCode(cronJob.getCode());
				buyBoxImportUtil.executeExtraction();
			}
			cronJob.setEndTime(new Date());

			//saving Cronjob
			getModelService().save(cronJob);
			//saving the last start time
			configModel.setMplConfigDate(cronJob.getStartTime());
			//saving MplConfiguration
			getModelService().save(configModel);

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

	protected FetchSalesOrderService getFetchSalesOrderService()
	{
		return Registry.getApplicationContext().getBean("fetchSalesOrderServiceImpl", FetchSalesOrderService.class);
	}
}
