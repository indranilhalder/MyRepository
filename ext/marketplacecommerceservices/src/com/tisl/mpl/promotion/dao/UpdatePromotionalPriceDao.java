/**
 *
 */
package com.tisl.mpl.promotion.dao;

import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface UpdatePromotionalPriceDao
{
	List<PriceRowModel> fetchPricedData(List<String> product);

}
