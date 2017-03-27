/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public interface PromotionPriceUpdaterService
{
	List<ProductPromotionModel> getRequiredPromotion(Date mplConfigDate);

	boolean poulatePriceRowData(final ProductPromotionModel buyAPercentageDiscount);

	MplConfigurationModel getCronDetails(String code);

	/**
	 * @Description : Save CronJob Configuration Details
	 * @param: startTime
	 * @param: code
	 */
	void saveCronData(final CronJobModel cronModel);

}