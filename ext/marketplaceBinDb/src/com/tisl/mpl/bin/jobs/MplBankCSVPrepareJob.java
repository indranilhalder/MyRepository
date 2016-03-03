/**
 *
 */
package com.tisl.mpl.bin.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.jobs.PromotionCreationJob;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class MplBankCSVPrepareJob extends AbstractJobPerformable<CronJobModel>
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PromotionCreationJob.class.getName());

	/**
	 * @Description : Generate CSV with Bank Details present in Bin but not in Bank
	 *
	 *
	 * @param: oModel
	 */
	@Override
	public PerformResult perform(final CronJobModel oModel)
	{
		try
		{
			LOG.debug("Generate CSV with Bank Data");
			generateFileData();
		}
		catch (final EtailBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
			ExceptionUtil.etailBusinessExceptionHandler(exception, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
			ExceptionUtil.etailNonBusinessExceptionHandler(exception);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 *
	 * @Description : Generate CSV with Bank Details present in Bin but not in Bank
	 *
	 */
	private void generateFileData()
	{
		// YTODO Auto-generated method stub
	}

}
