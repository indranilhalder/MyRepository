/**
 *
 */
package com.tisl.mpl.core.payment.cronjob;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplProcessOrderDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplThirdPartyWalletService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author 682160
 *
 */
public class MplThirdPartyWalletCronJob extends AbstractJobPerformable<CronJobModel>
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MplThirdPartyWalletCronJob.class.getName());

	@Autowired
	private MplThirdPartyWalletService mRupeeWalletService;
	@Resource(name = "mplProcessOrderDao")
	private MplProcessOrderDao mplProcessOrderDao;


	@Override
	public PerformResult perform(final CronJobModel cronModel)
	{

		try
		{
			LOG.debug("Validating and updating pendinfg mrupee orders");
			mRupeeWalletService.fetchThirdPartyAuditTableData();

		}
		catch (final EtailNonBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
			ExceptionUtil.etailNonBusinessExceptionHandler(exception);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final Exception exception)
		{
			LOG.error("Exception======================", exception);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}





}
