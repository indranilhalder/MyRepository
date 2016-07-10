/**
 *
 */
package com.tisl.mpl.core.cronjob.priority;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import javax.annotation.Resource;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerPriorityService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class UpdateSellerPriorityCronJob extends AbstractJobPerformable<CronJobModel>
{
	@Resource(name = "mplSellerPriorityService")
	private MplSellerPriorityService mplSellerPriorityService;

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		PerformResult perform = null;
		try
		{
			if (mplSellerPriorityService.updateSellerPriorityDetails())
			{
				perform = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
			}
			else
			{
				perform = new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
			}

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);

		}
		return perform;

	}
}
