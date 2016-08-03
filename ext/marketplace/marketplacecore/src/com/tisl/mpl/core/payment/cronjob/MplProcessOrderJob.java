/**
 *
 */
package com.tisl.mpl.core.payment.cronjob;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;


/**
 * @author TCS
 *
 */
public class MplProcessOrderJob extends AbstractJobPerformable<CronJobModel>
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MplProcessOrderJob.class.getName());

	/**
	 * @Description- This job will process a Payment_Pending Order to Payment_Successful / Payment_Failed when there is
	 *               any problem in the redirection from Juspay
	 */
	@Override
	public PerformResult perform(final CronJobModel arg0)
	{

		return null;
	}

}
