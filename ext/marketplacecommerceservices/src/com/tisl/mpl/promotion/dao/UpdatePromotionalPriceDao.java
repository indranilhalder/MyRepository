/**
 *
 */
package com.tisl.mpl.promotion.dao;

import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.List;

import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public interface UpdatePromotionalPriceDao
{
	List<PriceRowModel> fetchPricedData(List<String> product);

	List<PriceRowModel> fetchPromoPriceData(String promoCode);

	/**
	 * to fetch the cron details
	 *
	 * @param code
	 * @return MplConfigurationModel
	 */
	MplConfigurationModel getCronDetails(String code);

}