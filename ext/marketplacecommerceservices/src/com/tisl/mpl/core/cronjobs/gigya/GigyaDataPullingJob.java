/**
 *
 */
package com.tisl.mpl.core.cronjobs.gigya;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.GigyaDataPullService;
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class GigyaDataPullingJob extends AbstractJobPerformable<CronJobModel>
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(GigyaDataPullingJob.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel
	 * )
	 */
	@Override
	public PerformResult perform(final CronJobModel oModel)
	{
		// YTODO Auto-generated method stub
		try
		{
			Date date = null;
			final MplConfigurationModel configModel = getGigyaDataPullService().getCronDetails(oModel.getCode());
			if (null != configModel && null != configModel.getMplConfigDate())
			{
				LOG.debug("Starting from created date of product:" + configModel.getMplConfigDate());
				final Map<String, Date> productList = getGigyaDataPullService()
						.specificProductDetails(configModel.getMplConfigDate());
				if (MapUtils.isNotEmpty(productList))
				{
					final String theLastKey = new ArrayList<>(productList.keySet()).get(productList.size() - 1);
					if (StringUtils.isNotEmpty(theLastKey))
					{
						date = productList.get(theLastKey);
					}
					final List<String> product = new ArrayList<String>();
					product.addAll(productList.keySet());
					getGigyaDataPullUtility().pullDataFromGigya(product);
				}
			}
			else
			{
				LOG.debug("1st time run");
				final Map<String, Date> productList = getGigyaDataPullService().fetchProductDetails();
				if (MapUtils.isNotEmpty(productList))
				{
					final String theLastKey = new ArrayList<>(productList.keySet()).get(productList.size() - 1);
					if (StringUtils.isNotEmpty(theLastKey))
					{
						date = productList.get(theLastKey);
					}
					final List<String> product = new ArrayList<String>();
					product.addAll(productList.keySet());
					getGigyaDataPullUtility().pullDataFromGigya(product);
				}
			}
			saveCronData(oModel, date);
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
	 * @Description : Save Cron Job Details
	 * @param oModel
	 */
	private void saveCronData(final CronJobModel oModel, final Date date)
	{
		if (null != oModel && null != date && null != oModel.getCode())
		{
			getGigyaDataPullService().saveCronDetails(date, oModel.getCode());
		}

	}

	protected GigyaDataPullService getGigyaDataPullService()
	{
		return Registry.getApplicationContext().getBean("gigyaDataPullServiceImpl", GigyaDataPullService.class);
	}

	protected GigyaDataPullUtility getGigyaDataPullUtility()
	{
		return Registry.getApplicationContext().getBean("gigyaDataPullUtility", GigyaDataPullUtility.class);
	}

}
