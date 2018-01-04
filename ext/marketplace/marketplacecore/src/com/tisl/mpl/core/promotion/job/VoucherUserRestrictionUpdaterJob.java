/**
 *
 */
package com.tisl.mpl.core.promotion.job;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.voucher.model.CouponUserRestrictionModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.PromotionPriceUpdaterService;
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class VoucherUserRestrictionUpdaterJob
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PromotionPriceUpdaterJob.class.getName());
	@Autowired
	private PromotionPriceUpdaterService promotionPriceUpdaterService;

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
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	//TPR-7408 starts here
	@Autowired
	private ConfigurationService configurationService;

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
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}


	public PerformResult perform(final CronJobModel cronJob)
	{
		try
		{

			boolean errorFlag = false;


			final MplConfigurationModel configModel = promotionPriceUpdaterService.getCronDetails(cronJob.getCode());

			if (null != configModel && null != configModel.getMplConfigDate())
			{
				//LOG.debug("CRON LAST START DATE" + configModel.getMplConfigDate());
				List<UserRestrictionModel> userRestriction = new ArrayList<UserRestrictionModel>();
				final String queryString = configurationService.getConfiguration().getString("voucher.couponUserRestriction.query");
				LOG.debug("The queryString is " + queryString);
				final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
				query.addQueryParameter("earlierDate", configModel.getMplConfigDate());
				LOG.debug("QUERY>>>>>>" + query);
				userRestriction = getFlexibleSearchService().<UserRestrictionModel> search(query).getResult();
				LOG.debug(userRestriction);
				//final List<PrincipalModel> restrCustomerList = new ArrayList<PrincipalModel>();
				//final List<VoucherModel> voucherList = new ArrayList<VoucherModel>();
				final SortedMap<VoucherModel, List<PrincipalModel>> voucherUserMap = new TreeMap<VoucherModel, List<PrincipalModel>>();
				for (final UserRestrictionModel restrictedUser : userRestriction)
				{
					voucherUserMap.put(restrictedUser.getVoucher(), new ArrayList<PrincipalModel>(restrictedUser.getUsers()));

					//restrCustomerList.addAll(restrictedUser.getUsers());
					//voucherList.add(restrictedUser.getVoucher());
					//					for (final PrincipalModel user : restrictedUser.getUsers())
					//					{
					//						restrCustomerList.add(user);
					//
					//					}
				}

				//final String queryString = configurationService.getConfiguration().getString("voucher.couponUserRestriction.query");
				final String queryStr = "SELECT {pk} FROM {CouponUserRestriction} where {vouchers} in ?voucherList";
				LOG.debug("The queryStr is " + queryStr);
				final FlexibleSearchQuery userRestrQuery = new FlexibleSearchQuery(queryStr);
				query.addQueryParameter("voucherList", voucherUserMap.keySet());
				//LOG.debug("QUERY>>>>>>" + query);
				final List<CouponUserRestrictionModel> couponUserRestrs = getFlexibleSearchService()
						.<CouponUserRestrictionModel> search(userRestrQuery).getResult();
				if (CollectionUtils.isNotEmpty(couponUserRestrs))
				{
					getModelService().removeAll(couponUserRestrs);
				}


				for (final Map.Entry<VoucherModel, List<PrincipalModel>> pair : voucherUserMap.entrySet())
				{
					pair.getKey();
					pair.getValue();
				}


				final CouponUserRestrictionModel userRestr = getModelService().create(CouponUserRestrictionModel.class);
				userRestr.setVouchers(voucherUserMap.keySet());

				for (final List<PrincipalModel> userList : voucherUserMap.values())
				{
					for (final PrincipalModel user : userList)
					{
						userRestr.setClosedUsers(user);
					}
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

		finally
		{
			//promotionPriceUpdaterService.purgeRedundantData();
		}
		//ASFSAD
	}
}
