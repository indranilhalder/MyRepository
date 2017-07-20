/**
 *
 */
package com.tisl.mpl.core.promotion.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.PromotionPriceUpdaterService;
import com.tisl.mpl.model.MplConfigurationModel;
//Removed as UNUSED IMPORTS
//import com.tisl.mpl.promotion.dao.impl.UpdatePromotionalPriceDaoImpl;
import com.tisl.mpl.util.ExceptionUtil;
//Removed as UNUSED IMPORTS
//import javax.annotation.Resource;
// import org.apache.commons.collections.CollectionUtils;   //Sonar fix


/**
 * @author TCS
 *
 */
public class PromotionPriceUpdaterJob extends AbstractJobPerformable<CronJobModel>
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PromotionPriceUpdaterJob.class.getName());

	@Autowired
	private PromotionPriceUpdaterService promotionPriceUpdaterService;

	/*
	 * @Autowired private MarketplaceCoreHMCExtension marketplaceCoreHMCExtension;
	 */

	//SONAR FIX-UNUSED VARIABLE
	//@Resource(name = "mplUpdatePromotionPriceDao")
	//private UpdatePromotionalPriceDaoImpl updatePromotionalPriceDao;


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

	/**
	 * @description Cron job to update Price Row for priority respective promotional products
	 */
	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		try
		{

			boolean errorFlag = false;


			final MplConfigurationModel configModel = promotionPriceUpdaterService.getCronDetails(cronJob.getCode());

			if (null != configModel && null != configModel.getMplConfigDate())
			{
				LOG.error("CRON LAST START DATE" + configModel.getMplConfigDate());
				List<ProductPromotionModel> promotionList = new ArrayList<ProductPromotionModel>();
				promotionList = fetchRequiredPromotion(configModel.getMplConfigDate());
				if (CollectionUtils.isNotEmpty(promotionList))
				{
					for (final ProductPromotionModel promoModel : promotionList)
					{

						errorFlag = promotionPriceUpdaterService.poulatePriceRowData(promoModel);

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
			promotionPriceUpdaterService.purgeRedundantData();
		}
	}

	/**
	 * @description fetch Required Promotions
	 *
	 *
	 * @param mplConfigDate
	 * @return promotionResultList
	 *
	 */
	private List<ProductPromotionModel> fetchRequiredPromotion(final Date mplConfigDate)
	{
		List<ProductPromotionModel> promotionResultList = new ArrayList<ProductPromotionModel>();

		promotionResultList = promotionPriceUpdaterService.getRequiredPromotion(mplConfigDate);

		return promotionResultList;

	}

}
