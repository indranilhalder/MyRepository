/**
 *
 */
package com.tisl.mpl.coupon.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.model.VoucherModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.model.CouponMailJobModel;


/**
 * @author TCS
 *
 */
public class CouponMailJob extends AbstractJobPerformable<CouponMailJobModel>
{
	private static final Logger LOG = Logger.getLogger(CouponMailJob.class);


	@Autowired
	private VoucherService voucherService;

	@Override
	public PerformResult perform(final CouponMailJobModel mailJob)
	{
		try
		{
			final String couponCode = mailJob.getVoucherCode();
			final VoucherModel voucher = getVoucherService().getVoucher(couponCode);
			LOG.debug("*****************Coupon Mail Cronjob is triggered*******************************");

			LOG.debug("***Voucher from the dao call is " + voucher.getCode());

			LOG.debug("*****************Coupon Mail Cronjob Ends*******************************");

			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);

		}
		catch (final Exception e)
		{
			LOG.error("**********Error*************" + e.getMessage());
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
	}

	/**
	 * @return the voucherService
	 */
	public VoucherService getVoucherService()
	{
		return voucherService;
	}

	/**
	 * @param voucherService
	 *           the voucherService to set
	 */
	public void setVoucherService(final VoucherService voucherService)
	{
		this.voucherService = voucherService;
	}





}
