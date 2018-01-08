/**
 *
 */
package com.tisl.mpl.core.promotion.job;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.voucher.model.CouponUserRestrictionModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.marketplacecommerceservices.service.PromotionPriceUpdaterService;
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class VoucherUserRestrictionUpdaterJob extends AbstractJobPerformable<CronJobModel>
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PromotionPriceUpdaterJob.class.getName());
	@Autowired
	private PromotionPriceUpdaterService promotionPriceUpdaterService;

	@Autowired
	private MplVoucherService mplVoucherService;

	@Autowired
	private ModelService modelService;

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

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}


	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		try
		{

			boolean errorFlag = false;


			final MplConfigurationModel configModel = promotionPriceUpdaterService.getCronDetails(cronJob.getCode());

			if (null != configModel && null != configModel.getMplConfigDate())
			{
				final List<UserRestrictionModel> userRestriction = mplVoucherService.fetchUserRestrictionDetails(configModel
						.getMplConfigDate());

				final List<CouponUserRestrictionModel> couponUserRestlist = new ArrayList<CouponUserRestrictionModel>();

				if (CollectionUtils.isNotEmpty(userRestriction))
				{
					for (final UserRestrictionModel restrictedUser : userRestriction)
					{
						final VoucherModel voucher = restrictedUser.getVoucher();
						final List<CouponUserRestrictionModel> couponUserRestrs = mplVoucherService.fetchExistingVoucherData(voucher);

						if (CollectionUtils.isNotEmpty(couponUserRestrs))
						{
							getModelService().removeAll(couponUserRestrs);
						}

						if (CollectionUtils.isNotEmpty(restrictedUser.getUsers()))
						{
							for (final PrincipalModel user : restrictedUser.getUsers())
							{
								final CouponUserRestrictionModel userRestr = getModelService().create(CouponUserRestrictionModel.class);
								userRestr.setVoucher(voucher);
								userRestr.setClosedUser(user);
								userRestr.setPositive(restrictedUser.getPositive());
								couponUserRestlist.add(userRestr);
							}
						}
					}
					getModelService().saveAll(couponUserRestlist);
				}
			}
			else
			{
				errorFlag = true;
				LOG.debug("CRON LAST START DATE NOT RECORDED");
			}


			if (errorFlag)
			{
				return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
			}
			else
			{
				promotionPriceUpdaterService.saveCronData(cronJob);
				return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
			}
		}
		catch (final EtailBusinessExceptions exception)
		{
			LOG.error("", exception);
			ExceptionUtil.etailBusinessExceptionHandler(exception, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
			ExceptionUtil.etailNonBusinessExceptionHandler(exception);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

	}
}
