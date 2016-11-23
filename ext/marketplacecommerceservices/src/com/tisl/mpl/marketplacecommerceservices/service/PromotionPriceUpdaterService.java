/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.cronjob.model.CronJobModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.model.BuyAPercentageDiscountModel;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public interface PromotionPriceUpdaterService
{
	List<BuyAPercentageDiscountModel> getRequiredPromotion(Date mplConfigDate);

	boolean poulatePriceRowData(final BuyAPercentageDiscountModel buyAPercentageDiscount);

	MplConfigurationModel getCronDetails(String code);

	/**
	 * @Description : Save CronJob Configuration Details
	 * @param: startTime
	 * @param: code
	 */
	void saveCronData(final CronJobModel cronModel);

}